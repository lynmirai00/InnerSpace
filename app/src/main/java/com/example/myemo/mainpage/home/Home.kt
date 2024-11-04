@file:Suppress("DEPRECATION")

package com.example.myemo.mainpage.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.example.myemo.selectedBackgroundColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarView
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

data class DiaryEntry(
    val email: String? = null,
    val date: String? = null,
    val emoji: String? = null,
    val diary: String? = null
)

@SuppressLint("MutableCollectionMutableState")
@ExperimentalMaterial3Api
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    onNavigateToDashboard: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    // Biến trạng thái lưu cảm xúc và nhật ký mỗi ngày
    val dayToEmojiMap = remember { mutableStateOf(mutableMapOf<Int, String>()) }
    val diaryText = remember { mutableStateOf("") }

    // Trạng thái ngày đã chọn
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val today = remember { LocalDate.now() }

    // Biến trạng thái hiển thị hộp thoại emoji
    val showEmojiBox = remember { mutableStateOf(false) }

    // Danh sách các ngày không được chọn
    val disabledDates = (1..36500).map { today.plusDays(it.toLong()) }

    val flowerImages = listOf(
        R.drawable.flowers1,
        R.drawable.flowers2,
        R.drawable.flowers3,
        R.drawable.flowers4,
        R.drawable.flowers5,
        R.drawable.flowers6,
        R.drawable.flowers7,
        R.drawable.flowers8,
        R.drawable.flowers9,
        R.drawable.flowers10,
        R.drawable.flowers11,
        R.drawable.flowers12,
    ) // Thay các tên ảnh này bằng các tài nguyên hình ảnh thực tế của bạn

    // Chọn ngẫu nhiên một hình ảnh từ danh sách
    val randomImage = flowerImages.random()

    // Tự động lưu nhật ký khi diaryText thay đổi
    LaunchedEffect(diaryText.value) {
        selectedDate.value?.let { date ->
            saveDiaryEntry(date, dayToEmojiMap.value[date.dayOfMonth], diaryText.value)
        }
    }

    // Lấy dữ liệu từ Firebase khi chọn ngày
    LaunchedEffect(selectedDate.value) {
        selectedDate.value?.let { date ->
            val documentId = "${currentUser?.email}_${date}"
            db.collection("diaryEntries").document(documentId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val entry = document.toObject<DiaryEntry>()
                        entry?.let {
                            dayToEmojiMap.value[date.dayOfMonth] = it.emoji ?: "Not selected"
                            diaryText.value = it.diary ?: ""
                        }
                    } else {
                        dayToEmojiMap.value[date.dayOfMonth] = "Not selected"
                        diaryText.value = ""
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error getting document", e)
                }
        }
    }

    // Cấu hình Calendar
    val calendarSelection = CalendarSelection.Date(
        withButtonView = false,
        selectedDate = selectedDate.value,
        onSelectDate = { newDate ->
            selectedDate.value = newDate
            showEmojiBox.value = true
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedBackgroundColor.value)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Hi, ${currentUser?.displayName ?: "User"}\nGive you some flowers today ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = randomImage),
                        contentDescription = "Flower",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .background(Color.White, RoundedCornerShape(10.dp)),
            ) {
                CalendarView(
                    sheetState = rememberSheetState(),
                    selection = calendarSelection,
                    config = CalendarConfig(
                        yearSelection = true,
                        monthSelection = true,
                        style = CalendarStyle.MONTH,
                        disabledDates = disabledDates,
                    )
                )
            }

            EmojiBox(
                showDialog = showEmojiBox.value,
                selectedDay = selectedDate.value?.dayOfMonth ?: 0,
                onDismiss = { showEmojiBox.value = false },
                dayToEmojiMap = dayToEmojiMap.value,
                onEmojiClick = { emoji ->
                    dayToEmojiMap.value[selectedDate.value?.dayOfMonth ?: 0] = emoji
                    showEmojiBox.value = false
                    saveDiaryEntry(selectedDate.value, emoji, diaryText.value)
                }
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                val selectedDayEmoji = selectedDate.value?.let { day ->
                    dayToEmojiMap.value[day.dayOfMonth]
                }

                when (val emojiResult =
                    selectedDayEmoji?.let { getEmojiImageResourceAndColor(it) }) {
                    is Pair<*, *> -> {
                        val (iconResId, backgroundColor) = emojiResult as Pair<Int, Color>
                        EmojiItem(
                            icon = iconResId,
                            label = selectedDayEmoji,
                            onEmojiClick = { /* Không cần hành động khi xem cảm xúc đã chọn */ },
                            color = backgroundColor
                        )
                    }

                    is String -> Text(
                        text = emojiResult,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                    )
                }
                Text(
                    text = selectedDate.value?.let { "Tell me something on ${it.dayOfMonth}-${it.monthValue}-${it.year}" }
                        ?: "Please select a date",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    BasicTextField(
                        value = diaryText.value,
                        onValueChange = { newText -> diaryText.value = newText },
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier.fillMaxSize(),
                        decorationBox = { innerTextField ->
                            if (diaryText.value.isEmpty()) {
                                Text("Start writing here...", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            ActionBar(
                currentPage = "Home",
                onHomeClick = {},
                onDashboardClick = onNavigateToDashboard,
                onAccountClick = onNavigateToAccount
            )
        }
    }
}

// Hàm lưu nhật ký vào Firebase
private fun saveDiaryEntry(date: LocalDate?, emoji: String?, diaryText: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    if (currentUser == null) {
        Log.w("saveDiaryEntry", "User not logged in")
        return
    }

    date?.let {
        val documentId = "${currentUser.email}_${it}"

        val diaryEntry = mapOf(
            "email" to currentUser.email,
            "date" to it.toString(),
            "emoji" to (emoji ?: "Not selected"),
            "diary" to diaryText
        )

        db.collection("diaryEntries")
            .document(documentId)
            .set(diaryEntry)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }
}

// Hàm trả về Pair của resId và màu cho cảm xúc, hoặc chuỗi thông báo khi không có cảm xúc phù hợp
fun getEmojiImageResourceAndColor(emoji: String): Any {
    return when (emoji) {
        "Happy" -> R.drawable.happy to Color(0xFFFFFAE6)   // Vàng cho cảm xúc vui vẻ
        "Neutral" -> R.drawable.neutral to Color(0xFFEAFBFE) // Xám cho cảm xúc trung lập
        "Bored" -> R.drawable.bored to Color(0xFFEDEDED)  // Xanh nhạt cho cảm xúc chán
        "Sad" -> R.drawable.sad to Color(0xFFB8D1F1)  // Xanh đậm cho cảm xúc buồn
        "Angry" -> R.drawable.angry to Color(0xFFFFD5CD)   // Đỏ cho cảm xúc giận dữ
        else -> "Emotion not selected"                    // Chuỗi thông báo khi không có cảm xúc
    }
}
