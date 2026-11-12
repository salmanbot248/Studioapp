package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@Composable
fun SearchOverlay(
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  onClose: () -> Unit,
  videos: List<TikTokVideo>,
  onVideoClick: (TikTokVideo) -> Unit,
  modifier: Modifier = Modifier
) {
  val trendingTags = listOf("fyp", "viral", "supercars", "dance", "nature", "comedy", "citylights")
  val filteredVideos = if (searchQuery.isBlank()) {
    videos
  } else {
    videos.filter {
      it.caption.contains(searchQuery, ignoreCase = true) ||
      it.author.contains(searchQuery, ignoreCase = true) ||
      it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TikTokDark)
      .statusBarsPadding()
  ) {
    // Top Search Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
      }

      OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        placeholder = { Text("Search TikTok creators, sounds, hashtags...", color = Color.Gray, fontSize = 13.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
          }
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = TikTokCard,
          unfocusedContainerColor = TikTokCard,
          focusedBorderColor = Color.Transparent,
          unfocusedBorderColor = Color.Transparent,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.weight(1f),
        singleLine = true
      )

      Spacer(modifier = Modifier.width(6.dp))

      Text(
        text = "Search",
        color = TikTokRed,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .clickable { /* search trigger */ }
          .padding(8.dp)
      )
    }

    // Trending Tags pills
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      trendingTags.take(4).forEach { tag ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TikTokCard)
            .clickable { onQueryChange(tag) }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.TrendingUp, contentDescription = null, tint = TikTokRed, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "#$tag", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Results Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(filteredVideos) { video ->
        Box(
          modifier = Modifier
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(10.dp))
            .background(TikTokCard)
            .clickable {
              onVideoClick(video)
              onClose()
            }
        ) {
          AsyncImage(
            model = video.thumbUrl.ifEmpty { video.authorAvatar },
            contentDescription = video.caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(Color.Black.copy(alpha = 0.65f))
              .fillMaxWidth()
              .padding(8.dp)
          ) {
            Text(text = video.caption, color = Color.White, fontSize = 12.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "@${video.author}", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
