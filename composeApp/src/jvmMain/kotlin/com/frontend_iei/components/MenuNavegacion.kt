package com.frontend_iei.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frontend_iei.Screen

@Composable
fun MenuNavegacion(
    screenActual: Screen,
    onScreenSeleccionada: (Screen) -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { onScreenSeleccionada(Screen.Busqueda) },
            enabled = screenActual != Screen.Busqueda
        ) {
            Text("Búsqueda")
        }

        Button(
            onClick = { onScreenSeleccionada(Screen.Carga) },
            enabled = screenActual != Screen.Carga
        ) {
            Text("Carga de datos")
        }
    }
}
