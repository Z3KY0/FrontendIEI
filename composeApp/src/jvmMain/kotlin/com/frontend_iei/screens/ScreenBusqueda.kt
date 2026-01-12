package com.frontend_iei.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class EstacionITV(
    val nombre: String,
    val tipo: String,
    val direccion: String,
    val localidad: String,
    val codigoPostal: String,
    val provincia: String,
    val descripcion: String
)

@Composable
@Preview
fun ScreenBusqueda() {

    var localidad by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Estación fija") }

    var resultados by remember { mutableStateOf(listOf<EstacionITV>()) }

    Column(Modifier.padding(50.dp)) {

        Text("Buscador de Estaciones ITV", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Row {

            // ---------- FORMULARIO ----------
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Localidad:", modifier = Modifier.width(100.dp))
                    OutlinedTextField(
                        value = localidad,
                        onValueChange = { localidad = it },
                        singleLine = true, // evita multiline
                        modifier = Modifier.width(200.dp)

                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Text("Cód. Postal:", modifier = Modifier.width(100.dp))
                    OutlinedTextField(
                        value = codigoPostal,
                        onValueChange = { codigoPostal = it },
                        singleLine = true, // evita multiline
                        modifier = Modifier.width(200.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Text("Provincia:", modifier = Modifier.width(100.dp))
                    OutlinedTextField(
                        value = provincia,
                        onValueChange = { provincia = it },
                        singleLine = true, // evita multiline
                        modifier = Modifier.width(200.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tipo:", modifier = Modifier.width(100.dp))
                    DropdownMenuTipo(tipo) { tipo = it }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = {
                        localidad = ""
                        codigoPostal = ""
                        provincia = ""
                        tipo = "Estación fija"
                        resultados = emptyList()
                    }) {
                        Text("Cancelar")
                    }

                    Button(onClick = {
                        // Datos de prueba simulando la búsqueda
                        resultados = listOf(
                            EstacionITV("ITV Vigo", "Fija", "Av. Madrid 10", "Vigo", "36214", "Pontevedra", "Turismos y motos"),
                            EstacionITV("ITV Valencia", "Móvil", "C/ Norte 22", "Valencia", "46010", "Valencia", "Solo turismos")
                        )
                    }) {
                        Text("Buscar")
                    }
                }
            }

            Spacer(modifier = Modifier.width(30.dp))

            // ---------- MAPA (simulado) ----------
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(250.dp)
                    .border(1.dp, Color.Black),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Mapa de estaciones ITV")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Resultados de la búsqueda:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))

        // ---------- TABLA ----------
        Column(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            // Cabecera
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                TableHeader("Nombre")
                TableHeader("Tipo")
                TableHeader("Dirección")
                TableHeader("Localidad")
                TableHeader("CP")
                TableHeader("Provincia")
                TableHeader("Descripción")
            }
            Divider()

            LazyColumn {
                items(resultados) { itv ->
                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        TableCell(itv.nombre)
                        TableCell(itv.tipo)
                        TableCell(itv.direccion)
                        TableCell(itv.localidad)
                        TableCell(itv.codigoPostal)
                        TableCell(itv.provincia)
                        TableCell(itv.descripcion)
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun TableHeader(text: String) {
    Text(
        text,
        modifier = Modifier
            .padding(4.dp),
        fontSize = 14.sp,
        color = Color.Black
    )
}


@Composable
fun TableCell(text: String) {
    Text(
        text,
        modifier = Modifier
            .padding(4.dp),
        fontSize = 13.sp
    )
}

@Composable
fun DropdownMenuTipo(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                onSelected("Estación fija")
                expanded = false
            }) {
                Text("Estación fija")
            }
            DropdownMenuItem(onClick = {
                onSelected("Estación móvil")
                expanded = false
            }) {
                Text("Estación móvil")
            }
        }
    }
}