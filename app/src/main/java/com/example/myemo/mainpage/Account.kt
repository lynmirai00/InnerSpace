package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Account(
    onNavigateToDashboard: () -> Unit,
    onNavigateToHome: (String?) -> Unit,
    onLogout: () -> Unit,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
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
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Cho phép cuộn trang nếu nội dung nhiều
        ) {
            Column(
                verticalArrangement = Arrangement.Center, // Căn giữa nội dung theo chiều dọc
                horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa nội dung theo chiều ngang
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp)) // Khoảng cách giữa các phần
                // Hình đại diện
                Box(
                    modifier = Modifier
                        .size(150.dp) // Kích thước hình đại diện
                        .background(Color.Gray, shape = RoundedCornerShape(75.dp)) // Hình tròn
                        .padding(16.dp) // Thêm padding bên trong
                )
                Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa các phần
                Text(
                    text = currentUser?.displayName ?: "Inner Space",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) // Tên người dùng
                Text(
                    text = currentUser?.email ?: "innerspace@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) // Địa chỉ email

                Spacer(modifier = Modifier.height(100.dp)) // Khoảng cách giữa các phần

                // Nút thông báo nhắc nhở
                Button(
                    onClick = { /* Xử lý nhắc nhở */ },
                    modifier = Modifier
                        .height(50.dp)
                        .width(250.dp)
                        .clip(RoundedCornerShape(20.dp)) // Bo góc trước khi thiết lập viền
                        .border(
                            1.dp,
                            Color.Black,
                            RoundedCornerShape(20.dp)
                        ), // Viền màu đen với bo góc 20dp
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // Nền trắng của button
                    ),
                ) {
                    Text("Set Reminder Notification",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(200.dp)) // Khoảng cách giữa các phần

                // Nút đăng xuất
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .height(50.dp)
                        .width(250.dp)
                        .clip(RoundedCornerShape(20.dp)) // Bo góc trước khi thiết lập viền
                        .border(
                            1.dp,
                            Color.Black,
                            RoundedCornerShape(20.dp)
                        ), // Viền màu đen với bo góc 20dp
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // Nền trắng của button
                    ),
                ) {
                    Text("Log Out",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Đẩy ActionBar xuống đáy

            // ActionBar
            ActionBar(
                onHomeClick = { onNavigateToHome("aa") },
                onDashboardClick = onNavigateToDashboard,
                onAccountClick = {}
            )
        }
    }
}
