package com.example.myemo.mainpage.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.unit.DpOffset
import com.example.myemo.selectedBackgroundColor

@Composable
fun ChangeBackgroundColorDialog() {
    var expanded by remember { mutableStateOf(false) }

    // Nút hiển thị "Change Background Color"
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = { expanded = true }) // Hiển thị dropdown khi nhấn
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Change Background Color",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
        // DropdownMenu hiển thị danh sách màu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .background(Color.White),
                offset = DpOffset(0.dp, 0.dp), // Điều chỉnh khoảng cách của menu nếu cần
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
            ) {
                val colors = listOf(
                    Color(0xFFD1E9F6), // Màu gốc
                    Color(0xFFF6EACB), // Màu tím nhạt hơn
                    Color(0xFFF1D3CE), // Màu tím rất nhạt
                    Color(0xFFE1ECC8), // Màu vàng nhạt
                    Color(0xFFEECAD5)  // Màu xanh lá nhạt
                )
                colors.forEach { color ->
                    DropdownMenuItem(
                        text = { Text(" ") },
                        modifier = Modifier.background(color),
                        onClick = {
                            selectedBackgroundColor.value = color // Cập nhật màu nền
                            expanded = false
                        }
                    )
                }
            }
    }
}

