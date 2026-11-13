package com.tecsup.lab13

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
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

/**
 * Ejercicio 1: AnimatedVisibility.
 * Un boton alterna la visibilidad de un cuadro de color con fadeIn y fadeOut.
 * El interruptor agrega expandVertically/shrinkVertically para ver como se mueve
 * el resto de la pantalla.
 */
@Composable
fun Ejercicio1() {
    var visible by remember { mutableStateOf(false) }
    var animarTamano by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar cuadro" else "Mostrar cuadro")
        }

        Row(
            Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Switch(checked = animarTamano, onCheckedChange = { animarTamano = it })
            Text("Animar también el tamaño", fontSize = 14.sp)
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)) + if (animarTamano) expandVertically(tween(700)) else EnterTransition.None,
            exit = fadeOut(tween(700)) + if (animarTamano) shrinkVertically(tween(700)) else ExitTransition.None
        ) {
            Box(
                Modifier
                    .size(170.dp)
                    .background(Color(0xFFFF8F3C), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("¡Hola!", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(20.dp))
        Box(
            Modifier
                .background(Color(0xFFEDE4F7), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Text(
                "Este texto está debajo del cuadro. Mira cómo se acomoda cuando el cuadro aparece o desaparece.",
                fontSize = 14.sp
            )
        }
    }
}
