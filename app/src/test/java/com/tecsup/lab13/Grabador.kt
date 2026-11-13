package com.tecsup.lab13

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.captureRoboImage
import java.io.File

/** Guarda cuadros del video (una imagen por cuadro) y un manifest con la duracion de cada uno y donde fue el toque. */
class Grabador(private val rule: AndroidComposeTestRule<*, *>, nombre: String) {
    private val dir = File(System.getProperty("frames.dir") ?: "build/frames", nombre).also { it.deleteRecursively(); it.mkdirs() }
    private var n = 0

    init {
        rule.mainClock.autoAdvance = false
        rule.mainClock.advanceTimeBy(300)
    }

    fun cuadro(dur: Double, tap: Offset? = null) {
        rule.waitForIdle()
        n++
        val f = File(dir, "%04d.png".format(n))
        rule.onRoot().captureRoboImage(f.path)
        File(dir, "manifest.txt").appendText("${f.name}|$dur|${tap?.x ?: -1}|${tap?.y ?: -1}\n")
    }

    /** Avanza el reloj de la app de a [paso] ms grabando un cuadro por paso. */
    fun animar(ms: Long, paso: Long = 40) {
        var t = 0L
        while (t < ms) { rule.mainClock.advanceTimeBy(paso); t += paso; cuadro(paso / 1000.0) }
    }

    fun centro(m: SemanticsNodeInteraction) = m.fetchSemanticsNode().boundsInRoot.center

    fun tocar(m: SemanticsNodeInteraction) {
        val c = centro(m)
        cuadro(0.35, c)
        m.performClick()
        rule.mainClock.advanceTimeBy(60)
        cuadro(0.1, c)
    }

    fun hay(texto: String) = rule.onAllNodesWithText(texto, substring = true).fetchSemanticsNodes().isNotEmpty()
}
