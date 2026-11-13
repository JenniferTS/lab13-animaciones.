package com.tecsup.lab13

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
 * Ejercicio 3: animateDpAsState + Modifier.offset.
 * Un solo boton mueve y agranda los dos cuadros. Los modificadores estan en distinto orden:
 * en el A el offset va antes del fondo (se mueve todo) y en el B va despues (solo se mueve el contenido).
 */
@Composable
fun Ejercicio3() {
    var movido by remember { mutableStateOf(false) }
    val especificacion = spring<androidx.compose.ui.unit.Dp>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)

    val lado by animateDpAsState(if (movido) 120.dp else 70.dp, especificacion, label = "lado")
    val desplazX by animateDpAsState(if (movido) 24.dp else 0.dp, especificacion, label = "x")
    val desplazY by animateDpAsState(if (movido) 170.dp else 0.dp, especificacion, label = "y")

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { movido = !movido }) {
            Text(if (movido) "Volver" else "Mover y agrandar")
        }

        Row(Modifier.fillMaxWidth().padding(top = 20.dp)) {
            // A: offset -> size -> background. El fondo viaja junto con el cuadro.
            Column(Modifier.width(160.dp)) {
                Text("A: offset, size, background", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Box(Modifier.height(300.dp).fillMaxWidth()) {
                    Box(
                        Modifier
                            .offset(x = desplazX, y = desplazY)
                            .size(lado)
                            .background(Color(0xFF7B4FB5), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp) }
                }
            }
            // B: size -> background -> offset. El fondo se queda y solo se mueve lo que esta dentro.
            Column(Modifier.width(160.dp)) {
                Text("B: size, background, offset", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Box(Modifier.height(300.dp).fillMaxWidth()) {
                    Box(
                        Modifier
                            .size(lado)
                            .background(Color(0xFFFF8F3C), RoundedCornerShape(18.dp))
                            .offset(x = desplazX, y = desplazY)
                    ) { Text("B", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp) }
                }
            }
        }
    }
}
