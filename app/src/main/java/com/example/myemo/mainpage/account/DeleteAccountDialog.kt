package com.example.myemo.mainpage.account

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteAccountDialog(
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            title = { Text(text = "Confirm Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        onDeleteConfirmed()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFd1e9f6) // Nền trắng của button
                    ),
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}
