package com.example.myemo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val DEFAULT_COLOR = 0xFFd1e9f6.toInt()
        private const val DEFAULT_AVATAR_URI = "" // Giá trị mặc định rỗng
        private const val KEY_LANGUAGE = "selected_language"
        private const val DEFAULT_LANGUAGE = "ja" // Giá trị mặc định là tiếng Nhật
        private const val KEY_SELECTED_FLAG = "selected_flag" // Flag key for shared preferences
        private val DEFAULT_FLAG_RES_ID = R.drawable.japan // Default flag is Japan
    }

    // Lưu URI của avatar theo userId
    fun saveAvatarUri(userId: String, uri: String) {
        sharedPreferences.edit().putString("avatar_uri_$userId", uri).apply()
    }

    // Lấy URI của avatar theo userId
    fun getAvatarUri(userId: String): String {
        return sharedPreferences.getString("avatar_uri_$userId", DEFAULT_AVATAR_URI) ?: DEFAULT_AVATAR_URI
    }

    // Lưu màu nền
    fun saveBackgroundColor(color: Int) {
        sharedPreferences.edit().putInt(KEY_BACKGROUND_COLOR, color).apply()
    }

    // Lấy màu nền
    fun getBackgroundColor(): Int {
        return sharedPreferences.getInt(KEY_BACKGROUND_COLOR, DEFAULT_COLOR)
    }

    // Lưu thời gian nhắc nhở
    fun saveReminderTime(userId: String, time: String) {
        sharedPreferences.edit().putString("reminder_time_$userId", time).apply()
    }

    // Lấy thời gian nhắc nhở
    fun getReminderTime(userId: String): String {
        return sharedPreferences.getString("reminder_time_$userId", "10:00") ?: "10:00"
    }

    // Lưu ngôn ngữ đã chọn
    fun saveSelectedLanguage(language: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
    }

    // Lấy ngôn ngữ đã chọn
    fun getSelectedLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    // Save Selected Flag
    fun saveSelectedFlag(flagResId: Int) {
        sharedPreferences.edit().putInt(KEY_SELECTED_FLAG, flagResId).apply()
    }

    // Get Selected Flag
    fun getSelectedFlag(): Int {
        return sharedPreferences.getInt(KEY_SELECTED_FLAG, DEFAULT_FLAG_RES_ID) // Default is Japan flag
    }
}