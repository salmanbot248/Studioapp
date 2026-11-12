package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.TikTokVideo
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGold
import com.example.ui.theme.TikTokRed
import com.example.viewmodel.HeartAnimation
import kotlinx.coroutines.delay

@Composable
fun TikTokOverlay(
  video: TikTokVideo,
  flyingHearts: List<HeartAnimation>,
  onLikeClick: () -> Unit,
  onCommentClick: () -> Unit,
  onBookmarkClick: () -> Unit,
  onShareClick: () -> Unit,
  onFollowClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {

    // Subtle dark gradient at bottom for text contrast
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .height(280.dp)
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color.Black.copy(alpha = 0.45f),
              Color.Black.copy(alpha = 0.85f)
            )
          )
        )
    )

    // Left Bottom Creator & Music Details
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth(0.78f)
        .padding(start = 16.dp, bottom = 18.dp)
    ) {
      // Author Handle with click to profile
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { onProfileClick() }
          .padding(bottom = 6.dp)
      ) {
        Text(
          text = "@${video.author}",
          color = Color.White,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(6.dp))
        // Verified blue pill
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(TikTokCyan),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified",
            tint = Color.Black,
            modifier = Modifier.size(11.dp)
          )
        }
      }

      // Caption with hashtags
      var isExpanded by remember { mutableStateOf(false) }
      Text(
        text = video.caption,
        color = Color.White.copy(alpha = 0.95f),
        fontSize = 14.sp,
        lineHeight = 19.sp,
        maxLines = if (isExpanded) 10 else 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .clickable { isExpanded = !isExpanded }
          .padding(bottom = 8.dp)
      )

      // Audio marquee ticker
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.35f))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.MusicNote,
          contentDescription = "Music",
          tint = Color.White,
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = video.musicTitle,
          color = Color.White,
          fontSize = 13.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Right Side Action Column (TikTok Style)
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 12.dp, bottom = 18.dp)
    ) {
      // 1. Creator Avatar with Follow Red "+"
      Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
          .padding(bottom = 6.dp)
          .size(52.dp)
      ) {
        // Avatar circle
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(1.5.dp, Color.White, CircleShape)
            .clickable { onProfileClick() }
        ) {
          AsyncImage(
            model = video.authorAvatar,
            contentDescription = "Author Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        // Red "+" or Checkmark button
        Box(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = 4.dp)
            .size(20.dp)
            .clip(CircleShape)
            .background(if (video.isFollowing) TikTokCyan else TikTokRed)
            .clickable { onFollowClick() }
            .testTag("follow_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (video.isFollowing) Icons.Default.Check else Icons.Default.Add,
            contentDescription = if (video.isFollowing) "Following" else "Follow",
            tint = if (video.isFollowing) Color.Black else Color.White,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      // 2. Like Button with Spring Animation
      var heartBounced by remember { mutableStateOf(false) }
      val heartScale by animateFloatAsState(
        targetValue = if (heartBounced) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
        label = "heartScale"
      )

      TikTokActionButton(
        icon = if (video.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
        count = formatCount(video.likesCount),
        tint = if (video.isLiked) TikTokRed else Color.White,
        scale = heartScale,
        testTag = "like_button",
        onClick = {
          heartBounced = true
          onLikeClick()
        }
      )

      LaunchedEffect(heartBounced) {
        if (heartBounced) {
          delay(200)
          heartBounced = false
        }
      }

      // 3. Comments Button
      TikTokActionButton(
        icon = Icons.Outlined.ChatBubbleOutline,
        count = formatCount(video.commentsCount),
        tint = Color.White,
        testTag = "comments_button",
        onClick = onCommentClick
      )

      // 4. Bookmark / Favorite Button
      TikTokActionButton(
        icon = if (video.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
        count = formatCount(video.bookmarksCount),
        tint = if (video.isBookmarked) TikTokGold else Color.White,
        testTag = "bookmark_button",
        onClick = onBookmarkClick
      )

      // 5. Share Button
      TikTokActionButton(
        icon = Icons.Default.Share,
        count = formatCount(video.sharesCount),
        tint = Color.White,
        testTag = "share_button",
        onClick = onShareClick
      )

      // 6. Spinning Vinyl Music Disc
      SpinningVinylDisc(
        albumArtUrl = video.thumbUrl.ifEmpty { video.authorAvatar },
        onClick = { /* audio page */ }
      )
    }

    // Double-tap flying hearts overlay
    flyingHearts.forEach { heart ->
      FloatingHeart(heart = heart)
    }
  }
}

@Composable
fun TikTokActionButton(
  icon: ImageVector,
  count: String,
  tint: Color,
  testTag: String,
  scale: Float = 1f,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = tint,
      modifier = Modifier
        .size(36.dp)
        .scale(scale)
        .shadow(2.dp, shape = CircleShape)
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = count,
      color = Color.White,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      style = androidx.compose.ui.text.TextStyle(
        shadow = androidx.compose.ui.graphics.Shadow(
          color = Color.Black.copy(alpha = 0.8f),
          blurRadius = 4f
        )
      )
    )
  }
}

@Composable
fun SpinningVinylDisc(
  albumArtUrl: String,
  onClick: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "vinylRotation"
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .size(48.dp)
      .rotate(rotation)
      .clickable { onClick() }
  ) {
    // Vinyl outer black disk with grooves
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(Color(0xFF1B1B1B))
        .border(1.5.dp, Color(0xFF333333), CircleShape)
    )

    // Center circular art
    Box(
      modifier = Modifier
        .size(26.dp)
        .clip(CircleShape)
        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
    ) {
      AsyncImage(
        model = albumArtUrl,
        contentDescription = "Album Art",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

@Composable
fun FloatingHeart(heart: HeartAnimation) {
  var visible by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (visible) 1.4f else 0.4f,
    animationSpec = spring(dampingRatio = 0.35f, stiffness = 300f),
    label = "heartScale"
  )
  val alpha by animateFloatAsState(
    targetValue = if (visible) 1f else 0f,
    animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
    label = "heartAlpha"
  )

  LaunchedEffect(heart.id) {
    visible = true
    delay(500)
    visible = false
  }

  Box(
    modifier = Modifier
      .offset { IntOffset(heart.x.toInt() - 60, heart.y.toInt() - 60) }
      .rotate(heart.rotation)
      .scale(scale)
  ) {
    Icon(
      imageVector = Icons.Default.Favorite,
      contentDescription = "Floating Heart",
      tint = TikTokRed.copy(alpha = alpha),
      modifier = Modifier
        .size(90.dp)
        .shadow(12.dp, CircleShape)
    )
  }
}

fun formatCount(count: Int): String {
  return when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
    count >= 10_000 -> String.format("%.1fK", count / 1_000.0)
    count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
    else -> count.toString()
  }
}
