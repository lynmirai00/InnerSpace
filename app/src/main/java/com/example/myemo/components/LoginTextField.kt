package com.example.myemo.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.myemo.PreferenceManager

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange:(String) -> Unit,
    labelText:String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable (() -> Unit))? = null
){
    val context = LocalContext.current // Lấy context
    val preferenceManager = remember { PreferenceManager(context) }
    val backgroundColor = remember { mutableStateOf(Color(preferenceManager.getBackgroundColor())) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText, style = MaterialTheme.typography.bodySmall)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(30),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent, // Màu nền khi focus
            unfocusedContainerColor = Color.Transparent, // Màu nền khi không focus
            focusedIndicatorColor = backgroundColor.value, // Màu đường viền khi focus
            unfocusedIndicatorColor = backgroundColor.value // Màu đường viền khi không focus
        )
    )
}










