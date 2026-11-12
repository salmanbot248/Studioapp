package com.example.data

import com.example.model.Comment
import com.example.model.TikTokVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class VideoRepository {

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build()

  // Curated TikTok-style vertical videos that play reliably on Android
  val defaultVideos: List<TikTokVideo> = listOf(
    TikTokVideo(
      id = "vid_1",
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      thumbUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
      author = "neon_beats",
      authorName = "Neon Beats 🎧",
      authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&q=80",
      caption = "Sunset vibe in the city! What's your favourite evening track? ✨🏙️ #sunset #citylights #vibes #fyp #trending",
      musicTitle = "♬ Neon Beats - Summer Nights (Speed Up) · original audio",
      likesCount = 284500,
      commentsCount = 3420,
      bookmarksCount = 45100,
      sharesCount = 18200,
      isLiked = true,
      category = "Music",
      timeAgo = "2h ago",
      tags = listOf("sunset", "citylights", "vibes", "fyp", "trending")
    ),
    TikTokVideo(
      id = "vid_2",
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
      thumbUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&q=80",
      author = "drift_legend",
      authorName = "TurboDrift 🏎️",
      authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80",
      caption = "Ready for the weekend cruise! Sound on for that pure engine roar 🔊🔥 #cars #drift #supercars #motorsport #foryou",
      musicTitle = "♬ Turbo Sound - V8 Symphony (Bass Boosted)",
      likesCount = 512800,
      commentsCount = 7890,
      bookmarksCount = 89200,
      sharesCount = 34100,
      category = "Trending",
      timeAgo = "5h ago",
      tags = listOf("cars", "drift", "supercars", "motorsport", "foryou")
    ),
    TikTokVideo(
      id = "vid_3",
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
      thumbUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=800&q=80",
      author = "wanderlust_sam",
      authorName = "Sam Travels ✈️",
      authorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&q=80",
      caption = "Found this hidden paradise waterfall in the mountains! Tag someone you'd bring here 🏞️💙 #nature #wanderlust #travel #escape",
      musicTitle = "♬ Chill Vibes - Acoustic Ambient Melody",
      likesCount = 193400,
      commentsCount = 1840,
      bookmarksCount = 31200,
      sharesCount = 12500,
      category = "Travel",
      timeAgo = "1d ago",
      tags = listOf("nature", "wanderlust", "travel", "escape")
    ),
    TikTokVideo(
      id = "vid_4",
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
      thumbUrl = "https://images.unsplash.com/photo-1543610892-0b1f7e6d8ac1?w=800&q=80",
      author = "corgi_charlie",
      authorName = "Charlie The Pup 🐶",
      authorAvatar = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=200&q=80",
      caption = "Wait till the end... he really thought he could hide behind the curtain 😂🐾 #pets #dogs #funny #cuteanimals #corgi",
      musicTitle = "♬ Funny Squeak - Playful Comedy Theme",
      likesCount = 982100,
      commentsCount = 14500,
      bookmarksCount = 142000,
      sharesCount = 82400,
      category = "Funny",
      timeAgo = "3h ago",
      tags = listOf("pets", "dogs", "funny", "cuteanimals", "corgi")
    ),
    TikTokVideo(
      id = "vid_5",
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
      thumbUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800&q=80",
      author = "dance_crew_official",
      authorName = "Urban Groove Crew ⚡",
      authorAvatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200&q=80",
      caption = "New dance trend unlocked! Who's going to duet this? Drop your attempts in the comments 👇💃🕺 #dance #challenge #duet #tiktokdance",
      musicTitle = "♬ Urban Groove - Electro Funk Remix 2026",
      likesCount = 672000,
      commentsCount = 9120,
      bookmarksCount = 94300,
      sharesCount = 47800,
      category = "Dance",
      timeAgo = "6h ago",
      tags = listOf("dance", "challenge", "duet", "tiktokdance")
    )
  )

  suspend fun fetchFeed(customUrl: String? = null): List<TikTokVideo> = withContext(Dispatchers.IO) {
    val urlsToTry = mutableListOf<String>()
    if (!customUrl.isNullOrBlank()) {
      urlsToTry.add(customUrl)
    }
    urlsToTry.add("https://salmanbot248.github.io/jazz-24-7-bot-tg/feed.json")
    urlsToTry.add("https://raw.githubusercontent.com/salmanbot248/jazz-24-7-bot-tg/main/feed.json")

    for (url in urlsToTry) {
      try {
        val request = Request.Builder()
          .url(url)
          .header("User-Agent", "TikTok-Android/1.0")
          .build()

        client.newCall(request).execute().use { response ->
          if (response.isSuccessful) {
            val jsonStr = response.body?.string() ?: return@use
            val parsedList = parseFeedJson(jsonStr)
            if (parsedList.isNotEmpty()) {
              return@withContext parsedList
            }
          }
        }
      } catch (e: Exception) {
        // Continue to fallback
      }
    }
    return@withContext defaultVideos
  }

  private fun parseFeedJson(jsonStr: String): List<TikTokVideo> {
    val list = mutableListOf<TikTokVideo>()
    try {
      val trimmed = jsonStr.trim()
      val jsonArray: JSONArray = if (trimmed.startsWith("[")) {
        JSONArray(trimmed)
      } else {
        val obj = JSONObject(trimmed)
        if (obj.has("items")) obj.getJSONArray("items") else JSONArray()
      }

      for (i in 0 until jsonArray.length()) {
        val item = jsonArray.getJSONObject(i)
        val url = item.optString("direct").ifEmpty {
          item.optString("url").ifEmpty {
            item.optString("share_url")
          }
        }
        val title = item.optString("title").ifEmpty {
          item.optString("caption", "TikTok Short Video")
        }
        val author = item.optString("author").ifEmpty {
          item.optString("profile", "creator_${i + 1}")
        }.replace("@", "")
        val thumb = item.optString("thumb").ifEmpty {
          item.optString("thumbnail", "")
        }
        val platform = item.optString("platform", "Trending")

        if (url.isNotEmpty()) {
          list.add(
            TikTokVideo(
              id = "feed_${item.optString("id", i.toString())}",
              videoUrl = url,
              thumbUrl = thumb,
              author = author,
              authorName = "@$author",
              authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&q=80",
              caption = "$title #fyp #viral #trending #tiktok",
              musicTitle = "♬ Original sound - $author",
              likesCount = (12000..890000).random(),
              commentsCount = (300..15000).random(),
              bookmarksCount = (1200..45000).random(),
              sharesCount = (500..20000).random(),
              category = platform.replaceFirstChar { it.uppercase() },
              timeAgo = "Recently",
              tags = listOf("fyp", "viral", "trending")
            )
          )
        }
      }
    } catch (_: Exception) {
      // Fallback
    }

    return if (list.isNotEmpty()) list else defaultVideos
  }

  fun getDefaultComments(videoAuthor: String): List<Comment> {
    return listOf(
      Comment(
        id = "c1",
        user = "sarah_miller",
        userName = "Sarah Miller",
        avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&q=80",
        text = "This is literally the best content on my FYP today!! 🔥😍",
        timeAgo = "2h",
        likes = 1240,
        isLiked = true
      ),
      Comment(
        id = "c2",
        user = "david_beats",
        userName = "David K",
        avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&q=80",
        text = "Bro the transition at 0:04 was SO clean holy shhh 👏",
        timeAgo = "4h",
        likes = 852
      ),
      Comment(
        id = "c3",
        user = "elena_v",
        userName = "Elena Volkova",
        avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
        text = "Added to my favorites immediately ❤️ need part 2 please!!",
        timeAgo = "6h",
        likes = 431
      ),
      Comment(
        id = "c4",
        user = "gamer_zack",
        userName = "Zack Plays",
        avatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=100&q=80",
        text = "Algorithm blessed me with this masterpiece 😂",
        timeAgo = "1d",
        likes = 219
      ),
      Comment(
        id = "c5",
        user = videoAuthor,
        userName = "@$videoAuthor (Creator)",
        avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
        text = "Thanks for all the love everyone!! Part 2 is dropping tomorrow at 6 PM! 🚀❤️",
        timeAgo = "1h",
        likes = 3450,
        isLiked = true
      )
    )
  }
}
