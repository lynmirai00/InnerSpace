package com.example.myemo.mainpage.dashboard

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

@SuppressLint("AutoboxingStateCreation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dashboard(
    onNavigateToHome: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val month by remember {
        mutableStateOf(
            Calendar.getInstance().get(Calendar.MONTH) + 1
        )
    } // +1 vì tháng bắt đầu từ 0
    val year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val goodjobImages = listOf(
        R.drawable.goodjob1,
        R.drawable.goodjob2,
        R.drawable.goodjob3,
        R.drawable.goodjob4,
        R.drawable.goodjob5,
        R.drawable.goodjob6,
        R.drawable.goodjob7,
    ) // Thay các tên ảnh này bằng các tài nguyên hình ảnh thực tế của bạn

    // Chọn ngẫu nhiên một hình ảnh từ danh sách
    val randomImage = goodjobImages.random()
    val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val monthName = months[month - 1] // month - 1 vì chỉ số mảng bắt đầu từ 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd1e9f6)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "Good job, ${currentUser?.displayName ?: "User"}\nThis is your progress",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = randomImage),
                        contentDescription = "Flower",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                // Chon thang va nam
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                            .border(
                                1.dp,
                                Color(0xFFd1e9f6),
                                RoundedCornerShape(10.dp)
                            ) // Đảm bảo hình dạng vuông
                            .background(Color.White) // Màu nền khi không nhấn
                            .clickable(onClick = { /* */ }), // Xử lý sự kiện nhấn
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$year",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                            .border(
                                1.dp,
                                Color(0xFFd1e9f6),
                                RoundedCornerShape(10.dp)
                            ) // Đảm bảo hình dạng vuông
                            .background(Color.White) // Màu nền khi không nhấn
                            .clickable(onClick = { /* */ }), // Xử lý sự kiện nhấn
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = monthName,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Hiển thị biểu đồ cột
                EmotionBarChart(
                    currentUser?.email.toString(),
                    year,
                    month,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            ActionBar(
                onHomeClick = onNavigateToHome,
                onDashboardClick = {},
                onAccountClick = onNavigateToAccount
            )
        }
    }
}
