package com.example.myemo.mainpage.dashboard

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.example.myemo.mainpage.home.EmojiItem
import com.example.myemo.mainpage.home.fetchEmojiData
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.Calendar

@SuppressLint("AutoboxingStateCreation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dashboard(
    onNavigateToHome: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    // Trạng thái để hiển thị menu
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var monthMenuExpanded by remember { mutableStateOf(false) }
    val goodjobImages = listOf(
        R.drawable.goodjob1,
        R.drawable.goodjob2,
        R.drawable.goodjob3,
        R.drawable.goodjob4,
        R.drawable.goodjob5,
        R.drawable.goodjob6,
        R.drawable.goodjob7
    ) // Thay các tên ảnh này bằng các tài nguyên hình ảnh thực tế của bạn

    // Chọn ngẫu nhiên một hình ảnh từ danh sách
    val randomImage = goodjobImages.random()
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }
    var dayToEmojiMap by remember { mutableStateOf<Map<LocalDate, String>>(emptyMap()) }
    var monthSize by remember { mutableStateOf(getDaysInMonth(selectedYear, selectedMonth).size) }
    val emojiCounts = dayToEmojiMap.values.groupingBy { it }.eachCount()
    val prioritizedEmotions = listOf("Happy", "Neutral", "Bored", "Sad", "Angry")
    // Sắp xếp cảm xúc dựa trên số lượng và thứ tự ưu tiên
    val sortedEmotions = emojiCounts.entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
            .thenBy { prioritizedEmotions.indexOf(it.key) })
        .take(2) // Lấy 2 cảm xúc đầu tiên
    val leastFrequentEmotion = emojiCounts.entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
            .thenBy { prioritizedEmotions.indexOf(it.key) })
        .takeLast(1) // Lấy 1 cảm xúc cuoi cung
    // Lấy ngày đầu tiên và ngày cuối cùng từ keys của map
    val firstRecordedDate = dayToEmojiMap.keys.minOrNull()
    val lastRecordedDate = dayToEmojiMap.keys.maxOrNull()
    val selectedLanguage =
        remember { mutableStateOf(preferenceManager.getSelectedLanguage()) } // Ngôn ngữ mặc định là Japanese

    LaunchedEffect(currentUser?.email, selectedYear, selectedMonth) {
        dayToEmojiMap = fetchEmojiData(currentUser?.email ?: "", selectedYear, selectedMonth)
        monthSize = getDaysInMonth(selectedYear, selectedMonth).size
        Log.d("Dashboard", "dayToEmojiMap: $dayToEmojiMap")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Cho phép cuộn nội dung
        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 32.dp,
                        end = 32.dp,
                        bottom = 24.dp
                    )
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.goodjob,
                            currentUser?.displayName ?: "User"
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 19.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = randomImage), // chọn ảnh từ goodjobImages
                        contentDescription = "Flower",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Chọn tháng và năm
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                    ) {
                        // Chọn tháng
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, backgroundColor.value, RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .clickable { monthMenuExpanded = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = months[selectedMonth - 1],
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 15.sp
                            )

                            DropdownMenu(
                                expanded = monthMenuExpanded,
                                onDismissRequest = { monthMenuExpanded = false },
                                modifier = Modifier
                                    .height(250.dp)
                                    .width(120.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, backgroundColor.value, RoundedCornerShape(10.dp))
                                    .background(Color.White),
                                offset = DpOffset(
                                    0.dp,
                                    0.dp
                                ), // Điều chỉnh khoảng cách của menu nếu cần
                                shape = RoundedCornerShape(10.dp),
                                shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                            ) {
                                // Hiển thị các tháng
                                months.forEachIndexed { index, month ->
                                    DropdownMenuItem(
                                        text = { Text(month) },
                                        onClick = {
                                            selectedMonth = index + 1
                                            monthMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Chọn năm
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, backgroundColor.value, RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .clickable { yearMenuExpanded = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$selectedYear",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 15.sp
                            )

                            DropdownMenu(
                                expanded = yearMenuExpanded,
                                onDismissRequest = { yearMenuExpanded = false },
                                modifier = Modifier
                                    .height(250.dp)
                                    .width(120.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, backgroundColor.value, RoundedCornerShape(10.dp))
                                    .background(Color.White),
                                offset = DpOffset(
                                    0.dp,
                                    0.dp
                                ), // Điều chỉnh khoảng cách của menu nếu cần
                                shape = RoundedCornerShape(10.dp),
                                shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                            ) {
                                // Hiển thị các năm từ 2020 đến 2030
                                (2020..2030).forEach { year ->
                                    DropdownMenuItem(
                                        text = { Text("$year") },
                                        onClick = {
                                            selectedYear = year
                                            yearMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Log.d(
                        "Dashboard",
                        "Selected Year: $selectedYear, Selected Month: $selectedMonth"
                    )

                    // Hiển thị biểu đồ cột
                    EmotionBarChart(
                        currentUser?.email.toString(),
                        selectedYear,
                        selectedMonth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "Overview of ${months[selectedMonth - 1]} $selectedYear ",
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 6.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.value)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(45.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                .background(Color.White), // Màu nền khi không nhấn
                            contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append(stringResource(R.string.recordeddays))

                                    // Thêm kiểu chữ cho số lượng emoji
                                    pushStyle(
                                        SpanStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                    append("${dayToEmojiMap.size}") // Hiển thị số lượng emoji
                                    pop() // Quay lại kiểu chữ mặc định
                                    append(" / $monthSize ${stringResource(R.string.days)}")
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Hiển thị ngày ghi nhận cảm xúc đầu tiên
                        firstRecordedDate?.let {
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${stringResource(R.string.firstrecordedday)} $it",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        // Hiển thị ngày ghi nhận cảm xúc cuối cùng
                        lastRecordedDate?.let {
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${stringResource(R.string.lastrecordedday)} $it",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Box(
                            modifier = Modifier
                                .height(45.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                .background(Color.White), // Màu nền khi không nhấn
                            contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.mostcommonemotion),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                                if (sortedEmotions.isEmpty()) {
                                    // Không có cảm xúc nào
                                    Text(
                                        text = stringResource(R.string.none),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontSize = 15.sp,
                                    )
                                } else {
                                    sortedEmotions.forEach { (emotion, count) ->
                                        Box(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp)
                                        ) {
                                            when (emotion) {
                                                "Happy" -> EmojiItem(
                                                    icon = R.drawable.happy,
                                                    label = "Happy",
                                                    onEmojiClick = {},
                                                    color = Color(0xFFFFFAE6)
                                                )

                                                "Neutral" -> EmojiItem(
                                                    icon = R.drawable.neutral,
                                                    label = "Neutral",
                                                    onEmojiClick = {},
                                                    color = Color(0xFFEAFBFE)
                                                )

                                                "Bored" -> EmojiItem(
                                                    icon = R.drawable.bored,
                                                    label = "Bored",
                                                    onEmojiClick = {},
                                                    color = Color(0xFFEDEDED)
                                                )

                                                "Sad" -> EmojiItem(
                                                    icon = R.drawable.sad,
                                                    label = "Sad",
                                                    onEmojiClick = {},
                                                    color = Color(0xFFB8D1F1)
                                                )

                                                "Angry" -> EmojiItem(
                                                    icon = R.drawable.angry,
                                                    label = "Angry",
                                                    onEmojiClick = {},
                                                    color = Color(0xFFFFD5CD)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "(${count}${stringResource(R.string.days)})",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontSize = 15.sp,
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .height(45.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                .background(Color.White), // Màu nền khi không nhấn
                            contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.leastcommonemotion),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                                if (leastFrequentEmotion.isEmpty()) {
                                    // Không có cảm xúc nào
                                    Text(
                                        text = stringResource(R.string.none),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontSize = 15.sp,
                                    )
                                } else {
                                    val (emotion, count) = leastFrequentEmotion.first() // Lấy phần tử đầu tiên trong danh sách
                                    Box(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(40.dp)
                                    ) {
                                        when (emotion) {
                                            "Happy" -> EmojiItem(
                                                icon = R.drawable.happy,
                                                label = "Happy",
                                                onEmojiClick = {},
                                                color = Color(0xFFFFFAE6)
                                            )

                                            "Neutral" -> EmojiItem(
                                                icon = R.drawable.neutral,
                                                label = "Neutral",
                                                onEmojiClick = {},
                                                color = Color(0xFFEAFBFE)
                                            )

                                            "Bored" -> EmojiItem(
                                                icon = R.drawable.bored,
                                                label = "Bored",
                                                onEmojiClick = {},
                                                color = Color(0xFFEDEDED)
                                            )

                                            "Sad" -> EmojiItem(
                                                icon = R.drawable.sad,
                                                label = "Sad",
                                                onEmojiClick = {},
                                                color = Color(0xFFB8D1F1)
                                            )

                                            "Angry" -> EmojiItem(
                                                icon = R.drawable.angry,
                                                label = "Angry",
                                                onEmojiClick = {},
                                                color = Color(0xFFFFD5CD)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "(${count}${stringResource(R.string.days)})",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontSize = 15.sp,
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (monthSize - dayToEmojiMap.size > 5) {
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                    .background(Color.White), // Màu nền khi không nhấn
                                contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                            ) {
                                Text(
                                    text = if (selectedLanguage.value == "ja") {
                                        buildAnnotatedString {
                                            append("あなたは ")
                                            pushStyle(
                                                SpanStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            )
                                            append("${monthSize - dayToEmojiMap.size}")
                                            pop() // Quay lại kiểu chữ mặc định
                                            append(" 日間感情を記録していません \uD83D\uDE22")
                                        }
                                    } else {
                                        buildAnnotatedString {
                                            append("You haven’t recorded your emotions for ")
                                            pushStyle(
                                                SpanStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            )
                                            append("${monthSize - dayToEmojiMap.size}")
                                            pop() // Quay lại kiểu chữ mặc định
                                            append(" days \uD83D\uDE22")
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                    .background(Color.White), // Màu nền khi không nhấn
                                contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                            ) {
                                Text(
                                    text = stringResource(R.string.recordemotionnotgood),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                    .background(Color.White), // Màu nền khi không nhấn
                                contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                            ) {
                                Text(
                                    text = stringResource(R.string.recordemotiongood),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(45.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)) // Đảm bảo hình dạng vuông
                                    .background(Color.White), // Màu nền khi không nhấn
                                contentAlignment = Alignment.Center // Đặt nội dung nằm giữa
                            ) {
                                Text(
                                    text = stringResource(R.string.recordemotiongood2),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(45.dp))
                    }
                }
            }
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            ActionBar(
                currentPage = "Dashboard",
                onHomeClick = onNavigateToHome,
                onDashboardClick = {},
                onAccountClick = onNavigateToAccount,
                backgroundColor = backgroundColor.value // Pass the selected color
            )
        }
    }
}
