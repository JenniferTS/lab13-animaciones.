package com.tecsup.lab13

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xxhdpi")
class EjerciciosTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    private fun abrir(g: Grabador, ejercicio: String) {
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText(ejercicio))
        g.animar(400)
        g.cuadro(0.8)
    }

    @Test fun ejercicio1() {
        val g = Grabador(rule, "ejercicio1")
        abrir(g, "Ejercicio 1")
        g.tocar(rule.onNodeWithText("Mostrar cuadro"))
        g.animar(1000)
        g.cuadro(1.2)
        g.tocar(rule.onNodeWithText("Ocultar cuadro"))
        g.animar(1000)
        g.cuadro(1.0)
        // ahora tambien se anima el tamano y el texto de abajo se mueve suave
        g.tocar(rule.onNode(isToggleable()))
        g.animar(300)
        g.tocar(rule.onNodeWithText("Mostrar cuadro"))
        g.animar(1000)
        g.cuadro(1.2)
        g.tocar(rule.onNodeWithText("Ocultar cuadro"))
        g.animar(1000)
        g.cuadro(1.5)
    }

    @Test fun ejercicio2() {
        val g = Grabador(rule, "ejercicio2")
        abrir(g, "Ejercicio 2")
        g.tocar(rule.onNodeWithText("Cambiar a verde"))
        g.animar(1500)
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Cambiar a azul"))
        g.animar(1500)
        g.cuadro(0.8)
        g.tocar(rule.onNodeWithText("spring"))
        g.animar(300)
        g.tocar(rule.onNodeWithText("Cambiar a verde"))
        g.animar(2500)
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Cambiar a azul"))
        g.animar(2500)
        g.cuadro(1.2)
    }

    @Test fun ejercicio3() {
        val g = Grabador(rule, "ejercicio3")
        abrir(g, "Ejercicio 3")
        g.tocar(rule.onNodeWithText("Mover y agrandar"))
        g.animar(2200)
        g.cuadro(1.5)
        g.tocar(rule.onNodeWithText("Volver"))
        g.animar(2200)
        g.cuadro(1.0)
    }

    @Test fun ejercicio4() {
        val g = Grabador(rule, "ejercicio4")
        abrir(g, "Ejercicio 4")
        g.cuadro(1.2)
        g.tocar(rule.onNodeWithText("Contenido"))
        g.animar(1400)
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Error"))
        g.animar(1400)
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Simular una carga"))
        g.animar(3600)
        g.cuadro(1.0)
        g.tocar(rule.onNodeWithText("Simular una carga"))
        g.animar(3600)
        g.cuadro(1.2)
    }
}
