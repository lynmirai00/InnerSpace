package com.example.myemo.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            .background(Color(0xFFECECEC))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Nhật ký (Diary)
        IconButton(onClick = onDashboardClick) {
            Icon(
                painter = painterResource(id = R.drawable.icon_google), // Thay bằng icon nhật ký
                contentDescription = "Diary",
                tint = Color.Blue
            )
        }

        // Nút Home
        IconButton(onClick = onHomeClick) {
            Icon(
                painter = painterResource(id = R.drawable.icon_github), // Thay bằng icon home của cậu
                contentDescription = "Home",
                tint = Color.Blue
            )
        }

        // Nút Cài đặt tài khoản
        IconButton(onClick = onAccountClick) {
            Icon(
                painter = painterResource(id = R.drawable.icon_instagram), // Thay bằng icon account
                contentDescription = "Account Settings",
                tint = Color.Blue
            )
        }
    }
}