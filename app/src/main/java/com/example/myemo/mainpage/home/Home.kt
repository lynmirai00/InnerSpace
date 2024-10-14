package com.example.myemo.mainpage.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myemo.mainpage.ActionBar

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
                        Color(0xFFd1e9f6), // Màu đầu tiên
                        Color(0xFFe0edfa), // Màu thứ hai
                        Color(0xFFedf3fc), // Màu thứ ba
                        Color(0xFFf7f8fd), // Màu thứ tư
                        Color(0xFFffffff)  // Màu cuối
                    ),
                    start = Offset(0f, 1000f),  // Bắt đầu từ dưới lên
                    end = Offset(0f, 0f)  // Kết thúc ở trên
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Cho phép cuộn trang nếu nội dung nhiều
                .padding(bottom = 80.dp) // Đặt khoảng trống cho ActionBar
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
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Cố định ActionBar ở dưới
        ) {
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
                .background(Color.White, RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Text("Start writing here...", color = Color.Gray) // Placeholder cho ô ghi
        }
    }
}
