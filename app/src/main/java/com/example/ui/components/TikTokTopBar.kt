package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeedTab
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed

@Composable
fun TikTokTopBar(
  currentTab: FeedTab,
  onTabSelected: (FeedTab) -> Unit,
  isMuted: Boolean,
  onToggleMute: () -> Unit,
  onSearchClick: () -> Unit,
  onRefreshClick: () -> Unit,
  onSettingsClick: () -> Unit,
  selectedCategory: String,
  onCategorySelected: (String) -> Unit,
  categories: List<String> = listOf("All", "Trending", "Music", "Dance", "Funny", "Travel", "Cars"),
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color.Black.copy(alpha = 0.75f),
            Color.Black.copy(alpha = 0.35f),
            Color.Transparent
          )
        )
      )
      .statusBarsPadding()
      .padding(top = 8.dp)
  ) {
    // Top Row: LIVE | Tabs | Actions
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: LIVE button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(14.dp))
          .background(Color.White.copy(alpha = 0.16f))
          .clickable { onTabSelected(FeedTab.LIVE) }
          .padding(horizontal = 9.dp, vertical = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Tv,
          contentDescription = "LIVE",
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "LIVE",
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Center: Following | For You Tabs
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
      ) {
        // Following Tab
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onTabSelected(FeedTab.FOLLOWING) }
            .testTag("following_tab")
        ) {
          Text(
            text = "Following",
            color = if (currentTab == FeedTab.FOLLOWING) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 17.sp,
            fontWeight = if (currentTab == FeedTab.FOLLOWING) FontWeight.Bold else FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Box(
            modifier = Modifier
              .width(28.dp)
              .height(2.5.dp)
              .background(if (currentTab == FeedTab.FOLLOWING) Color.White else Color.Transparent)
          )
        }

        // For You Tab
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onTabSelected(FeedTab.FOR_YOU) }
            .testTag("for_you_tab")
        ) {
          Text(
            text = "For You",
            color = if (currentTab == FeedTab.FOR_YOU) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 17.sp,
            fontWeight = if (currentTab == FeedTab.FOR_YOU) FontWeight.Bold else FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Box(
            modifier = Modifier
              .width(28.dp)
              .height(2.5.dp)
              .background(if (currentTab == FeedTab.FOR_YOU) Color.White else Color.Transparent)
          )
        }
      }

      // Right Action Icons (Sound, Search, Settings)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Mute / Unmute Button
        IconButton(
          onClick = onToggleMute,
          modifier = Modifier.size(34.dp).testTag("mute_button")
        ) {
          Icon(
            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
            contentDescription = "Mute Toggle",
            tint = if (isMuted) TikTokRed else Color.White,
            modifier = Modifier.size(22.dp)
          )
        }

        // Search Button
        IconButton(
          onClick = onSearchClick,
          modifier = Modifier.size(34.dp).testTag("search_button")
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }

        // Settings / Feed Switcher
        IconButton(
          onClick = onSettingsClick,
          modifier = Modifier.size(34.dp).testTag("settings_button")
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp, bottom = 4.dp)
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 14.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { category ->
        val isSelected = selectedCategory == category
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
              if (isSelected) Color.White else Color.White.copy(alpha = 0.16f)
            )
            .clickable { onCategorySelected(category) }
            .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
          Text(
            text = category,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }
  }
}
