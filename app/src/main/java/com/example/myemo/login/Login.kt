package com.example.myemo.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import com.example.myemo.PreferenceManager
import com.example.myemo.R
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.example.myemo.mainpage.home.EmojiItem
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@SuppressLint("AutoboxingStateCreation")
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
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }
    var selectedFlag by remember { mutableStateOf(preferenceManager.getSelectedFlag()) }
    var expanded by remember { mutableStateOf(false) } // Quản lý trạng thái của menu
    val selectedLanguage =
        remember { mutableStateOf(preferenceManager.getSelectedLanguage()) } // Ngôn ngữ mặc định là Japanese
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape) // Cắt ảnh thành hình tròn
                        .background(Color.LightGray, CircleShape) // Thay đổi nền nếu cần
                        .border(
                            2.dp,
                            Color.White,
                            CircleShape
                        ) // Thêm viền tròn xung quanh ảnh đại diện
                        .clickable(onClick = { expanded = true }),
                ) {
                    Image(
                        painter = painterResource(id = selectedFlag), // Hiển thị cờ được chọn
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center) // Canh ảnh vào giữa Box
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .wrapContentHeight() // Chiều cao vừa đủ để chứa nội dung
                            .wrapContentWidth() // Đặt chiều rộng menu
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .background(Color.White),
                        offset = DpOffset(
                            (-120).dp,
                            (-100).dp
                        ), // Điều chỉnh khoảng cách của menu nếu cần
                        shape = RoundedCornerShape(10.dp),
                        containerColor = Color.White,
                        shadowElevation = 0.dp // Loại bỏ bóng mờ nếu không cần
                    ) {
                        val flags = listOf(
                            R.drawable.japan to "Japanese",
                            R.drawable.usa to "English",
                            R.drawable.vietnam to "Vietnamese"
                        )

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            flags.forEach { (flag, countryName) ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Image(
                                                painter = painterResource(id = flag),
                                                contentDescription = countryName,
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = countryName,
                                                fontSize = 10.sp,
                                                color = Color.Black,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedFlag = flag // Cập nhật cờ được chọn
                                        expanded = false // Đóng menu
                                        // Cập nhật và lưu cờ vào SharedPreferences
                                        preferenceManager.saveSelectedFlag(flag) // Lưu cờ đã chọn
                                        // Cập nhật và lưu ngôn ngữ đã chọn
                                        selectedLanguage.value = when (countryName) {
                                            "Japanese" -> "ja"
                                            "English" -> "en"
                                            "Vietnamese" -> "vi"
                                            else -> "ja"
                                        }
                                        preferenceManager.saveSelectedLanguage(selectedLanguage.value) // Lưu vào SharedPreferences
                                        setLocale(context, selectedLanguage.value) // Áp dụng
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp)) // Đảm bảo hình dạng vuông
                    .background(Color.White) // Màu nền khi không nhấn
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        HeaderText(
                            text = "Sign In to",
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

                    // Email Field
                    LoginTextField(
                        value = email,
                        onValueChange = setEmail,
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
                    Spacer(modifier = Modifier.height(10.dp))

                    // Password Field
                    LoginTextField(
                        value = password,
                        onValueChange = setPassword,
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
                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Button
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
                            containerColor = Color.White, // Nền trắng của button
                            disabledContainerColor = Color.White // Đảm bảo màu không đổi khi bị vô hiệu hóa
                        ),
                        onClick = {
                            // Kiểm tra các trường email và password
                            isEmailEmpty = email.isEmpty()
                            isPasswordEmpty = password.isEmpty()

                            // Xử lý đăng nhập tại đây
                            if (isFieldsNotEmpty) {
                                if (!isLoading) { // Chặn click khi đang tải
                                    isLoading = true // Bắt đầu loading
                                    FirebaseAuth.getInstance()
                                        .signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Đăng nhập thành công
                                                val user =
                                                    FirebaseAuth.getInstance().currentUser
                                                user?.let {
                                                    Log.d(
                                                        "LoginScreen",
                                                        "User: ${it.displayName}, ${it.email}"
                                                    )
                                                    onLoginClick(it.email)
                                                }
                                            } else {
                                                isLoginFail = true
                                                Log.d(
                                                    "SignInScreen",
                                                    "Sign In Failed: ${task.exception?.message}"
                                                )
                                            }
                                            isLoading = false // Dừng loading khi hoàn tất
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
                                text = stringResource(R.string.login),
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(isLoginFail) {
                        Text(
                            text = stringResource(R.string.loginfail),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            //
            Row {
                Text(
                    text = stringResource(R.string.donthaveaccount),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TextButton(
                    onClick = onSignUpClick,
                ) {
                    Text(text = stringResource(R.string.signup))
                }
            }
        }
    }
}


@Suppress("DEPRECATION")
fun setLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale) // Đảm bảo layout thay đổi theo ngôn ngữ (nếu cần)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Cập nhật lại UI mà không cần phải khởi động lại Activity
    if (context is Activity) {
        context.recreate() // Tự động làm mới Activity
    }

    Log.d("LocaleCheck", "Current Locale: ${context.resources.configuration.locales[0]}")
}
