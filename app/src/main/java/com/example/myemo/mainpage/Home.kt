package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myemo.R

@Composable
fun Home(
    email: String?,
    onNavigateToDashboard: () -> Unit,
    onNavigateToAccount: () -> Unit
) {

    val userName = "Yen Nhi" // Cậu có thể thay bằng dữ liệu thực tế

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
            )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Cho phép cuộn trang nếu nội dung nhiều
        ) {
            // Phần chào
            GreetingSection(userName)

            Spacer(modifier = Modifier.height(20.dp))

            // Phần lịch
            CalendarSection()

            Spacer(modifier = Modifier.height(20.dp))

            // Ô ghi nhật ký
            DiaryEntrySection()

            Spacer(modifier = Modifier.weight(1f)) // Đẩy ActionBar xuống đáy

            // Thanh điều hướng (ActionBar)
            ActionBar(
                onHomeClick = {}, // Đây là trang Home, nên không cần xử lý
                onDashboardClick = onNavigateToDashboard,
                onAccountClick = onNavigateToAccount
            )
        }
    }
}

@Composable
fun GreetingSection(userName: String) {
    Text(
        text = "Hello, $userName! How are you feeling today?",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Blue
    )
}

@Composable
fun CalendarSection() {
    // Đây là placeholder, cậu có thể thay bằng lịch thực tế
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Calendar will be here", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DiaryEntrySection() {
    Column {
        Text(
            text = "Write your diary for today",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Ô ghi nhật ký
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Text("Start writing here...", color = Color.Gray) // Placeholder cho ô ghi
        }
    }
}
