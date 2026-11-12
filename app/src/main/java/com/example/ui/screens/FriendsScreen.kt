package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@Composable
fun FriendsScreen(
  videos: List<TikTokVideo>,
  onVideoClick: (TikTokVideo) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TikTokDark)
      .statusBarsPadding()
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Friends",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      Row {
        IconButton(onClick = { /* search friends */ }) {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
        }
        IconButton(onClick = { /* add friends */ }) {
          Icon(Icons.Default.GroupAdd, contentDescription = "Add Friends", tint = Color.White)
        }
      }
    }

    // Connect with Friends Banner
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(TikTokCard)
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Find your contacts on TikTok",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "Sync contacts to see videos from people you know",
          fontSize = 12.sp,
          color = Color.White.copy(alpha = 0.6f)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Button(
        onClick = { /* sync */ },
        colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text("Find", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Friends Videos Feed Grid
    Text(
      text = "Trending among friends",
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(videos) { video ->
        Box(
          modifier = Modifier
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(12.dp))
            .background(TikTokCard)
        ) {
          AsyncImage(
            model = video.thumbUrl.ifEmpty { video.authorAvatar },
            contentDescription = video.caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Overlay details
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(Color.Black.copy(alpha = 0.65f))
              .fillMaxWidth()
              .padding(8.dp)
          ) {
            Text(
              text = video.caption,
              color = Color.White,
              fontSize = 12.sp,
              maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(18.dp)
                  .clip(CircleShape)
              ) {
                AsyncImage(
                  model = video.authorAvatar,
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = video.author,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }
}
