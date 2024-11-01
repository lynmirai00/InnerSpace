package com.example.myemo.mainpage.account

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.mainpage.ActionBar
import com.example.myemo.selectedBackgroundColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun Account(
    onNavigateToDashboard: () -> Unit,
    onNavigateToHome: () -> Unit,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current // Lấy context
    val currentUser = FirebaseAuth.getInstance().currentUser
    var showChangeNameDialog by remember { mutableStateOf(false) }
    var showReminderTimeDialog by remember { mutableStateOf(false) }
    var reminderTime by remember {
        mutableStateOf(
            getReminderTime(context) ?: "10:00"
        )
    } // Giá trị mặc định

    // Ở phần UI chính, tạo SnackbarHostState và CoroutineScope
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Nội dung cuộn nằm bên trên
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Cho phép cuộn nội dung
                .padding(bottom = 80.dp) // Dành khoảng trống cho ActionBar
        ) {
            Spacer(modifier = Modifier.height(200.dp)) // Khoảng cách giữa các phần

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(selectedBackgroundColor.value)
                    .offset(y = (-75).dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(selectedBackgroundColor.value, shape = RoundedCornerShape(75.dp))
                        .border(
                            8.dp,
                            selectedBackgroundColor.value,
                            RoundedCornerShape(75.dp)
                        ) // Thêm viền tròn xung quanh ảnh đại diện
                ) {
                    // Hiển thị ảnh đại diện
                    Image(
                        painter = painterResource(id = R.drawable.a13), // Thay 'your_image' bằng tên ảnh trong res/drawable
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(140.dp) // Kích thước ảnh nhỏ hơn kích thước Box (còn lại là viền)
                            .clip(RoundedCornerShape(70.dp)) // Cắt ảnh thành hình tròn
                            .align(Alignment.Center) // Canh ảnh vào giữa Box
                    )
                }
                Text(
                    text = currentUser?.displayName ?: "Inner Space",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Text(
                    text = currentUser?.email ?: "innerspace@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(30.dp))

                //Nut Today Message
                ChangeBackgroundColorDialog()
                Spacer(modifier = Modifier.height(20.dp))

                //Nut set thoi gian nhac nho
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                        .background(Color.White) // Màu nền khi không nhấn
                        .clickable(onClick = {
                            showReminderTimeDialog = true
                        }) // Xử lý sự kiện nhấn
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        horizontalArrangement = Arrangement.SpaceBetween // Căn chỉnh về bên trái
                    ) {
                        Text(
                            "Set Reminder Time",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp
                        )
                        Text(
                            text = reminderTime,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                SetReminderTimeDialog(
                    context = LocalContext.current, // Lấy context hiện tại
                    showDialog = showReminderTimeDialog,
                    onDismiss = { showReminderTimeDialog = false },
                    onTimeSet = { time ->
                        reminderTime = time // Cập nhật giờ nhắc nhở
                        saveReminderTime(context, time) // Lưu giờ nhắc nhở vào SharedPreferences
                        // Gọi hàm setupReminder để khởi động nhắc nhở
                        setupReminder(context, reminderTime)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Nút thông báo nhắc nhở
                ReminderNotification(
                    context = context,
                    reminderTime = reminderTime // Nhớ cập nhật giá trị reminderTime
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Nút chỉnh sửa ten
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                        .background(Color.White) // Màu nền khi không nhấn
                        .clickable(onClick = { showChangeNameDialog = true }) // Xử lý sự kiện nhấn
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
                    ) {
                        Text(
                            "Change Name",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp
                        )
                    }
                }
                if (showChangeNameDialog) {
                    ChangeNameDialog(
                        onConfirm = { newName ->
                            // Lấy người dùng hiện tại
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.let {
                                val userId = it.uid
                                val db = FirebaseFirestore.getInstance()

                                // Cập nhật tên trong bảng users với userId
                                val userRef = db.collection("users").document(userId)

                                // Cập nhật Firestore
                                userRef.update("name", newName)
                                    .addOnSuccessListener {
                                        // Tạo đối tượng cập nhật hồ sơ
                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = newName // Cập nhật tên hiển thị
                                        }

                                        // Cập nhật displayName trong Firebase Authentication
                                        user.updateProfile(profileUpdates)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Log.d(
                                                        "ChangeName",
                                                        "User name updated successfully in Firebase"
                                                    )
                                                    // Đóng dialog sau khi cập nhật thành công
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Name updated successfully")
                                                    }
                                                    showChangeNameDialog = false
                                                } else {
                                                    Log.e(
                                                        "ChangeName",
                                                        "Failed to update displayName",
                                                        task.exception
                                                    )
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Failed to update display name")
                                                    }
                                                }
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("ChangeName", "Error updating user name", e)
                                        // Hiển thị thông báo lỗi
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Failed to update name in Firestore")
                                        }
                                    }
                            }
                        },
                        onDismiss = { showChangeNameDialog = false }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Nút đổi mật khẩu
                ChangePasswordButton(snackbarHostState = snackbarHostState, scope = scope)
                Spacer(modifier = Modifier.height(20.dp))

                // Nút xóa tài khoản
                DeleteAccountButton(onLogout = onLogout)
                Spacer(modifier = Modifier.height(20.dp))

                //Nut feedback
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                        .background(Color.White) // Màu nền khi không nhấn
                        .clickable(onClick = {}) // Xử lý sự kiện nhấn
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
                    ) {
                        Text(
                            "Feedback",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                //Nut about us
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                        .background(Color.White) // Màu nền khi không nhấn
                        .clickable(onClick = {}) // Xử lý sự kiện nhấn
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
                    ) {
                        Text(
                            "About Us",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Nút đăng xuất
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                        .background(Color.White) // Màu nền khi không nhấn
                        .clickable(onClick = onLogout) // Xử lý sự kiện nhấn
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                        horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
                    ) {
                        Text(
                            "Log Out",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp
                        )
                    }
                }

            }
        }

        // Đặt ActionBar cố định ở dưới cùng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Cố định ActionBar ở dưới
        ) {
            ActionBar(
                onHomeClick = onNavigateToHome,
                onDashboardClick = onNavigateToDashboard,
                onAccountClick = {}
            )
        }
        // Đặt SnackbarHost trong UI
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(80.dp)
                .width(280.dp),
        )

    }
}