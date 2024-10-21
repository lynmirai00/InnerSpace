package com.example.myemo.mainpage.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myemo.components.LoginTextField

@Composable
fun ChangeNameDialog(
    onConfirm: (String) -> Unit, // Tham số cho mật khẩu cũ, mới, và xác nhận mật khẩu
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var isNewNameEmpty by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Change Name") },
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
                    value = newName,
                    onValueChange = { newName = it },
                    labelText = "New Name",
                    modifier = Modifier
                        .height(65.dp)
                        .width(250.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(isNewNameEmpty) {
                    Text(
                        "New Name is not filled!",
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
                    isNewNameEmpty = newName.isEmpty()
                    if (!isNewNameEmpty) {
                        onConfirm(newName)
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
        }
    )
}