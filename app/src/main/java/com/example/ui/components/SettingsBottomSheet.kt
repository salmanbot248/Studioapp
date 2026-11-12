package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
  currentFeedUrl: String,
  isAutoNext: Boolean,
  onAutoNextToggle: () -> Unit,
  onUpdateFeedUrl: (String) -> Unit,
  onRefreshFeed: () -> Unit,
  onResetFeed: () -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var inputUrl by remember { mutableStateOf(currentFeedUrl) }

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
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      // Title
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Feed & Player Settings",
          fontSize = 18.sp,
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

      Spacer(modifier = Modifier.height(16.dp))

      // Custom Feed URL Input
      Text(
        text = "Custom Feed JSON URL:",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White.copy(alpha = 0.85f)
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = inputUrl,
        onValueChange = { inputUrl = it },
        placeholder = { Text("https://example.com/feed.json", color = Color.Gray, fontSize = 12.sp) },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = TikTokCard,
          unfocusedContainerColor = TikTokCard,
          focusedBorderColor = TikTokCyan,
          unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = {
            onUpdateFeedUrl(inputUrl)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Save & Load", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = {
            inputUrl = "https://salmanbot248.github.io/jazz-24-7-bot-tg/feed.json"
            onResetFeed()
            onDismiss()
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Default Feed", fontSize = 13.sp)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Auto-Next Toggle Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(TikTokCard)
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Auto-Play Next Video",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
          Text(
            text = if (isAutoNext) "Scrolls to next video automatically" else "Current video loops continuously",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f)
          )
        }
        Switch(
          checked = isAutoNext,
          onCheckedChange = { onAutoNextToggle() },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = TikTokRed
          )
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // APK & AAB Ready banner
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(TikTokCyan.copy(alpha = 0.15f))
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Download,
          contentDescription = "APK Ready",
          tint = TikTokCyan,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Direct Download APK & AAB",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Generated files are placed in /src folder for direct 1-click download",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.75f)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
