package com.example.myemo.login

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.myemo.R
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(onLoginClick: (String?) -> Unit, onSignUpClick: () -> Unit) {
    val (email, setEmail) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val isFieldsEmpty = email.isNotEmpty() && password.isNotEmpty()
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoginFail by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // Trắng
                        Color(0xFFededfe), // Tím nhạt
                        Color(0xFFd7ddfe), // Xanh nhạt
                        Color(0xFFbcccff), // Xanh nhạt hơn
                        Color(0xFF99c1ff), // Xanh dương nhạt
                        Color(0xFF96befc), // Xanh dương
                        Color(0xFF92baf8), // Xanh dương đậm hơn
                        Color(0xFF8fb7f5), // Xanh đậm
                        Color(0xFFaabcec), // Xanh đậm vừa
                        Color(0xFFbdc3e4), // Xám nhạt
                        Color(0xFFcbcbdb), // Xám
                        Color(0xFFd4d4d4)  // Xám sáng
                    ),
                    start = Offset(0f, 0f),  // Điểm bắt đầu từ góc trái trên
                    end = Offset(1000f, 1000f) // Điểm kết thúc ở góc phải dưới
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HeaderText(
                text = "Login",
                modifier = Modifier.padding(vertical = 16.dp)
                    .align(alignment = Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            LoginTextField(
                value = email,
                onValueChange = setEmail,
                labelText = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            LoginTextField(
                value = password,
                onValueChange = setPassword,
                labelText = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.Close
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    // Xử lý đăng nhập tại đây
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Đăng nhập thành công
                                val user = FirebaseAuth.getInstance().currentUser
                                user?.let {
                                    Log.d("LoginScreen", "User: ${it.displayName}, ${it.email}")
                                    onLoginClick(it.email)
//                                Toast.makeText(context, "${it.displayName}, ${it.email}", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                // Đăng nhập thất bại
//                                Toast.makeText(context, "Email or Password incorrect!", Toast.LENGTH_LONG).show()
                                isLoginFail = true
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFieldsEmpty
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(isLoginFail) {
                Text(
                    "Email or Password incorrect!",
                    color = MaterialTheme.colorScheme.error,
                )
            }

            AlternativeLoginOptions(
                onIconClick = { index ->
                    when (index) {
                        0 -> {
                            Toast.makeText(context, "Instagram Login Click", Toast.LENGTH_SHORT).show()
                        }

                        1 -> {
                            Toast.makeText(context, "Github Login Click", Toast.LENGTH_SHORT).show()

                        }

                        2 -> {
                            Toast.makeText(context, "Google Login Click", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onSignUpClick = onSignUpClick,
                modifier = Modifier.fillMaxSize()
                    .wrapContentSize(align = Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun AlternativeLoginOptions(
    onIconClick: (index: Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconList = listOf(
        R.drawable.icon_instagram,
        R.drawable.icon_github,
        R.drawable.icon_google,
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Or Sign in With")
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            iconList.forEachIndexed { index, iconResId ->
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "alternative Login",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onIconClick(index)
                        }.clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp))
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an Account?")
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up")
            }
        }
    }

}


