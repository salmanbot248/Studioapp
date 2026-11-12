package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

data class InboxNotification(
  val id: String,
  val user: String,
  val avatar: String,
  val text: String,
  val time: String,
  val isFollow: Boolean = false,
  val isUnread: Boolean = false
)

@Composable
fun InboxScreen(
  onNavigateHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  val notifications = listOf(
    InboxNotification(
      id = "1",
      user = "sarah_miller",
      avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&q=80",
      text = "started following you",
      time = "2h",
      isFollow = true,
      isUnread = true
    ),
    InboxNotification(
      id = "2",
      user = "TurboDrift 🏎️",
      avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&q=80",
      text = "liked your video: 'Sunset vibe in the city!'",
      time = "5h",
      isUnread = true
    ),
    InboxNotification(
      id = "3",
      user = "Elena Volkova",
      avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
      text = "commented: 'Need part 2 please!! 🔥'",
      time = "1d"
    ),
    InboxNotification(
      id = "4",
      user = "TikTok System",
      avatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=100&q=80",
      text = "Your video reached 50,000 views! Keep creating ✨",
      time = "2d"
    ),
    InboxNotification(
      id = "5",
      user = "Zack Plays",
      avatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=100&q=80",
      text = "mentioned you in a comment",
      time = "3d"
    )
  )

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
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Inbox",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Icon(
          imageVector = Icons.Default.ArrowDropDown,
          contentDescription = null,
          tint = Color.White
        )
      }

      IconButton(onClick = { /* messages */ }) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Direct Messages",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }
    }

    // New Activities banner
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      ActivityBubble(icon = Icons.Default.PersonAdd, label = "New followers", bg = TikTokCyan)
      ActivityBubble(icon = Icons.Default.Favorite, label = "Likes", bg = TikTokRed)
      ActivityBubble(icon = Icons.Default.Chat, label = "Comments", bg = Color(0xFF6C5CE7))
    }

    Spacer(modifier = Modifier.height(12.dp))
    Divider(color = Color.White.copy(alpha = 0.1f))

    // Notifications List
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(notifications, key = { it.id }) { item ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
          ) {
            AsyncImage(
              model = item.avatar,
              contentDescription = item.user,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = item.user,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = item.time,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.45f)
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.text,
              fontSize = 13.sp,
              color = Color.White.copy(alpha = 0.75f),
              maxLines = 2
            )
          }

          if (item.isFollow) {
            Button(
              onClick = { /* follow back */ },
              colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.height(30.dp)
            ) {
              Text("Follow back", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun ActivityBubble(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, bg: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(bg)
    ) {
      Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
  }
}
