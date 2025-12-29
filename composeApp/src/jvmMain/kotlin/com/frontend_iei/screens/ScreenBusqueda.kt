package com.frontend_iei.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend_iei.Screen
import com.frontend_iei.components.Contenido
import com.frontend_iei.components.MenuNavegacion
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun ScreenBusqueda() {

    Column ( (Modifier.padding(50.dp)) ) {

        Text("Buscador de Estaciones ITV", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(20.dp))

    }

}