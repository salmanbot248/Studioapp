package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.TikTokVideo
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@Composable
fun ProfileScreen(
  videos: List<TikTokVideo>,
  onVideoClick: (TikTokVideo) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TikTokDark)
      .statusBarsPadding()
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Spacer(modifier = Modifier.width(32.dp))
      Text(
        text = "Alex Creator",
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      IconButton(onClick = { /* menu */ }, modifier = Modifier.size(32.dp)) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Menu",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    // Avatar + Handle
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(90.dp)
          .clip(CircleShape)
          .border(2.dp, TikTokCyan, CircleShape)
      ) {
        AsyncImage(
          model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&q=80",
          contentDescription = "Profile Avatar",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "@alex_creator_official",
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Stats Row: Following | Followers | Likes
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        ProfileStat(count = "184", label = "Following")
        ProfileDivider()
        ProfileStat(count = "48.2K", label = "Followers")
        ProfileDivider()
        ProfileStat(count = "390.5K", label = "Likes")
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Buttons: Edit Profile | Share Profile
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
      ) {
        Button(
          onClick = { /* edit */ },
          colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("Edit profile", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
          onClick = { /* share */ },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("Share profile", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bio
      Text(
        text = "🎬 Creative director & visual storyteller\n✨ Sharing daily cinematic vibes | London 🇬🇧",
        fontSize = 13.sp,
        color = Color.White.copy(alpha = 0.85f),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.padding(horizontal = 32.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Profile Grid Tabs (Videos, Lock, Bookmark, Likes)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      listOf(
        Icons.Default.GridOn to 0,
        Icons.Default.Lock to 1,
        Icons.Default.Bookmark to 2,
        Icons.Default.Favorite to 3
      ).forEach { (icon, idx) ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { selectedTab = idx }
            .padding(vertical = 8.dp)
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selectedTab == idx) Color.White else Color.White.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Box(
            modifier = Modifier
              .width(36.dp)
              .height(2.dp)
              .background(if (selectedTab == idx) Color.White else Color.Transparent)
          )
        }
      }
    }

    Divider(color = Color.White.copy(alpha = 0.1f))

    // 3-Column Video Grid
    val displayVideos = when (selectedTab) {
      1 -> videos.take(1)
      2 -> videos.filter { it.isBookmarked }.ifEmpty { videos.take(2) }
      3 -> videos.filter { it.isLiked }.ifEmpty { videos.take(3) }
      else -> videos
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(3),
      modifier = Modifier
        .fillMaxSize()
        .padding(1.dp),
      horizontalArrangement = Arrangement.spacedBy(1.dp),
      verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
      items(displayVideos) { video ->
        Box(
          modifier = Modifier
            .aspectRatio(0.75f)
            .background(TikTokCard)
            .clickable { onVideoClick(video) }
        ) {
          AsyncImage(
            model = video.thumbUrl.ifEmpty { video.authorAvatar },
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Play count badge at bottom left
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = com.example.ui.components.formatCount(video.likesCount / 2),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProfileStat(count: String, label: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 14.dp)
  ) {
    Text(
      text = count,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )
    Text(
      text = label,
      fontSize = 12.sp,
      color = Color.White.copy(alpha = 0.6f)
    )
  }
}

@Composable
fun ProfileDivider() {
  Box(
    modifier = Modifier
      .height(18.dp)
      .width(1.dp)
      .background(Color.White.copy(alpha = 0.2f))
  )
}
