package com.frontend_iei.screens

import ApiClient
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend_iei.Screen
import com.frontend_iei.components.CheckboxWithText
import com.frontend_iei.components.Contenido
import com.frontend_iei.components.MenuNavegacion
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.skia.paragraph.TextBox

@Composable
@Preview
fun ScreenCarga() {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var allChecked by remember { mutableStateOf(false) }
    var GALchecked by remember { mutableStateOf(false) }
    var CVchecked by remember { mutableStateOf(false) }
    var CATchecked by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(false) }

    var registrosCorrectos by remember { mutableStateOf("") }
    var registrosReparados by remember { mutableStateOf("") }
    var registrosRechazados by remember { mutableStateOf("") }

    Column ( (Modifier.padding(50.dp) ) ) {

        Text("Carga del almacén de datos", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text("Seleccione fuente:", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                CheckboxWithText(
                    isChecked = allChecked,
                    onCheckedChange = { selected ->
                        allChecked = selected
                        GALchecked = selected
                        CVchecked = selected
                        CATchecked = selected
                    },
                    label = "Seleccionar todas"
                )
                CheckboxWithText(
                    isChecked = GALchecked,
                    onCheckedChange = { selected ->
                        GALchecked = selected
                        if (!selected) allChecked = false
                        if (GALchecked && CVchecked && CATchecked) allChecked = true
                                      },
                    label = "Galicia"
                )
                CheckboxWithText(
                    isChecked = CVchecked,
                    onCheckedChange = { selected ->
                        CVchecked = selected
                        if (!selected) allChecked = false
                        if (GALchecked && CVchecked && CATchecked) allChecked = true
                                      },
                    label = "Comunitat Valenciana"
                )
                CheckboxWithText(
                    isChecked = CATchecked,
                    onCheckedChange = { selected ->
                        CATchecked = selected
                        if (!selected) allChecked = false
                        if (GALchecked && CVchecked && CATchecked) allChecked = true
                                      },
                    label = "Catalunya"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    allChecked = false
                    GALchecked = false
                    CVchecked = false
                    CATchecked = false
                          },
                enabled = true
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    cargando = true
                    registrosCorrectos = "Cargando..."
                    registrosReparados = "Cargando..."
                    registrosRechazados = "Cargando..."
                    scope.launch {
                        println("Cargar Estaciones")
                        ApiClient().use { api ->
                            val fuentes = mutableListOf<String>()
                            if (GALchecked) { fuentes.add("GAL") }
                            if (CVchecked) { fuentes.add("CV") }
                            if (CATchecked) { fuentes.add("CAT") }
                            println(fuentes)
                            val resultadoCarga = api.cargarEstaciones(fuentes)
                            println(resultadoCarga)

                            when (resultadoCarga) {
                                is ApiResult.Success -> {
                                    val data = resultadoCarga.data
                                    println("Datos recibidos: $data")
                                    registrosCorrectos = data.get(0)
                                    registrosReparados = data.get(1)
                                    registrosRechazados = data.get(2)
                                }
                                is ApiResult.NotFound -> {
                                    println("No encontrado: ${resultadoCarga.status}")
                                }
                                is ApiResult.HttpError -> {
                                    println("Error HTTP: ${resultadoCarga.status}, body: ${resultadoCarga.body}")
                                }
                                is ApiResult.NetworkError -> {
                                    println("Error de red: ${resultadoCarga.exception.message}")
                                }
                            }
                            cargando = false
                        }
                    }
                },
                enabled = !cargando && GALchecked or CVchecked or CATchecked,
            ) {
                Text("Cargar")
            }
            Button(
                onClick = {
                    scope.launch {
                        println("Borrar todos los Datos")
                        ApiClient().use { api ->
                            api.borrarDatos()
                        }
                    }
                },
                enabled = true
            ) {
                Text("Borrar almacén de datos")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Resultados de la carga:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .border(1.dp, Color.Black)
                .width(600.dp)
                .padding(all = 8.dp)
        ) {
            Text("Número de registros cargados correctamente: " + registrosCorrectos)
            Text("")
            Text("Registros con errores y reparados: ")
            Text(registrosReparados)
            Text("")
            Text("Registros con errores y rechazados: ")
            Text(registrosRechazados)
        }
    }
}