package com.example.model

data class TikTokVideo(
  val id: String,
  val videoUrl: String,
  val thumbUrl: String,
  val author: String,
  val authorName: String,
  val authorAvatar: String,
  val caption: String,
  val musicTitle: String,
  val likesCount: Int,
  val commentsCount: Int,
  val bookmarksCount: Int,
  val sharesCount: Int,
  val isLiked: Boolean = false,
  val isBookmarked: Boolean = false,
  val isFollowing: Boolean = false,
  val category: String = "All",
  val timeAgo: String = "Just now",
  val tags: List<String> = emptyList()
)

data class Comment(
  val id: String,
  val user: String,
  val userName: String,
  val avatar: String,
  val text: String,
  val timeAgo: String,
  val likes: Int,
  val isLiked: Boolean = false
)

enum class TikTokNavTab {
  HOME,
  FRIENDS,
  CREATE,
  INBOX,
  PROFILE
}

enum class FeedTab {
  FOLLOWING,
  FOR_YOU,
  LIVE
}
