package com.tecsup.lab13

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class Estado { CARGANDO, CONTENIDO, ERROR }

/**
 * Ejercicio 4: AnimatedContent.
 * Tres estados (Cargando, Contenido y Error) que cambian con fadeIn/fadeOut y tiempos propios:
 * el contenido que sale se va en 300 ms y el que entra aparece en 700 ms despues de una pequena espera.
 */
@Composable
fun Ejercicio4() {
    var estado by remember { mutableStateOf(Estado.CARGANDO) }
    var intento by remember { mutableStateOf(0) }

    // Simula una carga: tarda 2 segundos y los intentos pares fallan
    LaunchedEffect(intento) {
        if (intento > 0) {
            estado = Estado.CARGANDO
            delay(2000)
            estado = if (intento % 2 == 0) Estado.ERROR else Estado.CONTENIDO
        }
    }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { estado = Estado.CARGANDO }) { Text("Cargando") }
            OutlinedButton(onClick = { estado = Estado.CONTENIDO }) { Text("Contenido") }
            OutlinedButton(onClick = { estado = Estado.ERROR }) { Text("Error") }
        }
        Button(onClick = { intento++ }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Simular una carga")
        }

        Box(
            Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(280.dp)
                .background(Color(0xFFF1EAFB), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = estado,
                transitionSpec = {
                    fadeIn(tween(durationMillis = 700, delayMillis = 300)) togetherWith fadeOut(tween(300))
                },
                label = "estadoPantalla"
            ) { actual ->
                when (actual) {
                    Estado.CARGANDO -> Mensaje(titulo = "Cargando...", detalle = "Buscando a los gatitos") {
                        CircularProgressIndicator(color = Color(0xFF7B4FB5))
                    }
                    Estado.CONTENIDO -> Mensaje(titulo = "¡Listo!", detalle = "Se encontraron 12 gatitos echados en la torre") {
                        Text("12", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2EB872))
                    }
                    Estado.ERROR -> Mensaje(titulo = "Algo salió mal", detalle = "No se pudo cargar. Prueba de nuevo.") {
                        Text("!", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD93B4B))
                    }
                }
            }
        }
    }
}

@Composable
private fun Mensaje(titulo: String, detalle: String, icono: @Composable () -> Unit) {
    Column(
        Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icono()
        Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(detalle, fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}
