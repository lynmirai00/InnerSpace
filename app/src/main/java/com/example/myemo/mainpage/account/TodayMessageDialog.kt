package com.example.myemo.mainpage.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodayMessageDialog() {
    var showDialog by remember { mutableStateOf(false) }

    // Danh sách câu động viên
    val quotes = listOf(
        "Believe in yourself!",
        "You are stronger than you think.",
        "Every day is a fresh start.",
        "Keep going, you’re doing great!",
        "Dream big, work hard!"
    )

    // Chọn câu ngẫu nhiên
    val randomQuote = quotes.random()

    // Nút hiển thị "Today Message"
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = { showDialog = true }) // Hiển thị hộp thoại khi nhấn
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Today Message",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }

    // Hộp thoại hiển thị câu động viên
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // Đóng hộp thoại khi nhấn ngoài
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                    horizontalArrangement = Arrangement.SpaceBetween // Căn chỉnh đều các phần tử
                ) {
                    Text(
                        text = randomQuote,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            },
            confirmButton = { TextButton(onClick = { }) {} }
        )
    }
}
