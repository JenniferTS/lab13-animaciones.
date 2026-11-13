package com.tecsup.lab13

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xxhdpi")
class JuegoTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    private fun centroX() = rule.onRoot().fetchSemanticsNode().boundsInRoot.center.x
    private fun gatoX(): Float? =
        rule.onAllNodesWithTag("gatoActivo").fetchSemanticsNodes().firstOrNull()?.boundsInRoot?.center?.x

    /** Espera a que el gatito pase por [objetivoDp] (respecto al centro) y lo suelta. */
    private fun soltarEn(g: Grabador, objetivoDp: Float, tapY: Float) {
        val dens = 3f
        val meta = centroX() + objetivoDp * dens
        var guarda = 0
        // primero avanza grabando cuadros hasta estar cerca
        while ((gatoX() ?: Float.MAX_VALUE).let { abs(it - meta) > 40 * dens } && guarda++ < 400) g.animar(40)
        // y despues con pasos chicos (sin grabar) hasta quedar casi encima
        var prev = gatoX() ?: meta
        guarda = 0
        while (guarda++ < 1500) {
            val x = gatoX() ?: break
            if (abs(x - meta) < 3 * dens) break
            rule.mainClock.advanceTimeBy(4)
        }
        g.cuadro(0.2, Offset(centroX(), tapY))
        rule.onNodeWithTag("cancha").performClick()
    }

    @Test fun juegoCompleto() {
        val g = Grabador(rule, "juego")
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Ejercicio final", substring = true))
        g.animar(600)
        g.cuadro(1.8)
        g.tocar(rule.onNodeWithText("Jugar"))
        g.animar(700)
        val tapY = 2100f
        val plan = listOf(0f, 1f, 30f, 0f, 2f, 0f, 1f, 0f, 0f, 2f, 28f, 0f, 1f, 0f, 0f, 90f, 0f, 1f, 0f, 0f, -34f, 0f, 1f, 0f)
        for (obj in plan) {
            soltarEn(g, obj, tapY)
            g.animar(1000, 50)
        }
        // para terminar: se deja caer a los gatitos lejos del centro hasta perder las 3 vidas
        var lado = 100f
        var intentos = 0
        while (!g.hay("Fin del juego") && intentos++ < 6) {
            soltarEn(g, lado, tapY)
            lado = -lado
            g.animar(1000, 50)
        }
        g.animar(1200, 50)
        g.cuadro(3.0)
    }
}
