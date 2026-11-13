package com.tecsup.lab13

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF2F6FED)
private val Verde = Color(0xFF2EB872)

private enum class Tipo(val etiqueta: String) { TWEEN("tween"), SPRING("spring") }

/**
 * Ejercicio 2: animateColorAsState.
 * El cuadro de la izquierda anima el color y el de la derecha lo cambia de golpe,
 * para poder comparar. Los chips cambian la especificacion de la animacion.
 */
@Composable
fun Ejercicio2() {
    var esVerde by remember { mutableStateOf(false) }
    var tipo by remember { mutableStateOf(Tipo.TWEEN) }
    val destino = if (esVerde) Verde else Azul

    val colorAnimado by animateColorAsState(
        targetValue = destino,
        animationSpec = when (tipo) {
            Tipo.TWEEN -> tween(durationMillis = 1200)
            Tipo.SPRING -> spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow)
        },
        label = "colorCuadro"
    )

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { esVerde = !esVerde }) {
            Text(if (esVerde) "Cambiar a azul" else "Cambiar a verde")
        }

        Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Tipo.entries.forEach { t ->
                FilterChip(
                    selected = tipo == t,
                    onClick = { tipo = t },
                    label = { Text(t.etiqueta) }
                )
            }
        }

        Row(
            Modifier.padding(top = 36.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CuadroColor("animateColorAsState (${tipo.etiqueta})", colorAnimado)
            CuadroColor("Cambio directo", destino)
        }
    }
}

@Composable
private fun CuadroColor(titulo: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.size(width = 150.dp, height = 210.dp)) {
        Box(Modifier.size(150.dp).background(color, RoundedCornerShape(24.dp)))
        Text(titulo, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 10.dp))
    }
}
