package com.example.myemo.mainpage.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myemo.components.HeaderText
import com.example.myemo.mainpage.ActionBar

@Composable
fun Dashboard(
    onNavigateToHome: (String?) -> Unit,
    onNavigateToAccount: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFd1e9f6), // Màu đầu tiên
                        Color(0xFFe0edfa), // Màu thứ hai
                        Color(0xFFedf3fc), // Màu thứ ba
                        Color(0xFFf7f8fd), // Màu thứ tư
                        Color(0xFFffffff)  // Màu cuối
                    ),
                    start = Offset(0f, 1000f),  // Bắt đầu từ dưới lên
                    end = Offset(0f, 0f)  // Kết thúc ở trên
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Cho phép cuộn trang nếu nội dung nhiều
                .padding(bottom = 80.dp) // Đặt khoảng trống cho ActionBar
        ) {
            HeaderText(
                text = "Dashboard",
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Đẩy ActionBar xuống đáy
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Cố định ActionBar ở dưới
        ) {
            ActionBar(
                onHomeClick = { onNavigateToHome("aa") },
                onDashboardClick = {},
                onAccountClick = onNavigateToAccount
            )
        }
    }
}