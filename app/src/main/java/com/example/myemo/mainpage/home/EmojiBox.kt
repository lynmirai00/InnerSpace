package com.example.myemo.mainpage.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.myemo.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmojiBox(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onEmojiClick: (String) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = stringResource(R.string.question),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Khi người dùng nhấn vào một emoji, cập nhật cảm xúc cho ngày
                    EmojiItem(icon = R.drawable.happy, label = "Happy", onEmojiClick = {
                        onEmojiClick("Happy")
                    }, Color(0xFFFFFAE6))
                    EmojiItem(icon = R.drawable.neutral, label = "Neutral", onEmojiClick = {
                        onEmojiClick("Neutral")
                    }, Color(0xFFEAFBFE))
                    EmojiItem(icon = R.drawable.bored, label = "Bored", onEmojiClick = {
                        onEmojiClick("Bored")
                    }, Color(0xFFEDEDED))
                    EmojiItem(icon = R.drawable.sad, label = "Sad", onEmojiClick = {
                        onEmojiClick("Sad")
                    }, Color(0xFFB8D1F1))
                    EmojiItem(icon = R.drawable.angry, label = "Angry", onEmojiClick = {
                        onEmojiClick("Angry")
                    }, Color(0xFFFFD5CD))
                }
            },
            confirmButton = {}, // Để trống nếu không cần
            containerColor = Color.White // Đổi màu nền thành trắng
        )
    }
}

@Composable
fun EmojiItem(icon: Int, label: String, onEmojiClick: (String) -> Unit, color: Color) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .padding(5.dp)
            .background(color, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
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
