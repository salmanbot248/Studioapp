package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TikTokNavTab
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed

@Composable
fun TikTokBottomNav(
  currentTab: TikTokNavTab,
  onTabSelected: (TikTokNavTab) -> Unit,
  unreadCount: Int = 3,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.Black.copy(alpha = 0.95f))
      .navigationBarsPadding()
      .padding(top = 8.dp, bottom = 4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Home
      TikTokNavItem(
        label = "Home",
        icon = if (currentTab == TikTokNavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
        isSelected = currentTab == TikTokNavTab.HOME,
        testTag = "nav_home",
        onClick = { onTabSelected(TikTokNavTab.HOME) }
      )

      // 2. Friends
      TikTokNavItem(
        label = "Friends",
        icon = if (currentTab == TikTokNavTab.FRIENDS) Icons.Filled.People else Icons.Outlined.People,
        isSelected = currentTab == TikTokNavTab.FRIENDS,
        testTag = "nav_friends",
        onClick = { onTabSelected(TikTokNavTab.FRIENDS) }
      )

      // 3. Iconic TikTok Create "+" Button (Cyan & Red Layered Button)
      TikTokCreateButton(
        onClick = { onTabSelected(TikTokNavTab.CREATE) }
      )

      // 4. Inbox
      TikTokNavItem(
        label = "Inbox",
        icon = if (currentTab == TikTokNavTab.INBOX) Icons.Filled.Mail else Icons.Outlined.Mail,
        isSelected = currentTab == TikTokNavTab.INBOX,
        badgeCount = unreadCount,
        testTag = "nav_inbox",
        onClick = { onTabSelected(TikTokNavTab.INBOX) }
      )

      // 5. Profile
      TikTokNavItem(
        label = "Profile",
        icon = if (currentTab == TikTokNavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
        isSelected = currentTab == TikTokNavTab.PROFILE,
        testTag = "nav_profile",
        onClick = { onTabSelected(TikTokNavTab.PROFILE) }
      )
    }
  }
}

@Composable
fun TikTokNavItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  testTag: String,
  badgeCount: Int = 0,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable { onClick() }
      .testTag(testTag)
      .padding(horizontal = 8.dp)
  ) {
    if (badgeCount > 0) {
      BadgedBox(
        badge = {
          Badge(
            containerColor = TikTokRed,
            contentColor = Color.White
          ) {
            Text(badgeCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
          modifier = Modifier.size(26.dp)
        )
      }
    } else {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    )
  }
}

@Composable
fun TikTokCreateButton(
  onClick: () -> Unit
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .clickable { onClick() }
      .testTag("nav_create")
      .padding(horizontal = 4.dp)
  ) {
    // Left Cyan Layer
    Box(
      modifier = Modifier
        .offset(x = (-3).dp)
        .width(38.dp)
        .height(28.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(TikTokCyan)
    )

    // Right Red Layer
    Box(
      modifier = Modifier
        .offset(x = 3.dp)
        .width(38.dp)
        .height(28.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(TikTokRed)
    )

    // Center White Container with Black Plus Icon
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .width(38.dp)
        .height(28.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Create",
        tint = Color.Black,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
