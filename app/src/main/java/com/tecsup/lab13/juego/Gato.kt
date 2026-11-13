package com.tecsup.lab13.juego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Colores de un gatito: el pelaje, el tono mas oscuro (rayas, cola, orejas) y la panza/patas. */
class ColorGato(val pelaje: Color, val oscuro: Color, val claro: Color)

val ColoresGato = listOf(
    ColorGato(Color(0xFFF5A04A), Color(0xFFD9772B), Color(0xFFFFD9A8)), // naranja
    ColorGato(Color(0xFF9EA7B3), Color(0xFF6F7885), Color(0xFFD8DEE6)), // gris
    ColorGato(Color(0xFFF3E5CF), Color(0xFFD2B48C), Color(0xFFFFF7EA)), // crema
    ColorGato(Color(0xFF55555F), Color(0xFF2E2E36), Color(0xFF8A8A96)), // negro
    ColorGato(Color(0xFFB7825A), Color(0xFF86573A), Color(0xFFE2BFA0)), // cafe
)

val AnchoGato: Dp = 120.dp
val AltoGato: Dp = 60.dp

/** Un gatito echado (de lado), dibujado con Canvas. Mira hacia la derecha. */
@Composable
fun GatoEchado(color: ColorGato, modifier: Modifier = Modifier) {
    Canvas(modifier.size(AnchoGato, AltoGato)) { dibujarGato(color) }
}


fun DrawScope.dibujarGato(c: ColorGato) {
    val w = size.width
    val h = size.height

    // cola enroscada detras del cuerpo
    val cola = Path().apply {
        moveTo(w * 0.16f, h * 0.84f)
        cubicTo(-w * 0.07f, h * 0.95f, -w * 0.03f, h * 0.40f, w * 0.09f, h * 0.46f)
    }
    drawPath(cola, c.oscuro, style = Stroke(width = h * 0.17f, cap = StrokeCap.Round))

    // cuerpo (como una hogaza)
    drawRoundRect(
        c.pelaje,
        topLeft = Offset(w * 0.08f, h * 0.36f),
        size = Size(w * 0.78f, h * 0.62f),
        cornerRadius = CornerRadius(h * 0.31f)
    )
    // rayas del lomo
    for (k in 0..2) {
        val x = w * (0.24f + 0.13f * k)
        drawLine(c.oscuro, Offset(x, h * 0.39f), Offset(x - w * 0.02f, h * 0.55f), strokeWidth = h * 0.055f, cap = StrokeCap.Round)
    }
    // patas delanteras
    drawOval(c.claro, topLeft = Offset(w * 0.56f, h * 0.80f), size = Size(w * 0.20f, h * 0.18f))
    drawOval(c.claro, topLeft = Offset(w * 0.70f, h * 0.80f), size = Size(w * 0.20f, h * 0.18f))

    // orejas
    val orejaIzq = Path().apply {
        moveTo(w * 0.68f, h * 0.30f); lineTo(w * 0.70f, h * 0.02f); lineTo(w * 0.80f, h * 0.20f); close()
    }
    val orejaDer = Path().apply {
        moveTo(w * 0.80f, h * 0.20f); lineTo(w * 0.90f, h * 0.02f); lineTo(w * 0.93f, h * 0.32f); close()
    }
    drawPath(orejaIzq, c.pelaje); drawPath(orejaDer, c.pelaje)
    val adentroIzq = Path().apply {
        moveTo(w * 0.705f, h * 0.24f); lineTo(w * 0.715f, h * 0.09f); lineTo(w * 0.775f, h * 0.20f); close()
    }
    val adentroDer = Path().apply {
        moveTo(w * 0.825f, h * 0.20f); lineTo(w * 0.885f, h * 0.09f); lineTo(w * 0.905f, h * 0.27f); close()
    }
    drawPath(adentroIzq, c.claro); drawPath(adentroDer, c.claro)

    // cabeza
    drawCircle(c.pelaje, radius = h * 0.31f, center = Offset(w * 0.81f, h * 0.55f))
    // ojitos cerrados (el gatito esta dormido)
    val ojo = Stroke(width = h * 0.04f, cap = StrokeCap.Round)
    drawArc(c.oscuro, 200f, 140f, false, Offset(w * 0.725f, h * 0.47f), Size(w * 0.06f, h * 0.10f), style = ojo)
    drawArc(c.oscuro, 200f, 140f, false, Offset(w * 0.835f, h * 0.47f), Size(w * 0.06f, h * 0.10f), style = ojo)
    // nariz y boca
    drawCircle(Color(0xFFE8788A), radius = h * 0.035f, center = Offset(w * 0.81f, h * 0.62f))
    drawLine(c.oscuro, Offset(w * 0.81f, h * 0.65f), Offset(w * 0.79f, h * 0.70f), strokeWidth = h * 0.025f, cap = StrokeCap.Round)
    drawLine(c.oscuro, Offset(w * 0.81f, h * 0.65f), Offset(w * 0.83f, h * 0.70f), strokeWidth = h * 0.025f, cap = StrokeCap.Round)
    // bigotes
    for (dy in listOf(-0.02f, 0.03f)) {
        drawLine(c.oscuro, Offset(w * 0.86f, h * (0.62f + dy)), Offset(w * 0.96f, h * (0.58f + dy * 2)), strokeWidth = h * 0.015f)
        drawLine(c.oscuro, Offset(w * 0.76f, h * (0.62f + dy)), Offset(w * 0.66f, h * (0.58f + dy * 2)), strokeWidth = h * 0.015f)
    }
}
