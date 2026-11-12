package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.TikTokVideo
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
  video: TikTokVideo,
  onDismiss: () -> Unit,
  onActionClick: (String) -> Unit
) {
  val context = LocalContext.current
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = TikTokDark,
    contentColor = Color.White,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 10.dp)
          .size(width = 40.dp, height = 4.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(Color.White.copy(alpha = 0.3f))
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(bottom = 16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Spacer(modifier = Modifier.width(32.dp))
        Text(
          text = "Send to",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Quick Friends Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        listOf(
          "Sarah" to "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&q=80",
          "David" to "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&q=80",
          "Elena" to "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80",
          "Zack" to "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=100&q=80",
          "Chloe" to "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=100&q=80"
        ).forEach { (name, avatar) ->
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
              onActionClick("Sent to $name! 🚀")
              onDismiss()
            }
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
            ) {
              AsyncImage(
                model = avatar,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
          }
        }
      }

      Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))

      // Share Action Buttons Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        ShareActionItem(
          icon = Icons.Default.Link,
          label = "Copy Link",
          bg = Color(0xFF333333),
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("TikTok Video", video.videoUrl)
            clipboard.setPrimaryClip(clip)
            onActionClick("Link copied to clipboard! 📋")
            onDismiss()
          }
        )

        ShareActionItem(
          icon = Icons.Default.Download,
          label = "Save Video",
          bg = TikTokCyan.copy(alpha = 0.25f),
          onClick = {
            onActionClick("Video download started! 📥")
            onDismiss()
          }
        )

        ShareActionItem(
          icon = Icons.Default.Repeat,
          label = "Repost",
          bg = TikTokRed.copy(alpha = 0.25f),
          onClick = {
            onActionClick("Video reposted to your followers! 🔁")
            onDismiss()
          }
        )

        ShareActionItem(
          icon = Icons.Default.Bookmark,
          label = "Favorite",
          bg = Color(0xFF333333),
          onClick = {
            onActionClick("Added to Favorites ⭐")
            onDismiss()
          }
        )

        ShareActionItem(
          icon = Icons.Default.VisibilityOff,
          label = "Not Interested",
          bg = Color(0xFF333333),
          onClick = {
            onActionClick("We will show fewer videos like this")
            onDismiss()
          }
        )

        ShareActionItem(
          icon = Icons.Default.Report,
          label = "Report",
          bg = Color(0xFF333333),
          onClick = {
            onActionClick("Report submitted. Thank you for keeping TikTok safe.")
            onDismiss()
          }
        )
      }
    }
  }
}

@Composable
fun ShareActionItem(
  icon: ImageVector,
  label: String,
  bg: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable { onClick() }
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(52.dp)
        .clip(CircleShape)
        .background(bg)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      color = Color.White.copy(alpha = 0.85f),
      maxLines = 1
    )
  }
}
