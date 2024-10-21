package com.example.myemo.mainpage.account

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun ReminderNotification(
    context: Context,
    reminderTime: String
) {
    // Trạng thái của Switch
    var isChecked by remember { mutableStateOf(false) }
    // Lấy trạng thái đã lưu từ SharedPreferences khi mở màn hình
    LaunchedEffect(Unit) {
        isChecked = getReminderState(context)
    }

    // Nút thông báo nhắc nhở
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp)),
    ) {
        // Text bên trái
        Text(
            text = "Reminder Notifications",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
        // Switch bên phải
        Switch(
            checked = isChecked,
            onCheckedChange = { checked ->
                isChecked = checked // Cập nhật trạng thái của Switch
                saveReminderState(context, checked) // Lưu trạng thái
                if (checked) {
                    scheduleReminder(context, reminderTime)
                } else {
                    cancelReminder(context)
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green, // Màu xanh khi bật
                uncheckedThumbColor = Color.Gray // Màu xám khi tắt
            ),
            modifier = Modifier.padding(end = 20.dp)
        )
    }
}

@SuppressLint("MissingPermission")
fun scheduleReminder(context: Context, reminderTime: String) {
    // Chuyển đổi chuỗi thời gian thành Calendar
    val timeParts = reminderTime.split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].toInt()

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    Log.d("ReminderNotification", "Scheduled reminder at $hour:$minute")

    // Tạo Intent và PendingIntent để kích hoạt BroadcastReceiver
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Sử dụng AlarmManager để lên lịch
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
    Log.d("ReminderNotification", "Alarm set for: ${calendar.time}")
}

fun cancelReminder(context: Context) {
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)

    Log.d("ReminderNotification", "Reminder cancelled")
}

// Thêm hàm để lưu trạng thái nhắc nhở
fun saveReminderState(context: Context, isChecked: Boolean) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("reminder_state", isChecked)
        apply()
    }
}

// Hàm để lấy trạng thái nhắc nhở
fun getReminderState(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("reminder_state", false) // Mặc định là false
}

// Khởi động nhắc nhở khi ứng dụng khởi động
fun setupReminder(context: Context, reminderTime: String) {
    val isChecked = getReminderState(context)
    if (isChecked) {
        scheduleReminder(context, reminderTime)
    }
}

