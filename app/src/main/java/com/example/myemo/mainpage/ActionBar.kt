package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myemo.R

@Composable
fun ActionBar(
    currentPage: String, // Add this parameter to indicate the current page
    onHomeClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onAccountClick: () -> Unit,
    backgroundColor: Color // Add this parameter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                Color.White,
                RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .border(
                1.dp,
                Color.White,
                RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            ), // Bo góc trên
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Dashboard
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable(onClick = onDashboardClick),
            contentAlignment = Alignment.Center // Đặt icon ở giữa Box
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bar_chart), // Thay bằng icon nhật ký
                contentDescription = "Dashboard",
                tint = if (currentPage == "Dashboard") backgroundColor else Color.Black // Change color if active
            )
        }
        Spacer(Modifier.width(20.dp))

        // Nút Home {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable(onClick = onHomeClick),
            contentAlignment = Alignment.Center // Đặt icon ở giữa Box
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home), // Thay bằng icon home của cậu
                contentDescription = "Home",
                tint = if (currentPage == "Home") backgroundColor else Color.Black
            )
        }
        Spacer(Modifier.width(20.dp))

        // Nút Cài đặt tài khoản
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable(onClick = onAccountClick),
            contentAlignment = Alignment.Center // Đặt icon ở giữa Box
        ) {
            Icon(
                painter = painterResource(id = R.drawable.user), // Thay bằng icon account
                contentDescription = "Account",
                tint = if (currentPage == "Account") backgroundColor else Color.Black // Change color if active
            )
        }
    }
}