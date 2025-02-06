package com.example.myemo.mainpage.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DeleteAccountDialog(
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean // Thêm biến kiểm tra trạng thái Loading
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) { // Không cho đóng nếu đang Loading
                    openDialog.value = false
                    onDismiss()
                }
            },
            title = { Text(text = stringResource(R.string.confirmdeleteaccount)) },
            text = {
                Text(
                    text = stringResource(R.string.deleteaccountwarning),
                    color = Color(0xFFFF7260),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirmed()
                    }
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Delete")
                    }
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFd1e9f6) // Nền trắng của button
                    ),
                    onClick = {
                        if (!isLoading) {
                            openDialog.value = false
                            onDismiss()
                        }
                    },
                    enabled = !isLoading // Disable khi đang Loading
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            },
            containerColor = Color.White // Đổi màu nền thành trắng
        )
    }
}

@Composable
fun DeleteAccountButton(onLogout: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    var isDeleting by remember { mutableStateOf(false) } // Thêm trạng thái Loading

    // Nút xóa tài khoản
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
            .background(Color.White) // Màu nền khi không nhấn
            .clickable(onClick = { showDeleteDialog = true }) // Xử lý sự kiện nhấn
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
        ) {
            Text(
                text = stringResource(R.string.deleteaccount),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp
            )
        }
    }

    // Hiển thị hộp thoại xác nhận
    if (showDeleteDialog) {
        DeleteAccountDialog(
            onDeleteConfirmed = {
                isDeleting = true // Hiển thị Loading khi bắt đầu xóa
                val user = FirebaseAuth.getInstance().currentUser
                val db = FirebaseFirestore.getInstance()

                user?.let { currentUser ->
                    val email = currentUser.email
                    if (email != null) {
                        currentUser.delete().addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                Log.d("DeleteAccount", "User account deleted.")
                                preferenceManager.clearAvatarUri(email) // Xóa avatar khỏi SharedPreferences
                                preferenceManager.clearReminderTime(email) // Xóa reminder khỏi SharedPreferences

                                // Xóa dữ liệu người dùng trong Firestore (bảng users)
                                db.collection("users").document(currentUser.uid)
                                    .delete()
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "User data deleted from Firestore.")

                                        // Lấy danh sách tất cả nhật ký có email trùng khớp
                                        db.collection("diaryEntries")
                                            .whereEqualTo("email", email)
                                            .get()
                                            .addOnSuccessListener { querySnapshot ->
                                                val batch = db.batch()
                                                for (document in querySnapshot.documents) {
                                                    batch.delete(document.reference)
                                                }
                                                batch.commit()
                                                    .addOnSuccessListener {
                                                        Log.d(
                                                            "Firestore",
                                                            "All diary entries deleted."
                                                        )
                                                        isDeleting = false // Ẩn Loading
                                                        showDeleteDialog = false // Ẩn hộp thoại
                                                        onLogout() // Đăng xuất sau khi xóa thành công
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e(
                                                            "Firestore",
                                                            "Failed to delete diary entries",
                                                            e
                                                        )
                                                        isDeleting = false // Ẩn Loading
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Firestore", "Error getting diary entries", e)
                                                isDeleting = false // Ẩn Loading
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Failed to delete user data", e)
                                        isDeleting = false // Ẩn Loading
                                    }
                            } else {
                                Log.e(
                                    "DeleteAccount",
                                    "Failed to delete account",
                                    deleteTask.exception
                                )
                                isDeleting = false // Ẩn Loading
                            }
                        }
                    } else {
                        Log.e("DeleteAccount", "User email is null, cannot delete diary entries.")
                        isDeleting = false // Ẩn Loading
                    }
                }
            },
            onDismiss = {
                showDeleteDialog = false
            },
            isLoading = isDeleting // Truyền trạng thái Loading
        )
    }
}
