package com.example.myemo.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.myemo.components.HeaderText
import com.example.myemo.components.LoginTextField
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun SignUp(onSignUpClick: (String?) -> Unit, onLoginClick: () -> Unit,) {
    val (name, onNameChange) = rememberSaveable { mutableStateOf("") }
    val (email, onEmailChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, onConfirmPasswordChange) = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var isPasswordSame by remember { mutableStateOf(false) }
    var isSignUpFail by remember { mutableStateOf(false) }
    val isFieldsNotEmpty = name.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty() &&
            confirmPassword.isNotEmpty()
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderText(
                text = "Sign Up",
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(alignment = Alignment.Start)
            )
            LoginTextField(
                value = name,
                onValueChange = onNameChange,
                labelText = "Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            LoginTextField(
                value = email,
                onValueChange = onEmailChange,
                labelText = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            LoginTextField(
                value = password,
                onValueChange = onPasswordChange,
                labelText = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(16.dp))
            LoginTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                leadingIcon = Icons.Default.Lock,
                labelText = "Confirm Password",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    isPasswordSame = password != confirmPassword
                    if (!isPasswordSame) {
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
                                                Toast.makeText(
                                                    context,
                                                    "Sign Up Failed: ${e.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                } else {
//                                    Toast.makeText(
//                                        context,
//                                        "Sign Up Failed: ${task.exception?.message}",
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                    isSignUpFail = true
                                    Log.d("SignUpScreen", "Sign Up Failed: ${task.exception?.message}")
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFieldsNotEmpty,
            ) {
                Text("Sign Up")
            }
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(isPasswordSame) {
                Text(
                    "Password is not matching",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            AnimatedVisibility(isSignUpFail) {
                Text(
                    "This email is exist",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(16.dp))
            val singTx = "Sign In"
            val signInAnnotation = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("Already have an account?")
                }
                append("  ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(singTx, singTx)
                    append(singTx)
                }


            }
            ClickableText(
                signInAnnotation,
            ) { offset ->
                signInAnnotation.getStringAnnotations(offset, offset).forEach {
                    if (it.tag == singTx) {
                        Toast.makeText(context, "Sign in Clicked", Toast.LENGTH_SHORT).show()
                        onLoginClick()
                    }
                }
            }
        }
    }
}