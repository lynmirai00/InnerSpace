package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myemo.components.HeaderText

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
                        Color(0xFFEDFCC4), // Màu đầu tiên
                        Color(0xFFF2FDD0), // Màu thứ hai
                        Color(0xFFF6FDDC), // Màu thứ ba
                        Color(0xFFFAFEE9), // Màu thứ tư
                        Color(0xFFFDFFF5)  // Màu cuối
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
        ) {
            HeaderText(
                text = "Dashboard",
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f)) // Đẩy ActionBar xuống đáy
            // Thanh điều hướng (ActionBar)
            ActionBar(
                onHomeClick = { onNavigateToHome("aa") },
                onDashboardClick = {},
                onAccountClick = onNavigateToAccount
            )
        }
    }
}