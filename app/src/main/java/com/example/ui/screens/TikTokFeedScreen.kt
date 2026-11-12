package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.model.FeedTab
import com.example.model.TikTokVideo
import com.example.ui.components.TikTokOverlay
import com.example.ui.components.TikTokTopBar
import com.example.ui.components.TikTokVideoPlayer
import com.example.viewmodel.HeartAnimation
import kotlinx.coroutines.launch

@Composable
fun TikTokFeedScreen(
  videos: List<TikTokVideo>,
  currentFeedTab: FeedTab,
  onFeedTabSelected: (FeedTab) -> Unit,
  selectedCategory: String,
  onCategorySelected: (String) -> Unit,
  isMuted: Boolean,
  onToggleMute: () -> Unit,
  isAutoNext: Boolean,
  flyingHearts: List<HeartAnimation>,
  onLikeClick: (TikTokVideo) -> Unit,
  onDoubleTap: (TikTokVideo, Float, Float) -> Unit,
  onCommentClick: (TikTokVideo) -> Unit,
  onBookmarkClick: (TikTokVideo) -> Unit,
  onShareClick: (TikTokVideo) -> Unit,
  onFollowClick: (TikTokVideo) -> Unit,
  onProfileClick: () -> Unit,
  onSearchClick: () -> Unit,
  onRefreshClick: () -> Unit,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()

  // Filter videos by category if not "All"
  val filteredVideos = if (selectedCategory == "All") {
    videos
  } else {
    videos.filter { it.category.equals(selectedCategory, ignoreCase = true) }
      .ifEmpty { videos }
  }

  val pagerState = rememberPagerState(initialPage = 0) {
    filteredVideos.size
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    if (filteredVideos.isEmpty()) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text("No videos found. Check settings or refresh feed.", color = Color.White)
      }
    } else {
      VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        key = { index -> if (index in filteredVideos.indices) filteredVideos[index].id else index }
      ) { page ->
        if (page in filteredVideos.indices) {
          val video = filteredVideos[page]
          val isActive = page == pagerState.currentPage

          Box(modifier = Modifier.fillMaxSize()) {
            TikTokVideoPlayer(
              video = video,
              isActive = isActive,
              isMuted = isMuted,
              isAutoNext = isAutoNext,
              onVideoEnded = {
                if (page < filteredVideos.size - 1) {
                  coroutineScope.launch {
                    pagerState.animateScrollToPage(page + 1)
                  }
                }
              },
              onDoubleTap = { x, y ->
                onDoubleTap(video, x, y)
              }
            )

            TikTokOverlay(
              video = video,
              flyingHearts = flyingHearts,
              onLikeClick = { onLikeClick(video) },
              onCommentClick = { onCommentClick(video) },
              onBookmarkClick = { onBookmarkClick(video) },
              onShareClick = { onShareClick(video) },
              onFollowClick = { onFollowClick(video) },
              onProfileClick = onProfileClick
            )
          }
        }
      }
    }

    // Top Navigation & Filter Bar
    TikTokTopBar(
      currentTab = currentFeedTab,
      onTabSelected = onFeedTabSelected,
      isMuted = isMuted,
      onToggleMute = onToggleMute,
      onSearchClick = onSearchClick,
      onRefreshClick = onRefreshClick,
      onSettingsClick = onSettingsClick,
      selectedCategory = selectedCategory,
      onCategorySelected = onCategorySelected,
      modifier = Modifier.align(Alignment.TopCenter)
    )
  }
}
