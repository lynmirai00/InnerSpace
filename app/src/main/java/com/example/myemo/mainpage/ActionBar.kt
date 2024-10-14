package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myemo.R

@Composable
fun ActionBar(
    onHomeClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
            .border(1.dp, Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 0.dp, bottomEnd = 0.dp)) // Bo góc trên
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Dashboard
        IconButton(
            onClick = onDashboardClick,
            modifier = Modifier
                .size(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bar_chart), // Thay bằng icon nhật ký
                contentDescription = "Dashboard",
                tint = Color.Black
            )
        }
        Spacer(Modifier.width(20.dp))

        // Nút Home {
        IconButton(
            onClick = onHomeClick,
            modifier = Modifier
                .size(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home), // Thay bằng icon home của cậu
                contentDescription = "Home",
                tint = Color.Black
            )
        }
        Spacer(Modifier.width(20.dp))

        // Nút Cài đặt tài khoản
        IconButton(
            onClick = onAccountClick,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.user), // Thay bằng icon account
                contentDescription = "Account",
                tint = Color.Black
            )
        }
    }
}