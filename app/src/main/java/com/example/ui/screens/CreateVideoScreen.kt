package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay

@Composable
fun CreateVideoScreen(
  onClose: () -> Unit,
  onVideoCreated: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isRecording by remember { mutableStateOf(false) }
  var recordSeconds by remember { mutableIntStateOf(0) }
  var selectedSpeed by remember { mutableStateOf("1x") }
  var selectedMode by remember { mutableStateOf("15s") }
  var selectedSound by remember { mutableStateOf("Original Sound - Summer Nights") }

  LaunchedEffect(isRecording) {
    if (isRecording) {
      while (isRecording && recordSeconds < 15) {
        delay(1000)
        recordSeconds++
      }
      if (recordSeconds >= 15) {
        isRecording = false
        onVideoCreated()
      }
    } else {
      recordSeconds = 0
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    // Top Controls Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Close Button
        IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }

        // Add Sound Pill
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { /* select sound */ }
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Add sound", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        // Placeholder for balance
        Spacer(modifier = Modifier.width(36.dp))
      }
    }

    // Right Camera Tools Column
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .align(Alignment.TopEnd)
        .statusBarsPadding()
        .padding(top = 56.dp, end = 16.dp)
    ) {
      CameraToolItem(icon = Icons.Default.Cameraswitch, label = "Flip")
      CameraToolItem(icon = Icons.Default.Speed, label = "Speed")
      CameraToolItem(icon = Icons.Default.AutoAwesome, label = "Beauty")
      CameraToolItem(icon = Icons.Default.Timer, label = "Timer")
      CameraToolItem(icon = Icons.Default.FlashOn, label = "Flash")
    }

    // Recording seconds indicator
    if (isRecording) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.TopCenter)
          .statusBarsPadding()
          .padding(top = 60.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(TikTokRed.copy(alpha = 0.8f))
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "00:${String.format("%02d", recordSeconds)} / 00:15", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    // Bottom Controls & Recording Area
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(bottom = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Speed Selector
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(Color.Black.copy(alpha = 0.45f))
          .padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        listOf("0.3x", "0.5x", "1x", "2x", "3x").forEach { speed ->
          Text(
            text = speed,
            color = if (selectedSpeed == speed) TikTokCyan else Color.White.copy(alpha = 0.65f),
            fontWeight = if (selectedSpeed == speed) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.clickable { selectedSpeed = speed }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Recording Row: Effects | BIG RED RECORD BUTTON | Upload
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Effects
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.clickable { /* effects */ }
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF333333)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "Effects", tint = TikTokCyan, modifier = Modifier.size(24.dp))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Effects", color = Color.White, fontSize = 11.sp)
        }

        // BIG RED TIKTOK RECORD BUTTON
        val infiniteTransition = rememberInfiniteTransition(label = "rec")
        val pulseScale by infiniteTransition.animateFloat(
          initialValue = 1f,
          targetValue = 1.12f,
          animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
          ),
          label = "recPulse"
        )

        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(80.dp)
            .clickable {
              isRecording = !isRecording
              if (!isRecording && recordSeconds > 2) {
                onVideoCreated()
              }
            }
        ) {
          // Outer white ring
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .border(4.dp, Color.White, CircleShape)
          )

          // Inner Red Button (pulses if recording)
          Box(
            modifier = Modifier
              .size(if (isRecording) 36.dp else 66.dp)
              .scale(if (isRecording) pulseScale else 1f)
              .clip(if (isRecording) RoundedCornerShape(8.dp) else CircleShape)
              .background(TikTokRed)
          )
        }

        // Upload Gallery
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.clickable { onVideoCreated() }
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF333333)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Image, contentDescription = "Upload", tint = Color.White, modifier = Modifier.size(24.dp))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Upload", color = Color.White, fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Bottom Mode Switcher: Photo | 15s | 60s | 10m
      Row(
        horizontalArrangement = Arrangement.spacedBy(22.dp)
      ) {
        listOf("Photo", "15s", "60s", "10m").forEach { mode ->
          Text(
            text = mode,
            color = if (selectedMode == mode) Color.White else Color.White.copy(alpha = 0.5f),
            fontWeight = if (selectedMode == mode) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp,
            modifier = Modifier.clickable { selectedMode = mode }
          )
        }
      }
    }
  }
}

@Composable
fun CameraToolItem(icon: ImageVector, label: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable { /* tool */ }
  ) {
    Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(26.dp))
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = label, color = Color.White, fontSize = 11.sp)
  }
}
