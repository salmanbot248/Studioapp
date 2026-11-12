package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Comment
import com.example.model.TikTokVideo
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokDark
import com.example.ui.theme.TikTokRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
  video: TikTokVideo,
  comments: List<Comment>,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit,
  onLikeComment: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var newCommentText by remember { mutableStateOf("") }

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
        .fillMaxHeight(0.75f)
        .navigationBarsPadding()
        .imePadding()
    ) {
      // Header: Comment count & close button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Spacer(modifier = Modifier.width(32.dp))
        Text(
          text = "${video.commentsCount} comments",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Comments List
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(comments, key = { it.id }) { comment ->
          CommentItem(
            comment = comment,
            onLike = { onLikeComment(comment.id) }
          )
        }
      }

      // Quick Emoji Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(TikTokDark)
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        listOf("😂", "🥰", "🔥", "👏", "😱", "❤️", "💯").forEach { emoji ->
          Text(
            text = emoji,
            fontSize = 22.sp,
            modifier = Modifier
              .clickable { newCommentText += emoji }
              .padding(4.dp)
          )
        }
      }

      // Bottom Input Field
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(TikTokCard)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.Gray)
        ) {
          AsyncImage(
            model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80",
            contentDescription = "You",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        OutlinedTextField(
          value = newCommentText,
          onValueChange = { newCommentText = it },
          placeholder = {
            Text(
              text = "Add comment...",
              color = Color.White.copy(alpha = 0.5f),
              fontSize = 14.sp
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF333333),
            unfocusedContainerColor = Color(0xFF2A2A2A),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("comment_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = {
            if (newCommentText.isNotBlank()) {
              onAddComment(newCommentText)
              newCommentText = ""
            }
          },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (newCommentText.isNotBlank()) TikTokRed else Color.White.copy(alpha = 0.15f))
            .testTag("send_comment_button")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = if (newCommentText.isNotBlank()) Color.White else Color.White.copy(alpha = 0.4f),
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
fun CommentItem(
  comment: Comment,
  onLike: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top
  ) {
    // Commenter avatar
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
    ) {
      AsyncImage(
        model = comment.avatar,
        contentDescription = comment.userName,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Comment details
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = comment.userName,
        color = Color.White.copy(alpha = 0.7f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = comment.text,
        color = Color.White,
        fontSize = 14.sp,
        lineHeight = 18.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = comment.timeAgo,
          color = Color.White.copy(alpha = 0.45f),
          fontSize = 11.sp
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
          text = "Reply",
          color = Color.White.copy(alpha = 0.6f),
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    // Like Comment Button
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .clickable { onLike() }
        .padding(start = 8.dp)
    ) {
      Icon(
        imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = "Like Comment",
        tint = if (comment.isLiked) TikTokRed else Color.White.copy(alpha = 0.6f),
        modifier = Modifier.size(18.dp)
      )
      if (comment.likes > 0) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = comment.likes.toString(),
          color = Color.White.copy(alpha = 0.6f),
          fontSize = 10.sp
        )
      }
    }
  }
}
