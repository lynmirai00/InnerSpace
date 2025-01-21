package com.example.myemo.mainpage.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

@SuppressLint("MutableCollectionMutableState", "AutoboxingStateCreation")
@ExperimentalMaterial3Api
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    onNavigateToDashboard: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val calendar = Calendar.getInstance()

    // Biến trạng thái lưu cảm xúc và nhật ký mỗi ngày
    var dayToEmojiMap by remember { mutableStateOf<Map<LocalDate, String>>(emptyMap()) }
    val diaryText = remember { mutableStateOf("") }

    // Trạng thái ngày đã chọn
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    // Biến trạng thái hiển thị hộp thoại emoji
    val showEmojiBox = remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    // Callback khi tháng được chọn
    val onMonthSelected: (Int) -> Unit = { newMonth ->
        // Xử lý khi tháng được chọn, ví dụ cập nhật UI hoặc gửi dữ liệu đến các phần khác
        selectedMonth = newMonth
        Log.d("Home", "Selected Month: $newMonth")
    }

    // Callback khi năm được chọn
    val onYearSelected: (Int) -> Unit = { newYear ->
        // Xử lý khi năm được chọn
        selectedYear = newYear
        Log.d("Home", "Selected Year: $newYear")
    }

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
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }

    // Tự động lưu nhật ký khi diaryText thay đổi
    LaunchedEffect(diaryText.value) {
        selectedDate.value?.let { date ->
            if (dayToEmojiMap.containsKey(date)) {
                saveDiaryEntry(date, dayToEmojiMap[date], diaryText.value)
            }
        }
    }


    // Tự động tải dữ liệu emoji khi ngày, tháng hoặc năm thay đổi
    LaunchedEffect(selectedYear, selectedMonth) {
        dayToEmojiMap = fetchEmojiData(currentUser?.email ?: "", selectedYear, selectedMonth)
        selectedDate.value = null // Đặt lại ngày đã chọn để cập nhật dữ liệu mới
        diaryText.value = ""
    }

    // Lấy dữ liệu từ Firebase khi chọn ngày
    LaunchedEffect(selectedDate.value) {
        val date = selectedDate.value
        if (date != null && currentUser != null) {
            val documentId = "${currentUser.email}_${date}"
            try {
                val document = FirebaseFirestore.getInstance()
                    .collection("diaryEntries")
                    .document(documentId)
                    .get()
                    .await()

                if (document.exists()) {
                    // Cập nhật trạng thái nhật ký
                    diaryText.value = document.getString("diary") ?: ""
                } else {
                    // Nếu không có nhật ký, đặt giá trị trống
                    diaryText.value = ""
                }
            } catch (e: Exception) {
                Log.e("DiaryLoadError", "Error loading diary for $date: ${e.message}", e)
                diaryText.value = "" // Đặt lại giá trị trống trong trường hợp lỗi
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.greeting, currentUser?.displayName ?: "User"),
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

            CalendarBox(
                month = selectedMonth,
                year = selectedYear,
                dayToEmojiMap = dayToEmojiMap,
                onDaySelected = { day: LocalDate? ->
                    selectedDate.value = day
                    showEmojiBox.value = true
                },
                onMonthSelected = onMonthSelected,  // Truyền callback cho tháng
                onYearSelected = onYearSelected     // Truyền callback cho năm
            )

            EmojiBox(
                showDialog = showEmojiBox.value,
                onDismiss = {
                    // Kiểm tra nếu ngày được chọn đã có emoji
                    val selectedDay = selectedDate.value
                    if (selectedDay != null && dayToEmojiMap.containsKey(selectedDay)) {
                        // Ngày đã có emoji, không làm gì
                    } else {
                        // Ngày chưa có emoji, đặt selectedDate về null
                        selectedDate.value = null
                    }
                    showEmojiBox.value = false
                },
                onEmojiClick = { emoji ->
                    val updatedMap = dayToEmojiMap.toMutableMap()
                    updatedMap[selectedDate.value ?: LocalDate.now()] = emoji
                    dayToEmojiMap = updatedMap // cập nhật trạng thái
                    Log.d("EmojiLog", "Updated dayToEmojiMap: $dayToEmojiMap")
                    showEmojiBox.value = false
                    saveDiaryEntry(selectedDate.value, emoji, diaryText.value)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column {

                Text(
                    text = selectedDate.value?.let { stringResource(id = R.string.onday, "${it.year}-${it.monthValue}-${it.dayOfMonth}") }
                        ?: stringResource(R.string.selectdate),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp, bottom = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
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
                                Text(text = stringResource(R.string.diaryhint), color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
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
                onAccountClick = onNavigateToAccount,
                backgroundColor = backgroundColor.value // Pass the selected color
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

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fetchEmojiData(email: String, year: Int, month: Int): Map<LocalDate, String> {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("diaryEntries")
    val result = mutableMapOf<LocalDate, String>()

    try {
        Log.d("fetchEmojiData", "Fetching data for email: $email, year: $year, month: $month")

        val documents = collectionRef
            .whereEqualTo("email", email)
            .whereGreaterThanOrEqualTo("date", "$year-${month.toString().padStart(2, '0')}-01")
            .whereLessThanOrEqualTo(
                "date",
                "$year-${month.toString().padStart(2, '0')}-${
                    YearMonth.of(year, month).lengthOfMonth()
                }"
            )
            .get()
            .await()

        for (document in documents) {
            val date = LocalDate.parse(document.getString("date"))
            val emoji = document.getString("emoji") ?: continue
            result[date] = emoji

            // Log từng entry lấy được
            Log.d("fetchEmojiData", "Fetched entry: date=$date, emoji=$emoji")
        }

        Log.d("fetchEmojiData", "Completed fetching. Total entries: ${result.size}")
    } catch (e: Exception) {
        Log.e("fetchEmojiData", "Error fetching data: ${e.message}", e)
    }

    return result
}