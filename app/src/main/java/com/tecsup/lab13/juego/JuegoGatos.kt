package com.tecsup.lab13.juego

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

// ---- Reglas del juego (todas las medidas en dp) ----
private const val PASO = 44f          // cuanto sube la torre por cada gatito
private const val PERFECTO = 10f      // distancia maxima al centro para un acierto perfecto
private const val BIEN = 48f          // distancia maxima para que el gatito no se resbale
private const val SPAWN_Y = 170f      // donde aparece el gatito que se desliza (arriba)
private const val TOPE_TORRE = 340f   // la camara baja para que la punta de la torre no pase de aqui
private const val VIDAS = 3

enum class Fase { MENU, JUGANDO, FIN }

class GatoTorre(val x: Float, val color: ColorGato, val espejo: Boolean)
class Mensaje(val id: Int, val texto: String, val color: Color)

class EstadoJuego {
    val torre = mutableStateListOf<GatoTorre>()
    var fase by mutableStateOf(Fase.MENU)
    var puntos by mutableIntStateOf(0)
    var mejor by mutableIntStateOf(0)
    var vidas by mutableIntStateOf(VIDAS)
    var combo by mutableIntStateOf(0)
    var idGato by mutableIntStateOf(0)           // cambia con cada gatito nuevo para reiniciar el deslizamiento
    var cayendo by mutableStateOf(false)         // ya se soltó y todavía no termina de caer
    var activo by mutableStateOf(false)          // se dibuja el gatito que se mueve arriba
    var mensaje by mutableStateOf<Mensaje?>(null)
    private var contadorMensajes = 0

    init { reiniciarTorre() }

    private fun reiniciarTorre() {
        torre.clear()
        torre.add(GatoTorre(0f, ColoresGato[0], false)) // el gatito base ya está dormido en la almohada
    }

    fun empezar() {
        reiniciarTorre()
        puntos = 0; vidas = VIDAS; combo = 0; cayendo = false; mensaje = null
        fase = Fase.JUGANDO
        idGato++
    }

    fun avisar(texto: String, color: Color) { mensaje = Mensaje(++contadorMensajes, texto, color) }
}

private val Cielos = listOf(Color(0xFFFFD9B0), Color(0xFF8FD3F4), Color(0xFFFF9E80), Color(0xFF283593))

/** Ejercicio final: Torre de gatitos. Combina AnimatedVisibility, animate*AsState, AnimatedContent y Animatable. */
@Composable
fun JuegoGatos() {
    val juego = remember { EstadoJuego() }

    // El cielo cambia de color cada 6 gatitos (amanecer, dia, atardecer, noche)
    val indiceCielo = (juego.torre.size / 6) % Cielos.size
    val cielo by animateColorAsState(Cielos[indiceCielo], tween(1600), label = "cielo")
    val textoColor by animateColorAsState(if (indiceCielo == 3) Color.White else Color(0xFF2B2140), tween(1600), label = "texto")

    Box(Modifier.fillMaxSize().background(cielo)) {
        Cancha(juego, textoColor)

        AnimatedContent(
            targetState = juego.fase,
            transitionSpec = { fadeIn(tween(500, delayMillis = 100)) togetherWith fadeOut(tween(250)) },
            modifier = Modifier.fillMaxSize(),
            label = "faseJuego"
        ) { fase ->
            when (fase) {
                Fase.MENU -> Panel(
                    titulo = "Torre de gatitos",
                    detalle = "Los gatitos se deslizan de lado a lado. Toca la pantalla para soltarlos y apunta al centro de la torre.",
                    boton = "Jugar",
                    onClick = { juego.empezar() }
                )
                Fase.JUGANDO -> Box(Modifier.fillMaxSize())
                Fase.FIN -> Panel(
                    titulo = "¡Fin del juego!",
                    detalle = "Gatitos apilados: ${juego.torre.size - 1}\nPuntos: ${juego.puntos}\nMejor puntaje: ${juego.mejor}",
                    boton = "Jugar de nuevo",
                    onClick = { juego.empezar() }
                )
            }
        }
    }
}

@Composable
private fun Panel(titulo: String, detalle: String, boton: String, onClick: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0x66000000)), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .padding(28.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(titulo, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A2A5C), textAlign = TextAlign.Center)
            Text(detalle, fontSize = 15.sp, color = Color(0xFF3A2A5C), textAlign = TextAlign.Center)
            Button(onClick = onClick) { Text(boton) }
        }
    }
}

@Composable
private fun Cancha(juego: EstadoJuego, textoColor: Color) {
    val scope = rememberCoroutineScope()
    val xGato = remember { Animatable(0f) }      // posicion horizontal del gatito que se mueve (0 = centro)
    val yCaida = remember { Animatable(0f) }     // cuanto ha bajado desde que lo soltaron
    val xExtra = remember { Animatable(0f) }     // desplazamiento extra cuando se resbala
    val rotGato = remember { Animatable(0f) }    // giro cuando se cae
    val rebote = remember { Animatable(1f) }     // aplastamiento al aterrizar

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val ancho = maxWidth.value
        val alto = maxHeight.value
        // toda la pantalla es el boton para soltar al gatito
        Box(
            Modifier
                .fillMaxSize()
                .testTag("cancha")
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    soltar(juego, scope, xGato, yCaida, xExtra, rotGato, rebote, alto, ancho)
                }
        )
        val sueloTop = alto - 120f
        val rango = (ancho / 2f - AnchoGato.value / 2f - 8f).coerceAtLeast(60f)
        val n = juego.torre.size

        val camara by animateDpAsState(camaraObjetivo(n, sueloTop).dp, tween(600, easing = FastOutSlowInEasing), label = "camara")
        val inclinacion by animateFloatAsState(
            targetValue = (juego.torre.map { it.x }.average().toFloat() / 9f).coerceIn(-6f, 6f),
            animationSpec = spring(dampingRatio = 0.35f, stiffness = Spring.StiffnessLow),
            label = "inclinacion"
        )
        val respiracion by rememberInfiniteTransition(label = "resp")
            .animateFloat(1f, 1.04f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "respira")
        val pulso by rememberInfiniteTransition(label = "pulso")
            .animateFloat(0.45f, 0.95f, infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "pulsoGuia")

        // Cada vez que sale un gatito nuevo se reinicia su deslizamiento (cada vez mas rapido)
        LaunchedEffect(juego.idGato) {
            if (juego.fase == Fase.JUGANDO) {
                xGato.snapTo(-rango); yCaida.snapTo(0f); xExtra.snapTo(0f); rotGato.snapTo(0f)
                juego.cayendo = false
                juego.activo = true
                val duracion = (1000 - n * 25).coerceAtLeast(520)
                xGato.animateTo(rango, infiniteRepeatable(tween(duracion, easing = LinearEasing), RepeatMode.Reverse))
            }
        }
        // El mensaje flotante desaparece solo
        LaunchedEffect(juego.mensaje?.id) {
            if (juego.mensaje != null) { delay(900); juego.mensaje = null }
        }

        // Guia para apuntar: linea al centro y franjas de la zona buena y la perfecta
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val yObjetivo = (sueloTop - AltoGato.value - n * PASO + camara.value + AltoGato.value - 4f).dp.toPx()
            if (juego.fase == Fase.JUGANDO) {
                drawLine(Color.White.copy(alpha = 0.55f), Offset(cx, (SPAWN_Y + AltoGato.value + 6f).dp.toPx()), Offset(cx, yObjetivo),
                    strokeWidth = 3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 16f)))
                drawRoundRect(Color.White.copy(alpha = 0.35f), Offset(cx - BIEN.dp.toPx(), yObjetivo), Size(BIEN.dp.toPx() * 2, 10.dp.toPx()), CornerRadius(5.dp.toPx()))
                drawRoundRect(Color(0xFFFFC107).copy(alpha = pulso), Offset(cx - PERFECTO.dp.toPx(), yObjetivo - 1.dp.toPx()), Size(PERFECTO.dp.toPx() * 2, 12.dp.toPx()), CornerRadius(6.dp.toPx()))
            }
        }

        // Suelo y almohada (suben y bajan con la camara)
        Box(
            Modifier
                .offset { IntOffset(0, (sueloTop + 40f + camara.value).dp.roundToPx()) }
                .fillMaxWidth().height(400.dp).background(Color(0xFF9C6B4E))
        )
        Box(
            Modifier
                .offset { IntOffset(((ancho / 2f) - 120f).dp.roundToPx(), (sueloTop + camara.value).dp.roundToPx()) }
                .size(240.dp, 44.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFE88FB4))
        )

        // La torre se inclina un poco segun que tan descentrados quedaron los gatitos
        Box(
            Modifier.fillMaxSize().graphicsLayer {
                rotationZ = inclinacion
                transformOrigin = TransformOrigin(0.5f, (sueloTop + 40f) / alto)
            }
        ) {
            juego.torre.forEachIndexed { i, g ->
                val esUltimo = i == juego.torre.lastIndex
                Box(
                    Modifier
                        .offset { IntOffset(((ancho / 2f) + g.x - AnchoGato.value / 2f).dp.roundToPx(), (sueloTop - AltoGato.value - i * PASO + camara.value).dp.roundToPx()) }
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 1f)
                            val aplasta = if (esUltimo) rebote.value else 1f
                            scaleY = aplasta * (if (esUltimo) respiracion else 1f)
                            scaleX = (2f - aplasta) * (if (g.espejo) -1f else 1f)
                        }
                ) { GatoEchado(g.color) }
            }
        }

        // El gatito que se esta deslizando (o cayendo)
        if (juego.fase != Fase.MENU && juego.activo) {
            val color = ColoresGato[n % ColoresGato.size]
            Box(
                Modifier
                    .offset { IntOffset(((ancho / 2f) + xGato.value + xExtra.value - AnchoGato.value / 2f).dp.roundToPx(), (SPAWN_Y + yCaida.value).dp.roundToPx()) }
                    .testTag("gatoActivo")
                    .graphicsLayer { rotationZ = rotGato.value; scaleX = if (n % 2 == 1) -1f else 1f }
            ) { GatoEchado(color) }
        }

        // Marcador, vidas y combo
        Column(Modifier.fillMaxWidth().padding(top = 18.dp, start = 18.dp, end = 18.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(VIDAS) { k ->
                        val viva = k < juego.vidas
                        val escala by animateFloatAsState(if (viva) 1f else 0.55f, spring(Spring.DampingRatioMediumBouncy), label = "vida$k")
                        val colorPata by animateColorAsState(if (viva) Color(0xFFE8788A) else Color(0x55000000), tween(400), label = "colorVida$k")
                        Huellita(colorPata, Modifier.size(28.dp).graphicsLayer { scaleX = escala; scaleY = escala })
                    }
                }
                Box(Modifier.weight(1f))
                Text("Mejor: ${juego.mejor}", fontSize = 14.sp, color = textoColor)
            }
            // El puntaje cambia con una animacion cada vez que suma
            AnimatedContent(
                targetState = juego.puntos,
                transitionSpec = {
                    (slideInVertically { -it } + fadeIn(tween(250))) togetherWith (slideOutVertically { it } + fadeOut(tween(250)))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                label = "puntaje"
            ) { p ->
                Text("$p", fontSize = 44.sp, fontWeight = FontWeight.Bold, color = textoColor)
            }
            AnimatedVisibility(
                visible = juego.combo >= 2,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    "Combo x${juego.combo}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFC107),
                    modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xCC2B2140)).padding(horizontal = 12.dp, vertical = 3.dp)
                )
            }
        }

        // Mensaje flotante (¡Perfecto!, ¡Bien!, ¡Se cayó!)
        var ultimoMensaje by remember { mutableStateOf<Mensaje?>(null) }
        if (juego.mensaje != null) ultimoMensaje = juego.mensaje
        AnimatedVisibility(
            visible = juego.mensaje != null,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 150.dp),
            enter = scaleIn(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)) + fadeIn(tween(150)),
            exit = fadeOut(tween(300)) + slideOutVertically { -it / 2 }
        ) {
            val m = ultimoMensaje
            Text(
                m?.texto ?: "", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = m?.color ?: Color.White,
                modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(Color(0xCC2B2140)).padding(horizontal = 18.dp, vertical = 6.dp)
            )
        }

        if (juego.fase == Fase.JUGANDO) {
            Text(
                "Toca para soltar al gatito",
                Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp).width(300.dp),
                textAlign = TextAlign.Center, fontSize = 13.sp, color = Color.White
            )
        }
    }
}

/** Cuanto debe bajar la camara cuando la torre ya tiene [n] gatitos. */
private fun camaraObjetivo(n: Int, sueloTop: Float): Float {
    val puntaTorre = sueloTop - AltoGato.value - (n - 1) * PASO
    return (TOPE_TORRE - puntaTorre).coerceAtLeast(0f)
}

private fun soltar(
    juego: EstadoJuego, scope: kotlinx.coroutines.CoroutineScope,
    xGato: Animatable<Float, *>, yCaida: Animatable<Float, *>, xExtra: Animatable<Float, *>,
    rotGato: Animatable<Float, *>, rebote: Animatable<Float, *>, alto: Float, ancho: Float
) {
    if (juego.fase != Fase.JUGANDO || juego.cayendo || !juego.activo) return
    juego.cayendo = true
    scope.launch {
        xGato.stop()
        val x = xGato.value
        val n = juego.torre.size
        val sueloTop = alto - 120f
        val destino = sueloTop - AltoGato.value - n * PASO + camaraObjetivo(n, sueloTop) - SPAWN_Y
        yCaida.animateTo(destino, tween(360, easing = FastOutLinearInEasing))

        val distancia = abs(x)
        if (distancia <= BIEN) {
            val perfecto = distancia <= PERFECTO
            if (perfecto) xGato.animateTo(0f, tween(100))
            // el gatito deja de dibujarse arriba y pasa a la torre en el mismo cuadro
            juego.activo = false
            juego.torre.add(GatoTorre(if (perfecto) 0f else x, ColoresGato[n % ColoresGato.size], n % 2 == 1))
            if (perfecto) {
                juego.combo++
                juego.puntos += 20 + 10 * (juego.combo - 1)
                juego.avisar(if (juego.combo >= 2) "¡Perfecto! x${juego.combo}" else "¡Perfecto!", Color(0xFFFFC107))
            } else {
                juego.combo = 0
                juego.puntos += 10
                juego.avisar("¡Bien!", Color(0xFF5BE39B))
            }
            launch { rebote.snapTo(0.72f); rebote.animateTo(1f, spring(dampingRatio = 0.35f, stiffness = 250f)) }
            juego.mejor = maxOf(juego.mejor, juego.puntos)
            juego.idGato++
        } else {
            // se resbala: gira y se cae por un costado
            val lado = if (x >= 0) 1f else -1f
            juego.combo = 0
            juego.vidas--
            juego.avisar("¡Se cayó!", Color(0xFFFF6B7A))
            launch { rotGato.animateTo(lado * 110f, tween(700)) }
            launch { xExtra.animateTo(lado * (ancho / 2f), tween(700)) }
            yCaida.animateTo(yCaida.value + 700f, tween(700, easing = FastOutLinearInEasing))
            juego.activo = false
            if (juego.vidas <= 0) {
                juego.mejor = maxOf(juego.mejor, juego.puntos)
                delay(400)
                juego.fase = Fase.FIN
            } else {
                juego.idGato++
            }
        }
    }
}

@Composable
private fun Huellita(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        drawOval(color, Offset(w * 0.22f, w * 0.48f), Size(w * 0.56f, w * 0.44f))
        for ((px, py) in listOf(0.10f to 0.38f, 0.32f to 0.14f, 0.56f to 0.14f, 0.78f to 0.38f)) {
            drawCircle(color, w * 0.10f, Offset(w * (px + 0.06f), w * (py + 0.06f)))
        }
    }
}
