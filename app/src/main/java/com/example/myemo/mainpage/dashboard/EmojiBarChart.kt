package com.example.myemo.mainpage.dashboard

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysInMonth(year: Int, month: Int): List<Int> {
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth()
    return (1..daysInMonth).toList()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun EmotionBarChart(
    userId: String,
    year: Int,
    month: Int,
    modifier: Modifier
) {
    val x = getDaysInMonth(year, month)
    val modelProducer = remember { CartesianChartModelProducer() }

    // Sử dụng `remember` để giữ dữ liệu sau khi lấy
    val emotionData = remember { mutableStateListOf<Int>() }
    val isDataFetched = remember(year, month) { mutableStateOf(false) }

    if (!isDataFetched.value) {
        // Chỉ gọi `getEmotionDataForMonth` nếu dữ liệu chưa được lấy
        getEmotionDataForMonth(userId, year, month) { data ->
            emotionData.clear() // Xóa dữ liệu cũ
            emotionData.addAll(data) // Thêm dữ liệu mới
            isDataFetched.value = true // Đánh dấu dữ liệu đã được lấy
            Log.d("EmotionData", "Received emotion data: $emotionData")
        }
    }

    // Chỉ cập nhật biểu đồ khi có dữ liệu
    LaunchedEffect(emotionData.toList(), year, month) {
        if (emotionData.isNotEmpty()) {
            withContext(Dispatchers.Default) {
                modelProducer.runTransaction {
                    lineSeries { series(x, emotionData) } // Cập nhật biểu đồ
                }
            }
        } else {
            Log.e("EmotionData", "No data to update chart")
        }
    }

    EmotionLineChart(modelProducer, modifier)
}

@Composable
private fun EmotionLineChart(modelProducer: CartesianChartModelProducer, modifier: Modifier) {
    // Định nghĩa CartesianValueFormatter tùy chỉnh
    val fixedValueFormatter = CartesianValueFormatter { _, value, _ ->
        // Hiển thị văn bản tương ứng thay vì các số cụ thể
        when (value.toInt()) {
            1 -> "Angry"
            2 -> "Sad"
            3 -> "Bored"
            4 -> "Neutral"
            5 -> "Happy"
            else -> " " // Nếu không thuộc khoảng trên, không hiển thị gì
        }
    }

    val marker = rememberMarker()
    var xValue by remember { mutableIntStateOf(5) } // Biến trạng thái cho vị trí marker
    val markerVisibilityListener = object : CartesianMarkerVisibilityListener {
        override fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
            onMarkerVisibilityChanged(true, targets)
        }

        override fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
            onMarkerVisibilityChanged(true, targets)
        }

        override fun onHidden(marker: CartesianMarker) {
            onMarkerVisibilityChanged(false, emptyList())
        }

        private fun onMarkerVisibilityChanged(
            visibility: Boolean,
            targets: List<CartesianMarker.Target>
        ) {
            if (visibility && targets.isNotEmpty()) {
                val target = targets.first()
                xValue = target.x.toInt() // Giá trị trục x
            }
        }
    }
    Log.d("Marker", "X Value: $xValue")

    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0xffA2D2DF))) }
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = fixedValueFormatter,
                itemPlacer = VerticalAxis.ItemPlacer.step({ 1.0 }),
            ),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                guideline = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
            ),
            marker = marker,
            markerVisibilityListener = markerVisibilityListener,
            layerPadding = cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp),
            persistentMarkers = rememberExtraLambda(marker) { marker at xValue },
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun getEmotionDataForMonth(
    userId: String,
    year: Int,
    month: Int,
    callback: (List<Int>) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val emotionData =
        MutableList(getDaysInMonth(year, month).size) { 0 }

    val startDate = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // tháng bắt đầu từ 0
        set(Calendar.DAY_OF_MONTH, 1)
    }.time

    val endDate = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
    }.time

    // Định dạng ngày để sử dụng làm điều kiện lọc
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val startDateStr = dateFormat.format(startDate)
    val endDateStr = dateFormat.format(endDate)

    // Lấy dữ liệu từ Firestore
    db.collection("diaryEntries")
        .whereEqualTo("email", userId)
        .whereGreaterThanOrEqualTo("date", startDateStr)
        .whereLessThanOrEqualTo("date", endDateStr)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val dateStr = document.getString("date") ?: continue
                val emoji = document.getString("emoji") ?: continue

                // Tính chỉ số ngày để lưu giá trị cảm xúc
                val dayIndex =
                    SimpleDateFormat("dd", Locale.getDefault()).format(dateFormat.parse(dateStr)!!)
                        .toInt() - 1
                emotionData[dayIndex] += when (emoji) {
                    "Happy" -> 5
                    "Neutral" -> 4
                    "Bored" -> 3
                    "Sad" -> 2
                    "Angry" -> 1
                    else -> 0
                }

                // In ra dữ liệu để kiểm tra
                Log.d(
                    "EmotionData",
                    "Date: $dateStr, Emoji: $emoji, Day Index: $dayIndex, Current Emotion Value: ${emotionData[dayIndex]}"
                )
            }

            // In ra toàn bộ emotionData để kiểm tra
            Log.d("EmotionData", "Emotion Data for month $month/$year: $emotionData")
            callback(emotionData) // Trả về danh sách chứa dữ liệu cảm xúc
        }
        .addOnFailureListener {
            // Xử lý lỗi, ví dụ: log hoặc thông báo lỗi cho người dùng
            Log.e("EmotionData", "Error fetching data", it)
            callback(emotionData) // Trả về danh sách mặc định với giá trị 0 cho mọi ngày
        }
}
