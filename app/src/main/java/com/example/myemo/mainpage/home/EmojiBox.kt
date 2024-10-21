package com.example.myemo.mainpage.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.myemo.R

@Composable
fun EmojiBox(
    showDialog: Boolean, // Truyền trạng thái hiển thị từ bên ngoài
    onDismiss: () -> Unit, // Truyền callback khi hộp thoại bị tắt
    onEmojiClick: (String) -> Unit // Hàm callback khi nhấn vào một emoji
) {
    if (showDialog) {
        // Hộp thoại chỉ mở khi showDialog là true
        AlertDialog(
            onDismissRequest = { onDismiss() }, // Đóng hộp thoại khi nhấn ra ngoài
            title = {
                Text(
                    "How are you feeling today?",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            text = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Các biểu tượng cảm xúc
                    EmojiItem(icon = R.drawable.happy, label = "Happy", onEmojiClick)
                    EmojiItem(icon = R.drawable.neutral, label = "Neutral", onEmojiClick)
                    EmojiItem(icon = R.drawable.bored, label = "Bored", onEmojiClick)
                    EmojiItem(icon = R.drawable.sad, label = "Sad", onEmojiClick)
                    EmojiItem(icon = R.drawable.angry, label = "Angry", onEmojiClick)
                }
            },
            confirmButton = {} // Không cần nút xác nhận
        )
    }
}

@Composable
fun EmojiItem(icon: Int, label: String, onEmojiClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(CircleShape)
            .clickable(onClick = { onEmojiClick(label) }),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )
    }
}


