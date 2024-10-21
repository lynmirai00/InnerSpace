package com.example.myemo.mainpage.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.mainpage.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarView
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

@ExperimentalMaterial3Api
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    onNavigateToDashboard: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    // Lấy thông tin người dùng hiện tại
    val selectedDate =
        remember { mutableStateOf<LocalDate?>(LocalDate.now()) } // Khởi tạo với ngày hôm nay
    val currentUser = FirebaseAuth.getInstance().currentUser
    val today = remember { LocalDate.now() }

    // Danh sách các ngày không được chọn (nếu cần)
    val disabledDates = (1..36500).map { today.plusDays(it.toLong()) }
    val showEmojiBox = remember { mutableStateOf(false) } // Biến để điều khiển hiển thị EmojiBox
    val calendarSelection = CalendarSelection.Date(
        withButtonView = false, // Ẩn các button
        selectedDate = selectedDate.value, // Gán ngày đã chọn
        onSelectDate = { newDate ->
            // Xử lý khi người dùng chọn ngày
            selectedDate.value = newDate
            // Ví dụ: in ra ngày đã chọn
            Log.d("Calendar", "Selected date: $newDate")
            // Hiển thị EmojiBox khi chọn ngày
            showEmojiBox.value = true // Hiển thị EmojiBox khi chọn ngày
        }
    )
    val diaryText = remember { mutableStateOf("") } // Biến để lưu nội dung nhật ký

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd1e9f6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 80.dp
                ) // Đặt khoảng trống cho ActionBar
        ) {
            // Hiển thị thông điệp chào
            Text(
                text = "Hello, ${currentUser?.displayName ?: "User"}! How are you feeling today?",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )

            // Hiển thị CalendarView ngay lập tức
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
                        disabledDates = disabledDates, // Danh sách các ngày không được chọn
                    )
                )
            }
            // Hiển thị EmojiBox khi showEmojiBox là true
            EmojiBox(
                showDialog = showEmojiBox.value,
                onDismiss = { showEmojiBox.value = false }, // Ẩn EmojiBox khi nhấn ra ngoài
                onEmojiClick = { emoji ->
                    // Xử lý khi người dùng chọn cảm xúc
                    Log.d("Emoji", "Selected emoji: $emoji")
                    showEmojiBox.value = false // Ẩn EmojiBox sau khi chọn cảm xúc
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Ô ghi nhật ký
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Write something on" + (selectedDate.value?.let { " ${it.dayOfMonth}-${it.monthValue}-${it.year}" }
                        ?: " today"),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    // Sử dụng BasicTextField để người dùng có thể nhập liệu
                    BasicTextField(
                        value = diaryText.value,
                        onValueChange = { newText -> diaryText.value = newText },
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier.fillMaxSize(),
                        decorationBox = { innerTextField ->
                            if (diaryText.value.isEmpty()) {
                                Text("Start writing here...", color = Color.Gray)
                            }
                            innerTextField() // Hiển thị nội dung nhập liệu
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Đẩy ActionBar xuống đáy
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Cố định ActionBar ở dưới
        ) {
            ActionBar(
                onHomeClick = {}, // Đây là trang Home, nên không cần xử lý
                onDashboardClick = onNavigateToDashboard,
                onAccountClick = onNavigateToAccount
            )
        }
    }
}