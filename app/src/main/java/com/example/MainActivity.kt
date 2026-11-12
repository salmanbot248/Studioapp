package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.TikTokNavTab
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.SearchOverlay
import com.example.ui.components.SettingsBottomSheet
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.TikTokBottomNav
import com.example.ui.screens.CreateVideoScreen
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TikTokFeedScreen
import com.example.ui.theme.TikTokTheme
import com.example.viewmodel.TikTokViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      TikTokTheme {
        TikTokApp()
      }
    }
  }
}

@Composable
fun TikTokApp(
  viewModel: TikTokViewModel = viewModel()
) {
  val videos by viewModel.videos.collectAsState()
  val currentFeedTab by viewModel.currentFeedTab.collectAsState()
  val currentNavTab by viewModel.currentNavTab.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val isMuted by viewModel.isMuted.collectAsState()
  val isAutoNext by viewModel.isAutoNext.collectAsState()
  val activeCommentsVideo by viewModel.activeCommentsVideo.collectAsState()
  val commentsMap by viewModel.comments.collectAsState()
  val activeShareVideo by viewModel.activeShareVideo.collectAsState()
  val flyingHearts by viewModel.flyingHearts.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val isSearchOpen by viewModel.isSearchOpen.collectAsState()
  val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
  val customFeedUrl by viewModel.customFeedUrl.collectAsState()
  val toastMessage by viewModel.toastMessage.collectAsState()

  // Handle back button when in sub-screens
  if (currentNavTab != TikTokNavTab.HOME) {
    BackHandler {
      viewModel.selectNavTab(TikTokNavTab.HOME)
    }
  }

  if (isSearchOpen) {
    BackHandler {
      viewModel.toggleSearch(false)
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Black,
      bottomBar = {
        // Bottom nav is visible on all main tabs except camera/create mode
        if (currentNavTab != TikTokNavTab.CREATE && !isSearchOpen) {
          TikTokBottomNav(
            currentTab = currentNavTab,
            onTabSelected = { tab ->
              viewModel.selectNavTab(tab)
            }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = if (currentNavTab != TikTokNavTab.CREATE && !isSearchOpen) innerPadding.calculateBottomPadding() else 0.dp)
      ) {
        when (currentNavTab) {
          TikTokNavTab.HOME -> {
            TikTokFeedScreen(
              videos = videos,
              currentFeedTab = currentFeedTab,
              onFeedTabSelected = { viewModel.selectFeedTab(it) },
              selectedCategory = selectedCategory,
              onCategorySelected = { viewModel.selectCategory(it) },
              isMuted = isMuted,
              onToggleMute = { viewModel.toggleMute() },
              isAutoNext = isAutoNext,
              flyingHearts = flyingHearts,
              onLikeClick = { viewModel.toggleLike(it) },
              onDoubleTap = { v, x, y -> viewModel.onDoubleTap(v, x, y) },
              onCommentClick = { viewModel.openComments(it) },
              onBookmarkClick = { viewModel.toggleBookmark(it) },
              onShareClick = { viewModel.openShare(it) },
              onFollowClick = { viewModel.toggleFollow(it) },
              onProfileClick = { viewModel.selectNavTab(TikTokNavTab.PROFILE) },
              onSearchClick = { viewModel.toggleSearch(true) },
              onRefreshClick = { viewModel.loadFeed() },
              onSettingsClick = { viewModel.toggleSettings(true) }
            )
          }

          TikTokNavTab.FRIENDS -> {
            FriendsScreen(
              videos = videos,
              onVideoClick = {
                viewModel.selectNavTab(TikTokNavTab.HOME)
              }
            )
          }

          TikTokNavTab.CREATE -> {
            CreateVideoScreen(
              onClose = { viewModel.selectNavTab(TikTokNavTab.HOME) },
              onVideoCreated = {
                viewModel.showToast("Video uploaded to your TikTok profile! 🎬")
                viewModel.selectNavTab(TikTokNavTab.PROFILE)
              }
            )
          }

          TikTokNavTab.INBOX -> {
            InboxScreen(
              onNavigateHome = { viewModel.selectNavTab(TikTokNavTab.HOME) }
            )
          }

          TikTokNavTab.PROFILE -> {
            ProfileScreen(
              videos = videos,
              onVideoClick = {
                viewModel.selectNavTab(TikTokNavTab.HOME)
              }
            )
          }
        }
      }
    }

    // Search Overlay
    AnimatedVisibility(
      visible = isSearchOpen,
      enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 4 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 4 })
    ) {
      SearchOverlay(
        searchQuery = searchQuery,
        onQueryChange = { viewModel.setSearchQuery(it) },
        onClose = { viewModel.toggleSearch(false) },
        videos = videos,
        onVideoClick = {
          viewModel.toggleSearch(false)
          viewModel.selectNavTab(TikTokNavTab.HOME)
        }
      )
    }

    // Comments Bottom Sheet
    activeCommentsVideo?.let { video ->
      val videoComments = commentsMap[video.id] ?: emptyList()
      CommentsBottomSheet(
        video = video,
        comments = videoComments,
        onDismiss = { viewModel.closeComments() },
        onAddComment = { text -> viewModel.addComment(video.id, text) },
        onLikeComment = { cId -> viewModel.toggleCommentLike(video.id, cId) }
      )
    }

    // Share Bottom Sheet
    activeShareVideo?.let { video ->
      ShareBottomSheet(
        video = video,
        onDismiss = { viewModel.closeShare() },
        onActionClick = { msg -> viewModel.showToast(msg) }
      )
    }

    // Settings Bottom Sheet
    if (isSettingsOpen) {
      SettingsBottomSheet(
        currentFeedUrl = customFeedUrl,
        isAutoNext = isAutoNext,
        onAutoNextToggle = { viewModel.toggleAutoNext() },
        onUpdateFeedUrl = { viewModel.setCustomFeedUrl(it) },
        onRefreshFeed = { viewModel.loadFeed() },
        onResetFeed = {
          viewModel.setCustomFeedUrl("https://salmanbot248.github.io/jazz-24-7-bot-tg/feed.json")
        },
        onDismiss = { viewModel.toggleSettings(false) }
      )
    }

    // Animated Toast Banner
    AnimatedVisibility(
      visible = toastMessage != null,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding()
        .padding(bottom = 72.dp)
    ) {
      toastMessage?.let { msg ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .padding(horizontal = 16.dp, vertical = 9.dp)
        ) {
          Text(
            text = msg,
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
          )
        }
      }
    }
  }
}
