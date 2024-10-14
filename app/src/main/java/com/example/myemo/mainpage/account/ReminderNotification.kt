package com.example.myemo.mainpage.account

import ReminderReceiver
import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

@Composable
fun ReminderNotification(
    context: Context,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    reminderTime: String
) {
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
            modifier = Modifier.padding(start = 10.dp)
        )
        // Switch bên phải
        Switch(
            checked = isChecked,
            onCheckedChange = { checked ->
                onCheckedChange(checked)
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
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}

private fun scheduleReminder(context: Context, reminderTime: String) {
    // Chuyển đổi chuỗi thời gian thành giờ và phút
    val (hour, minute) = reminderTime.split(":").map { it.toInt() }

    // Thiết lập AlarmManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java)

    // Sử dụng FLAG_IMMUTABLE khi tạo PendingIntent
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Tính thời gian để đặt nhắc nhở
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)

    // Thiết lập nhắc nhở
    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY, // Lặp lại hàng ngày
        pendingIntent
    )
}


private fun cancelReminder(context: Context) {
    // Tạo Intent cho ReminderReceiver
    val intent = Intent(context, ReminderReceiver::class.java)

    // Thêm FLAG_IMMUTABLE vào PendingIntent
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Hủy nhắc nhở
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}

