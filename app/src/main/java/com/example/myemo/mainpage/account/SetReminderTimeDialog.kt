package com.example.myemo.mainpage.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import java.util.Calendar

@SuppressLint("DefaultLocale", "AutoboxingStateCreation")
@Composable
fun SetReminderTimeDialog(
    context: Context, // Thêm tham số context
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSet: (String) -> Unit
) {
    if (showDialog) {
        // Biến để lưu thời gian đã chọn
        var hour by remember { mutableStateOf(0) }
        var minute by remember { mutableStateOf(0) }

        // Lấy giờ nhắc nhở từ SharedPreferences khi mở hộp thoại
        LaunchedEffect(Unit) {
            val savedTime = getReminderTime(context) // Lấy giá trị đã lưu
            savedTime?.let {
                val timeParts = it.split(":")
                hour = timeParts[0].toInt()
                minute = timeParts[1].toInt()
            }
        }

        // Hiển thị hộp thoại
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Set Reminder Time", fontSize = 20.sp)
            },
            text = {
                Column {
                    TimePicker(onTimeSelected = { h, m ->
                        hour = h
                        minute = m
                    })
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Chuyển đổi giờ và phút thành chuỗi
                        val time = String.format("%02d:%02d", hour, minute)
                        onTimeSet(time) // Gọi hàm để truyền thời gian đã chọn
                        saveReminderTime(context, time) // Lưu giá trị vào SharedPreferences
                        onDismiss() // Đóng hộp thoại
                    }
                ) {
                    Text("Set")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun TimePicker(onTimeSelected: (Int, Int) -> Unit) {
    // Sử dụng Calendar để lấy giờ và phút hiện tại
    val calendar = Calendar.getInstance()
    var hour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    Column {
        // Hiển thị Slider cho giờ
        Text("Hour: $hour")
        Slider(
            value = hour.toFloat(),
            onValueChange = { newHour ->
                hour = newHour.toInt()
                onTimeSelected(hour, minute) // Gọi hàm khi giờ được chọn
            },
            valueRange = 0f..23f, // Giới hạn giá trị từ 0 đến 23
            steps = 22 // Cung cấp 23 bậc (0 đến 23)
        )

        // Hiển thị Slider cho phút
        Text("Minute: $minute")
        Slider(
            value = minute.toFloat(),
            onValueChange = { newMinute ->
                minute = newMinute.toInt()
                onTimeSelected(hour, minute) // Gọi hàm khi phút được chọn
            },
            valueRange = 0f..59f, // Giới hạn giá trị từ 0 đến 59
            steps = 58 // Cung cấp 59 bậc (0 đến 59)
        )
    }
}


fun saveReminderTime(context: Context, time: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("reminder_time", time)
    editor.apply()
}

fun getReminderTime(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("reminder_time", "10:00") // Giá trị mặc định nếu không có
}
