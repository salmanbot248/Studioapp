package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.VideoRepository
import com.example.model.Comment
import com.example.model.FeedTab
import com.example.model.TikTokNavTab
import com.example.model.TikTokVideo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HeartAnimation(
  val id: Long,
  val x: Float,
  val y: Float,
  val rotation: Float
)

class TikTokViewModel(
  private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

  private val _videos = MutableStateFlow<List<TikTokVideo>>(repository.defaultVideos)
  val videos: StateFlow<List<TikTokVideo>> = _videos.asStateFlow()

  private val _currentVideoIndex = MutableStateFlow(0)
  val currentVideoIndex: StateFlow<Int> = _currentVideoIndex.asStateFlow()

  private val _currentFeedTab = MutableStateFlow(FeedTab.FOR_YOU)
  val currentFeedTab: StateFlow<FeedTab> = _currentFeedTab.asStateFlow()

  private val _currentNavTab = MutableStateFlow(TikTokNavTab.HOME)
  val currentNavTab: StateFlow<TikTokNavTab> = _currentNavTab.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _isMuted = MutableStateFlow(false)
  val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

  private val _isAutoNext = MutableStateFlow(true)
  val isAutoNext: StateFlow<Boolean> = _isAutoNext.asStateFlow()

  private val _activeCommentsVideo = MutableStateFlow<TikTokVideo?>(null)
  val activeCommentsVideo: StateFlow<TikTokVideo?> = _activeCommentsVideo.asStateFlow()

  private val _comments = MutableStateFlow<Map<String, List<Comment>>>(emptyMap())
  val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

  private val _activeShareVideo = MutableStateFlow<TikTokVideo?>(null)
  val activeShareVideo: StateFlow<TikTokVideo?> = _activeShareVideo.asStateFlow()

  private val _flyingHearts = MutableStateFlow<List<HeartAnimation>>(emptyList())
  val flyingHearts: StateFlow<List<HeartAnimation>> = _flyingHearts.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _isSearchOpen = MutableStateFlow(false)
  val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

  private val _isSettingsOpen = MutableStateFlow(false)
  val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

  private val _customFeedUrl = MutableStateFlow("https://salmanbot248.github.io/jazz-24-7-bot-tg/feed.json")
  val customFeedUrl: StateFlow<String> = _customFeedUrl.asStateFlow()

  private val _isLoadingFeed = MutableStateFlow(false)
  val isLoadingFeed: StateFlow<Boolean> = _isLoadingFeed.asStateFlow()

  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  init {
    loadFeed()
  }

  fun loadFeed() {
    viewModelScope.launch {
      _isLoadingFeed.value = true
      val fetched = repository.fetchFeed(_customFeedUrl.value)
      if (fetched.isNotEmpty()) {
        _videos.value = fetched
        // Pre-populate comments for videos
        val map = mutableMapOf<String, List<Comment>>()
        fetched.forEach { v ->
          map[v.id] = repository.getDefaultComments(v.author)
        }
        _comments.value = map
      }
      _isLoadingFeed.value = false
    }
  }

  fun setCurrentVideoIndex(index: Int) {
    if (index in _videos.value.indices) {
      _currentVideoIndex.value = index
    }
  }

  fun selectFeedTab(tab: FeedTab) {
    _currentFeedTab.value = tab
  }

  fun selectNavTab(tab: TikTokNavTab) {
    _currentNavTab.value = tab
  }

  fun selectCategory(category: String) {
    _selectedCategory.value = category
  }

  fun toggleMute() {
    _isMuted.value = !_isMuted.value
    showToast(if (_isMuted.value) "Sound Muted 🔇" else "Sound Unmuted 🔊")
  }

  fun toggleAutoNext() {
    _isAutoNext.value = !_isAutoNext.value
    showToast(if (_isAutoNext.value) "Auto-Next On: Plays next video automatically" else "Loop Mode On: Current video loops")
  }

  fun toggleLike(video: TikTokVideo) {
    _videos.update { list ->
      list.map { item ->
        if (item.id == video.id) {
          val newLiked = !item.isLiked
          val delta = if (newLiked) 1 else -1
          item.copy(isLiked = newLiked, likesCount = (item.likesCount + delta).coerceAtLeast(0))
        } else item
      }
    }
  }

  fun onDoubleTap(video: TikTokVideo, x: Float, y: Float) {
    // Like the video if not already liked
    if (!video.isLiked) {
      toggleLike(video)
    }
    // Spawn floating heart animation
    val anim = HeartAnimation(
      id = System.currentTimeMillis() + (0..999).random(),
      x = x,
      y = y,
      rotation = (-25..25).random().toFloat()
    )
    _flyingHearts.update { it + anim }
    viewModelScope.launch {
      delay(900)
      _flyingHearts.update { list -> list.filterNot { it.id == anim.id } }
    }
  }

  fun toggleBookmark(video: TikTokVideo) {
    _videos.update { list ->
      list.map { item ->
        if (item.id == video.id) {
          val newBookmarked = !item.isBookmarked
          val delta = if (newBookmarked) 1 else -1
          item.copy(isBookmarked = newBookmarked, bookmarksCount = (item.bookmarksCount + delta).coerceAtLeast(0))
        } else item
      }
    }
    val updated = _videos.value.find { it.id == video.id }
    showToast(if (updated?.isBookmarked == true) "Added to Favorites ⭐" else "Removed from Favorites")
  }

  fun toggleFollow(video: TikTokVideo) {
    _videos.update { list ->
      list.map { item ->
        if (item.author == video.author) {
          item.copy(isFollowing = !item.isFollowing)
        } else item
      }
    }
    val updated = _videos.value.find { it.id == video.id }
    showToast(if (updated?.isFollowing == true) "Following @${video.author} ✨" else "Unfollowed @${video.author}")
  }

  fun openComments(video: TikTokVideo) {
    if (!_comments.value.containsKey(video.id)) {
      _comments.update { map ->
        map + (video.id to repository.getDefaultComments(video.author))
      }
    }
    _activeCommentsVideo.value = video
  }

  fun closeComments() {
    _activeCommentsVideo.value = null
  }

  fun addComment(videoId: String, text: String) {
    if (text.isBlank()) return
    val newComment = Comment(
      id = "c_${System.currentTimeMillis()}",
      user = "you",
      userName = "You",
      avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
      text = text.trim(),
      timeAgo = "Just now",
      likes = 0
    )
    _comments.update { map ->
      val currentList = map[videoId] ?: emptyList()
      map + (videoId to listOf(newComment) + currentList)
    }
    _videos.update { list ->
      list.map { if (it.id == videoId) it.copy(commentsCount = it.commentsCount + 1) else it }
    }
    showToast("Comment posted!")
  }

  fun toggleCommentLike(videoId: String, commentId: String) {
    _comments.update { map ->
      val currentList = map[videoId] ?: return@update map
      val updatedList = currentList.map { c ->
        if (c.id == commentId) {
          val newLiked = !c.isLiked
          c.copy(isLiked = newLiked, likes = if (newLiked) c.likes + 1 else (c.likes - 1).coerceAtLeast(0))
        } else c
      }
      map + (videoId to updatedList)
    }
  }

  fun openShare(video: TikTokVideo) {
    _activeShareVideo.value = video
  }

  fun closeShare() {
    _activeShareVideo.value = null
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun toggleSearch(isOpen: Boolean) {
    _isSearchOpen.value = isOpen
    if (!isOpen) _searchQuery.value = ""
  }

  fun toggleSettings(isOpen: Boolean) {
    _isSettingsOpen.value = isOpen
  }

  fun setCustomFeedUrl(url: String) {
    _customFeedUrl.value = url.trim()
    loadFeed()
    showToast("Feed URL updated & reloaded!")
  }

  fun showToast(msg: String) {
    _toastMessage.value = msg
    viewModelScope.launch {
      delay(2200)
      if (_toastMessage.value == msg) {
        _toastMessage.value = null
      }
    }
  }
}
