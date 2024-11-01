package com.example.myemo.mainpage.dashboard

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.example.myemo.selectedBackgroundColor
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
    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    // Trạng thái để hiển thị menu
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var monthMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedBackgroundColor.value),
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

                Image(
                    painter = painterResource(id = R.drawable.goodjob1), // chọn ảnh từ goodjobImages
                    contentDescription = "Flower",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                // Chọn tháng và năm
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    // Chọn năm
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, selectedBackgroundColor.value, RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .clickable { yearMenuExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$selectedYear",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 15.sp
                        )

                        DropdownMenu(
                            expanded = yearMenuExpanded,
                            onDismissRequest = { yearMenuExpanded = false },
                            modifier = Modifier
                                .height(250.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, selectedBackgroundColor.value, RoundedCornerShape(10.dp))
                                .background(Color.White),
                            offset = DpOffset(0.dp, 0.dp), // Điều chỉnh khoảng cách của menu nếu cần
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                        ) {
                            // Hiển thị các năm từ 2020 đến 2030
                            (2020..2030).forEach { year ->
                                DropdownMenuItem(
                                    text = { Text("$year") },
                                    onClick = {
                                        selectedYear = year
                                        yearMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Chọn tháng
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, selectedBackgroundColor.value, RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .clickable { monthMenuExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = months[selectedMonth - 1],
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 15.sp
                        )

                        DropdownMenu(
                            expanded = monthMenuExpanded,
                            onDismissRequest = { monthMenuExpanded = false },
                            modifier = Modifier
                                .height(250.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, selectedBackgroundColor.value, RoundedCornerShape(10.dp))
                                .background(Color.White),
                            offset = DpOffset(0.dp, 0.dp), // Điều chỉnh khoảng cách của menu nếu cần
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                        ) {
                            // Hiển thị các tháng
                            months.forEachIndexed { index, month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
                                    onClick = {
                                        selectedMonth = index + 1
                                        monthMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Log.d("Dashboard", "Selected Year: $selectedYear, Selected Month: $selectedMonth")

                // Hiển thị biểu đồ cột
                EmotionBarChart(
                    currentUser?.email.toString(),
                    selectedYear,
                    selectedMonth,
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
