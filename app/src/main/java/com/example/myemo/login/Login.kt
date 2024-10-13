package com.example.myemo.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(onLoginClick: (String?) -> Unit, onSignUpClick: () -> Unit) {
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val isFieldsNotEmpty = email.isNotEmpty() && password.isNotEmpty()
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoginFail by remember { mutableStateOf(false) }
    // Biến để kiểm tra email và password có rỗng hay không
    var isEmailEmpty by remember { mutableStateOf(false) }
    var isPasswordEmpty by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEDFCC4), // Màu đầu tiên
                        Color(0xFFF2FDD0), // Màu thứ hai
                        Color(0xFFF6FDDC), // Màu thứ ba
                        Color(0xFFFAFEE9), // Màu thứ tư
                        Color(0xFFFDFFF5)  // Màu cuối
                    ),
                    start = Offset(0f, 1000f),  // Bắt đầu từ dưới lên
                    end = Offset(0f, 0f)  // Kết thúc ở trên
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText(
                    text = "Sign In to",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Inner Space",
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Blue
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            LoginTextField(
                value = email,
                onValueChange = setEmail,
                labelText = "Email",
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(isEmailEmpty) {
                Text(
                    "Email is not fill!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Password Field
            LoginTextField(
                value = password,
                onValueChange = setPassword,
                labelText = "Password",
                modifier = Modifier
                    .height(60.dp)
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
            AnimatedVisibility(isPasswordEmpty) {
                Text(
                    "Password is not fill!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Login Button
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .width(250.dp)
                    .clip(RoundedCornerShape(20.dp)) // Bo góc trước khi thiết lập viền
                    .border(
                        2.dp,
                        Color.Black,
                        RoundedCornerShape(20.dp)
                    ), // Viền màu đen với bo góc 20dp
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White // Nền trắng của button
                ),
                onClick = {
                    // Kiểm tra các trường email và password
                    isEmailEmpty = email.isEmpty()
                    isPasswordEmpty = password.isEmpty()

                    // Xử lý đăng nhập tại đây
                    if (isFieldsNotEmpty) {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Đăng nhập thành công
                                    val user = FirebaseAuth.getInstance().currentUser
                                    user?.let {
                                        Log.d("LoginScreen", "User: ${it.displayName}, ${it.email}")
                                        onLoginClick(it.email)
                                    }
                                } else {
                                    isLoginFail = true
                                    Log.d(
                                        "SignInScreen",
                                        "Sign In Failed: ${task.exception?.message}"
                                    )
                                }
                            }
                    }
                },
            ) {
                Text(
                    "Log In",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(isLoginFail) {
                Text(
                    "Email or Password incorrect!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    "Don't have an Account?",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TextButton(
                    onClick = onSignUpClick,
                ) {
                    Text("Sign Up")
                }
            }
        }
    }
}