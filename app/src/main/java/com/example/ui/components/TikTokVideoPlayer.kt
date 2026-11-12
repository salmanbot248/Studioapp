package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.model.TikTokVideo
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun TikTokVideoPlayer(
  video: TikTokVideo,
  isActive: Boolean,
  isMuted: Boolean,
  isAutoNext: Boolean,
  onVideoEnded: () -> Unit,
  onDoubleTap: (x: Float, y: Float) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(true) }
  var isBuffering by remember { mutableStateOf(true) }
  var showPlayPauseFlash by remember { mutableStateOf(false) }
  var flashIconIsPlay by remember { mutableStateOf(false) }
  var progress by remember { mutableFloatStateOf(0f) }

  val exoPlayer = remember(video.videoUrl) {
    ExoPlayer.Builder(context).build().apply {
      val mediaItem = MediaItem.fromUri(video.videoUrl)
      setMediaItem(mediaItem)
      prepare()
      repeatMode = if (isAutoNext) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE
      playWhenReady = isActive
      volume = if (isMuted) 0f else 1f
    }
  }

  // Update volume & repeatMode when settings change
  LaunchedEffect(isMuted) {
    exoPlayer.volume = if (isMuted) 0f else 1f
  }

  LaunchedEffect(isAutoNext) {
    exoPlayer.repeatMode = if (isAutoNext) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE
  }

  // Play or pause based on active state
  LaunchedEffect(isActive) {
    if (isActive) {
      exoPlayer.playWhenReady = true
      exoPlayer.play()
      isPlaying = true
    } else {
      exoPlayer.pause()
      exoPlayer.seekTo(0)
      isPlaying = false
    }
  }

  // Player listener
  DisposableEffect(exoPlayer) {
    val listener = object : Player.Listener {
      override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
          Player.STATE_BUFFERING -> isBuffering = true
          Player.STATE_READY -> isBuffering = false
          Player.STATE_ENDED -> {
            isBuffering = false
            if (isAutoNext) {
              onVideoEnded()
            }
          }
          else -> Unit
        }
      }

      override fun onIsPlayingChanged(playing: Boolean) {
        isPlaying = playing
      }
    }
    exoPlayer.addListener(listener)

    onDispose {
      exoPlayer.removeListener(listener)
      exoPlayer.release()
    }
  }

  // Progress polling
  LaunchedEffect(isActive, isPlaying) {
    while (isActive && isPlaying) {
      val duration = exoPlayer.duration
      val current = exoPlayer.currentPosition
      if (duration > 0) {
        progress = (current.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
      }
      delay(150)
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = { offset ->
            onDoubleTap(offset.x, offset.y)
          },
          onTap = {
            if (exoPlayer.isPlaying) {
              exoPlayer.pause()
              flashIconIsPlay = false
            } else {
              exoPlayer.play()
              flashIconIsPlay = true
            }
            showPlayPauseFlash = true
          }
        )
      }
  ) {
    // Fallback/Poster image if provided
    if (video.thumbUrl.isNotEmpty()) {
      AsyncImage(
        model = video.thumbUrl,
        contentDescription = "Video Thumbnail",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
    }

    // Video View
    AndroidView(
      factory = { ctx ->
        PlayerView(ctx).apply {
          player = exoPlayer
          useController = false
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
          layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
        }
      },
      update = { playerView ->
        playerView.player = exoPlayer
      },
      modifier = Modifier.fillMaxSize()
    )

    // Buffering indicator
    if (isBuffering && isActive) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(
          color = TikTokCyan,
          strokeWidth = 3.dp,
          modifier = Modifier.size(44.dp)
        )
      }
    }

    // Play/Pause tap flash animation
    LaunchedEffect(showPlayPauseFlash) {
      if (showPlayPauseFlash) {
        delay(600)
        showPlayPauseFlash = false
      }
    }

    AnimatedVisibility(
      visible = showPlayPauseFlash || (!isPlaying && !isBuffering && isActive),
      enter = fadeIn(tween(150)),
      exit = fadeOut(tween(300)),
      modifier = Modifier.align(Alignment.Center)
    ) {
      Surface(
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.55f),
        modifier = Modifier.size(72.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(44.dp)
          )
        }
      }
    }

    // Slim TikTok Video Progress Line at bottom
    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .height(2.dp),
      color = Color.White.copy(alpha = 0.85f),
      trackColor = Color.White.copy(alpha = 0.15f)
    )
  }
}
