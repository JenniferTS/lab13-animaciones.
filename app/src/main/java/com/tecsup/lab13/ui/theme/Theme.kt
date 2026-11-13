package com.tecsup.lab13.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Color(0xFF7B4FB5),
    onPrimary = Color.White,
    secondary = Color(0xFFFF8F3C),
    background = Color(0xFFFBF7FF),
    surface = Color.White
)

@Composable
fun Lab13Theme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, content = content)
}
