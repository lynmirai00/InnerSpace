package com.example.myemo.mainpage.account

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myemo.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val channelId = "my_channel_id"
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo NotificationChannel nếu chưa có
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH // Đảm bảo mức độ quan trọng là cao
            )
            notificationManager.createNotificationChannel(channel)
            Log.d("ReminderReceiver", "Notification channel created")
        }

        // Tạo nội dung thông báo
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("リマインダー")
            .setContentText("今日はどんな一日でしたか？少し時間を取って、あなたの気持ちを記録してみましょう！😊")
            .setSmallIcon(R.drawable.user)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Gửi thông báo
        notificationManager.notify(1, notification)
        Log.d("ReminderReceiver", "Notification sent")
    }
}



