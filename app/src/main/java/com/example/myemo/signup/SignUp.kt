package com.example.myemo.signup

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.example.myemo.mainpage.home.EmojiItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                    .background(Color.White) // Màu nền khi không nhấn
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        HeaderText(
                            text = "Sign Up to",
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Inner Space",
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.Blue
                        )

                    }
                    Text(
                        text = "Embrace your emotions, understand yourself",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Khi người dùng nhấn vào một emoji, cập nhật cảm xúc cho ngày
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        ) {
                            EmojiItem(
                                icon = R.drawable.happy,
                                label = "Happy",
                                onEmojiClick = {},
                                Color(0xFFFFFAE6)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        ) {
                            EmojiItem(
                                icon = R.drawable.neutral,
                                label = "Neutral",
                                onEmojiClick = {},
                                Color(0xFFEAFBFE)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        ) {
                            EmojiItem(
                                icon = R.drawable.bored,
                                label = "Bored",
                                onEmojiClick = {},
                                Color(0xFFEDEDED)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        ) {
                            EmojiItem(
                                icon = R.drawable.sad,
                                label = "Sad",
                                onEmojiClick = {},
                                Color(0xFFB8D1F1)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        ) {
                            EmojiItem(
                                icon = R.drawable.angry,
                                label = "Angry",
                                onEmojiClick = {},
                                Color(0xFFFFD5CD)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Name Field
                    LoginTextField(
                        value = name,
                        onValueChange = onNameChange,
                        labelText = stringResource(R.string.name),
                        modifier = Modifier
                            .height(65.dp)
                            .width(250.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(isNameEmpty) {
                        Text(
                            text = stringResource(R.string.namenotfill),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Email Field
                    LoginTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        labelText = stringResource(R.string.email),
                        modifier = Modifier
                            .height(65.dp)
                            .width(250.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(isEmailEmpty) {
                        Text(
                            text = stringResource(R.string.emailnotfill),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    AnimatedVisibility(isEmailWrong) {
                        Text(
                            text = stringResource(R.string.emailwrong),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Password Field
                    LoginTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        labelText = stringResource(R.string.password),
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
                    AnimatedVisibility(isPasswordEmpty) {
                        Text(
                            text = stringResource(R.string.passwordnotfill),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    AnimatedVisibility(isPasswordTooShort) {
                        Text(
                            text = stringResource(R.string.passwordtooshort),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Confirm Password Field
                    LoginTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        labelText = stringResource(R.string.confirmpassword),
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
                    Spacer(Modifier.height(8.dp))
                    AnimatedVisibility(isConfirmPasswordEmpty) {
                        Text(
                            text = stringResource(R.string.confirmpasswordnotfill),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    AnimatedVisibility(isPasswordSame) {
                        Text(
                            text = stringResource(R.string.passwordnotsame),
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
                                1.dp,
                                Color(0xFF99c1ff),
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
                                    if (!isLoading) { // Chặn click khi đang tải
                                        isLoading = true
                                        FirebaseAuth.getInstance()
                                            .createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val user =
                                                        FirebaseAuth.getInstance().currentUser
                                                    // Cập nhật displayName
                                                    val profileUpdates =
                                                        UserProfileChangeRequest.Builder()
                                                            .setDisplayName(name) // Thiết lập displayName
                                                            .build()

                                                    user?.updateProfile(profileUpdates)
                                                        ?.addOnCompleteListener { updateTask ->
                                                            if (updateTask.isSuccessful) {
                                                                // Lưu thông tin người dùng vào Firestore
                                                                val userData = hashMapOf(
                                                                    "name" to name,
                                                                    "email" to email,
                                                                    // Không cần lưu password vào Firestore vì đã lưu trong Authentication
                                                                )
                                                                Firebase.firestore.collection("users")
                                                                    .document(user.uid)
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
                                                            isLoading = false
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
                            }
                        },
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF99c1ff),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.signup),
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(isSignUpFail) {
                        Text(
                            text = stringResource(R.string.signupfail),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
            // Sign In Row
            Row {
                Text(
                    text = stringResource(R.string.haveaccount),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TextButton(onClick = onLoginClick) {
                    Text(text = stringResource(R.string.signin))
                }
            }
        }
    }
}