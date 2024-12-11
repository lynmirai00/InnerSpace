package com.example.myemo.mainpage.account

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.components.LoginTextField
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordDialog(
    onConfirm: (String) -> Unit, // Tham số cho mật khẩu cũ, mới, và xác nhận mật khẩu
    onDismiss: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isOldPasswordEmpty by remember { mutableStateOf(false) }
    var isNewPasswordEmpty by remember { mutableStateOf(false) }
    var isConfirmPasswordEmpty by remember { mutableStateOf(false) }
    val isFieldsNotEmpty = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
    var isPasswordTooShort by remember { mutableStateOf(false) }
    var isPasswordSame by remember { mutableStateOf(false) }
    var isOldPasswordWrong by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Change Password") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Nhập mật khẩu cũ
                LoginTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    labelText = "Old Password",
                    modifier = Modifier
                        .height(65.dp)
                        .width(250.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) {
                            painterResource(id = R.drawable.ic_action_visibility_black)
                        } else {
                            painterResource(id = R.drawable.ic_action_visibility_off_black)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = icon, contentDescription = null)
                        }
                    },
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(isOldPasswordEmpty) {
                    Text(
                        "Old Password is not filled!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                AnimatedVisibility(isOldPasswordWrong) {
                    Text(
                        "Old Password is not correct!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Nhập mật khẩu mới
                LoginTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    labelText = "New Password",
                    modifier = Modifier
                        .height(65.dp)
                        .width(250.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) {
                            painterResource(id = R.drawable.ic_action_visibility_black)
                        } else {
                            painterResource(id = R.drawable.ic_action_visibility_off_black)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = icon, contentDescription = null)
                        }
                    },
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(isNewPasswordEmpty) {
                    Text(
                        "New Password is not filled!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                AnimatedVisibility(isPasswordTooShort) {
                    Text(
                        "Password must be at least 6 characters long!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Xác nhận mật khẩu mới
                LoginTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    labelText = "Confirm New Password",
                    modifier = Modifier
                        .height(65.dp)
                        .width(250.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) {
                            painterResource(id = R.drawable.ic_action_visibility_black)
                        } else {
                            painterResource(id = R.drawable.ic_action_visibility_off_black)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = icon, contentDescription = null)
                        }
                    },
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(isConfirmPasswordEmpty) {
                    Text(
                        "Confirm New Password is not filled!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                AnimatedVisibility(isPasswordSame) {
                    Text(
                        "Password is not matching!",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isOldPasswordEmpty = oldPassword.isEmpty()
                    isNewPasswordEmpty = newPassword.isEmpty()
                    isConfirmPasswordEmpty = confirmPassword.isEmpty()

                    if (isFieldsNotEmpty) {
                        // Kiểm tra độ dài của password
                        isPasswordTooShort = newPassword.length < 6

                        // Kiểm tra password và confirm password có giống nhau không
                        isPasswordSame = newPassword != confirmPassword

                        if (!isPasswordSame && !isPasswordTooShort) {
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.let {
                                val credential =
                                    EmailAuthProvider.getCredential(user.email!!, oldPassword)

                                user.reauthenticate(credential)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Nếu xác thực thành công, đổi mật khẩu
                                            isOldPasswordWrong = false
                                            onConfirm(newPassword)
                                        } else {
                                            // Nếu xác thực thất bại (mật khẩu cũ sai)
                                            isOldPasswordWrong = true
                                        }
                                    }
                            }
                        }
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd1e9f6) // Nền trắng của button
                ),
                onClick = { onDismiss() }
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.primary)
            }
        },
        containerColor = Color.White // Đổi màu nền thành trắng
    )
}

@Composable
fun ChangePasswordButton(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    // Nút đổi mật khẩu
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
            .background(Color.White) // Màu nền khi không nhấn
            .clickable(onClick = { showChangePasswordDialog = true }) // Xử lý sự kiện nhấn
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Start // Căn chỉnh về bên trái
        ) {
            Text(
                "Change Password",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp
            )
        }
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onConfirm = { newPassword ->
                // Thực hiện kiểm tra mật khẩu mới
                val user = FirebaseAuth.getInstance().currentUser
                // Đổi mật khẩu
                user?.updatePassword(newPassword)
                    ?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d("ChangePassword", "Password updated successfully")
                            // Hiển thị thông báo thành công, đóng dialog
                            scope.launch {
                                snackbarHostState.showSnackbar("Password updated successfully \uD83C\uDF89")
                            }
                            showChangePasswordDialog = false
                        } else {
                            Log.e("ChangePassword", "Failed to update password", updateTask.exception)
                            // Hiển thị thông báo lỗi
                            scope.launch {
                                snackbarHostState.showSnackbar("Failed to update password")
                            }
                        }
                    }
            },
            onDismiss = { showChangePasswordDialog = false }
        )
    }
}
