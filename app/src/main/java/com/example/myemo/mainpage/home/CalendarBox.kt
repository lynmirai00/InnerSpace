package com.example.myemo.mainpage.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("AutoboxingStateCreation", "MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBox(
    month: Int,
    year: Int,
    dayToEmojiMap: Map<LocalDate, String>,
    onDaySelected: (LocalDate?) -> Unit,
    onMonthSelected: (Int) -> Unit,  // Callback khi chọn tháng
    onYearSelected: (Int) -> Unit    // Callback khi chọn năm
) {
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }
    val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val today = remember { LocalDate.now() }
    // Danh sách các ngày không được chọn
    val disabledDates = (1..36500).map { today.plusDays(it.toLong()) }
    var selectedMonth by remember { mutableStateOf(month) }
    var selectedYear by remember { mutableStateOf(year) }
    // Trạng thái để hiển thị menu
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var monthMenuExpanded by remember { mutableStateOf(false) }
    val daysInMonth = getDaysInMonths(selectedMonth, selectedYear)
    val firstDayOfWeek = getFirstDayOfWeek(selectedMonth, selectedYear)

    // Hàm để gọi callback khi chọn tháng
    fun onMonthChanged(newMonth: Int) {
        selectedMonth = newMonth
        onMonthSelected(newMonth)  // Gọi callback cho Home
    }

    // Hàm để gọi callback khi chọn năm
    fun onYearChanged(newYear: Int) {
        selectedYear = newYear
        onYearSelected(newYear)  // Gọi callback cho Home
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Month and Year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .background(backgroundColor.value, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            // Giảm tháng
                            if (selectedMonth == 1) {
                                selectedMonth = 12
                                selectedYear -= 1 // Giảm năm nếu đang là tháng 1
                            } else {
                                selectedMonth -= 1
                            }
                            onMonthChanged(selectedMonth)
                            onYearChanged(selectedYear)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "arrow",
                        modifier = Modifier.size(14.dp),
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { monthMenuExpanded = true },
                ) {
                    Text(
                        text = months[selectedMonth - 1],
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(4.dp)
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
                        offset = DpOffset(0.dp, 0.dp), // Điều chỉnh khoảng cách của menu nếu cần
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                    ) {
                        // Hiển thị các tháng
                        months.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = index + 1
                                    onMonthChanged(selectedMonth)
                                    monthMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (monthMenuExpanded) R.drawable.bottom_line else R.drawable.straight),
                        contentDescription = "bottom_line",
                        modifier = Modifier.size(12.dp),
                        tint = backgroundColor.value
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { yearMenuExpanded = true },
                ) {
                    Text(
                        text = selectedYear.toString(),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(4.dp)
                    )
                    DropdownMenu(
                        expanded = yearMenuExpanded,
                        onDismissRequest = { yearMenuExpanded = false },
                        modifier = Modifier
                            .height(250.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, backgroundColor.value, RoundedCornerShape(10.dp))
                            .background(Color.White),
                        offset = DpOffset(0.dp, 0.dp), // Điều chỉnh khoảng cách của menu nếu cần
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 0.dp, // Có thể điều chỉnh bóng mờ ở đây
                    ) {
                        // Hiển thị các năm từ 2020 đến 2030
                        (2020..2030).forEach { year ->
                            DropdownMenuItem(
                                text = { Text("$year") },
                                onClick = {
                                    selectedYear = year
                                    onYearChanged(selectedYear)
                                    yearMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (yearMenuExpanded) R.drawable.bottom_line else R.drawable.straight),
                        contentDescription = "bottom_line",
                        modifier = Modifier.size(12.dp),
                        tint = backgroundColor.value
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .background(backgroundColor.value, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            // Tăng tháng
                            if (selectedMonth == 12) {
                                selectedMonth = 1
                                selectedYear += 1 // Tăng năm nếu đang là tháng 12
                            } else {
                                selectedMonth += 1
                            }
                            onMonthChanged(selectedMonth)
                            onYearChanged(selectedYear)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.to_right),
                        contentDescription = "to_right",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Week Days
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Box(
                        contentAlignment = Alignment.Center, // Căn giữa văn bản
                        modifier = Modifier
                            .width(40.dp)
                            .padding(3.dp)
                            .background(backgroundColor.value)
                    ) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            // Calendar Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .padding(5.dp)
                    .heightIn(max = 350.dp) // Giới hạn chiều cao
            ) {
                // Add empty slots for days before the first day of the week
                for (i in 1 until firstDayOfWeek) {
                    item { Spacer(modifier = Modifier.size(40.dp)) }
                }

                // Add days of the month
                for (day in daysInMonth) {
                    item {
                        DayItem(
                            day = day,
                            year = selectedYear,
                            month = selectedMonth,
                            emoji = dayToEmojiMap[LocalDate.of(selectedYear, selectedMonth, day)],
                            onDaySelected = onDaySelected,
                            disabledDates = disabledDates
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayItem(
    day: Int,
    year: Int,
    month: Int,
    emoji: String?,
    onDaySelected: (LocalDate?) -> Unit,
    disabledDates: List<LocalDate>
) {
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }
    var isSelected by remember { mutableStateOf(false) }
    val today = remember { LocalDate.now() } // Lấy ngày hôm nay
    val isToday =
        today.dayOfMonth == day && today.monthValue == month && today.year == year // Kiểm tra ngày hôm nay
    // Tạo LocalDate cho ngày hiện tại
    val currentDayDate = LocalDate.of(year, month, day)

    val isDisabled = disabledDates.contains(currentDayDate) // Kiểm tra nếu ngày bị vô hiệu hóa

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp)) // Đảm bảo hình dạng vuông
            .run {
                // Vô hiệu hóa khả năng click nếu ngày nằm trong disabledDates
                if (isDisabled) this else clickable {
                    isSelected = !isSelected
                    onDaySelected(currentDayDate) // Truyền ngày được chọn
                }
            }
    ) {
        if (emoji.isNullOrEmpty()) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = when {
                    isDisabled -> Color.Gray
                    isToday -> backgroundColor.value
                    else -> Color.Black
                }
            )
        } else {
            val (iconResId, bgColor) = getEmojiImageResourceAndColor(emoji)
                ?: (R.drawable.home to Color.Gray)
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
            ) {
                EmojiItem(
                    icon = iconResId,
                    label = emoji,
                    onEmojiClick = { onDaySelected(currentDayDate) },
                    color = bgColor
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFirstDayOfWeek(month: Int, year: Int): Int {
    // Sử dụng LocalDate để xác định ngày đầu tuần
    val firstDay = LocalDate.of(year, month, 1)
    return (firstDay.dayOfWeek.value % 7) + 1 // Chuyển về 1 (Chủ nhật) -> 7 (Thứ bảy)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysInMonths(month: Int, year: Int): List<Int> {
    // Sử dụng YearMonth để lấy số ngày trong tháng
    val yearMonth = YearMonth.of(year, month)
    return (1..yearMonth.lengthOfMonth()).toList()
}

fun getEmojiImageResourceAndColor(emoji: String?): Pair<Int, Color>? {
    return when (emoji) {
        "Happy" -> R.drawable.happy to Color(0xFFFFFAE6)
        "Neutral" -> R.drawable.neutral to Color(0xFFEAFBFE)
        "Bored" -> R.drawable.bored to Color(0xFFEDEDED)
        "Sad" -> R.drawable.sad to Color(0xFFB8D1F1)
        "Angry" -> R.drawable.angry to Color(0xFFFFD5CD)
        else -> null
    }
}