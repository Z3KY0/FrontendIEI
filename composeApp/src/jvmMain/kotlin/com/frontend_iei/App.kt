package com.frontend_iei

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frontend_iei.components.Contenido
import com.frontend_iei.components.MenuNavegacion
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import frontendiei.composeapp.generated.resources.Res
import frontendiei.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {

    var screenActual by remember { mutableStateOf<Screen>(Screen.Busqueda) }

    Column () {
        MenuNavegacion(
            screenActual = screenActual,
            onScreenSeleccionada = { screenActual = it },
        )

        Divider()

        Contenido(screenActual)
    }
}