package com.tecsup.lab13

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Una pantalla del laboratorio: lo que se ve en el menu y el composable que se abre. */
class Pantalla(val titulo: String, val detalle: String, val contenido: @Composable () -> Unit)

private val pantallas = listOf(
    Pantalla("Ejercicio 1", "AnimatedVisibility: mostrar y ocultar un cuadro") { Ejercicio1() },
)

@Composable
fun Lab13App() {
    var actual by remember { mutableStateOf<Int?>(null) }
    BackHandler(enabled = actual != null) { actual = null }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val seleccionada = actual?.let { pantallas[it] }
        if (seleccionada == null) {
            Menu(onElegir = { actual = it })
        } else {
            Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { actual = null }) { Text("‹ Volver") }
                Text(seleccionada.titulo, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            seleccionada.contenido()
        }
    }
}

@Composable
private fun Menu(onElegir: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Laboratorio 13", fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
        Text("Animaciones en Compose", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(
            "Elige un ejercicio para verlo funcionando.",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(pantallas) { i, p ->
                Card(
                    Modifier.fillMaxWidth().clickable { onElegir(i) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(p.titulo, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                        Text(p.detalle, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}
