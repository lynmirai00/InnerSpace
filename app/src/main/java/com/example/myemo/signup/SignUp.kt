package com.example.myemo.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.R
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun SignUp(onSignUpClick: (String?) -> Unit, onLoginClick: () -> Unit) {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val (name, onNameChange) = rememberSaveable { mutableStateOf("") }
    val (email, onEmailChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, onConfirmPasswordChange) = rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordSame by remember { mutableStateOf(false) }
    var isSignUpFail by remember { mutableStateOf(false) }
    var isNameEmpty by remember { mutableStateOf(false) }
    var isEmailEmpty by remember { mutableStateOf(false) }
    var isEmailWrong by remember { mutableStateOf(false) }
    var isPasswordEmpty by remember { mutableStateOf(false) }
    var isConfirmPasswordEmpty by remember { mutableStateOf(false) }
    var isPasswordTooShort by remember { mutableStateOf(false) }
    val isFieldsNotEmpty = name.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty() &&
            confirmPassword.isNotEmpty()
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
                    start = Offset(0f, 0f),  // Bắt đầu từ dưới lên
                    end = Offset(1000f, 0f)  // Kết thúc ở trên
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText(
                    text = "Sign Up to",
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

            // Name Field
            LoginTextField(
                value = name,
                onValueChange = onNameChange,
                labelText = "Name",
                modifier = Modifier
                    .height(60.dp)
                    .width(250.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(isNameEmpty) {
                Text(
                    "Name is not fill!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(10.dp))

            // Email Field
            LoginTextField(
                value = email,
                onValueChange = onEmailChange,
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
            AnimatedVisibility(isEmailWrong) {
                Text(
                    "Please enter a valid email format!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(10.dp))

            // Password Field
            LoginTextField(
                value = password,
                onValueChange = onPasswordChange,
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
            AnimatedVisibility(isPasswordTooShort) {
                Text(
                    "Password must be at least 6 characters long!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(10.dp))

            // Confirm Password Field
            LoginTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                labelText = "Confirm Password",
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
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(isConfirmPasswordEmpty) {
                Text(
                    "Confirm Password is not fill!",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            AnimatedVisibility(isPasswordSame) {
                Text(
                    "Password is not matching",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(20.dp))

            // Sign Up Button
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
                    isNameEmpty = name.isEmpty()
                    isEmailEmpty = email.isEmpty()
                    isPasswordEmpty = password.isEmpty()
                    isConfirmPasswordEmpty = confirmPassword.isEmpty()

                    if (isFieldsNotEmpty) {
                        // Kiểm tra độ dài của password
                        isPasswordTooShort = password.length < 6
                        // Kiểm tra email có đúng định dạng không
                        if (!isEmailEmpty) {
                            isEmailWrong = !isValidEmail(email.trim())
                        }
                        // Kiểm tra password và confirm password có giống nhau không
                        isPasswordSame = password != confirmPassword

                        // Xử lý đăng ký tài khoản
                        if (!isPasswordSame && !isEmailWrong && !isPasswordTooShort) {
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = FirebaseAuth.getInstance().currentUser
                                        val userData = hashMapOf(
                                            "name" to name,
                                            "email" to email,
                                            "password" to password
                                        )
                                        user?.let {
                                            Firebase.firestore.collection("users")
                                                .document(it.uid)
                                                .set(userData)
                                                .addOnSuccessListener {
                                                    // Navigate to main page
                                                    onSignUpClick(email)
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.d(
                                                        "SignUp",
                                                        "Sign Up Failed: ${e.message}"
                                                    )
                                                }
                                        }
                                    } else {
                                        isSignUpFail = true
                                        Log.d(
                                            "SignUpScreen",
                                            "Sign Up Failed: ${task.exception?.message}"
                                        )
                                    }
                                }
                        }
                    }
                },
            ) {
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(isSignUpFail) {
                Text(
                    "This email is exist",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Row
            Row {
                Text(
                    "Already have an Account?",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TextButton(onClick = onLoginClick) {
                    Text("Sign In")
                }
            }
        }
    }
}