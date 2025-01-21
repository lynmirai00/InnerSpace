package com.example.myemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myemo.ui.theme.MyEmoTheme
import java.util.Locale

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySavedLanguage()
        setContent {
            MyEmoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MyNavigation(navController)
                }
            }
        }

        // Gọi hàm tạo Notification Channel nếu cần
        Log.d("MainActivity", "onCreate called")
        createNotificationChannel(this)
    }

    private fun applySavedLanguage() {
        val preferenceManager = PreferenceManager(this)
        val selectedLanguage = preferenceManager.getSelectedLanguage()

        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Notification"
            val descriptionText = "Notification for reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("REMINDER_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("Main", "Notification channel created")
        } else {
            Log.d("ReminderReceiver", "No need to create notification channel for this Android version")
        }
    }
}


