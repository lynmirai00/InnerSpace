package com.example.myemo

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val DEFAULT_COLOR = 0xFFd1e9f6.toInt()
        private const val DEFAULT_AVATAR_URI = "" // Giá trị mặc định rỗng
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
}