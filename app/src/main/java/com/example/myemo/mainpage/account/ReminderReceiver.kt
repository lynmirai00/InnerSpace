import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Gửi thông báo
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo Notification Channel nếu thiết bị là Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo nội dung thông báo
        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Emotion Reminder")
            .setContentText("It's time to log your emotions.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Hiển thị thông báo
        notificationManager.notify(1, notification)
    }
}
