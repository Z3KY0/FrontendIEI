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
    var allChecked by remember { mutableStateOf(false) }
    var GALchecked by remember { mutableStateOf(false) }
    var CVchecked by remember { mutableStateOf(true) }
    var CATchecked by remember { mutableStateOf(false) }

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
                onClick = { },
                enabled = true
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    scope.launch {
                        println("Cargar Estaciones")
                        ApiClient().use { api ->
                            val fuentes = listOf("GAL", "CV", "CAT")
                            //val resultadoCarga = api.cargarEstaciones(fuentes)
                            //println(resultadoCarga)
                        }
                    }
                },
                enabled = true
            ) {
                Text("Cargar")
            }
            Button(
                onClick = {
                    scope.launch {
                        println("Borrar todos los Datos")
                        ApiClient().use { api ->
                            //api.borrarDatos()
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
                .border(1.dp, Color.Black)
                .padding(8.dp)
        ) {
            Text("Número de registros cargados correctamente: NN")
            Text("")
            Text("Registros con errores y reparados: ")
            Text("{Fuente de datos, nombre, Localidad, " +
                    "motivo del error, operación realizada}")
            Text("")
            Text("Registros con errores y rechazados: ")
            Text("{Fuente de datos, nombre, Localidad, motivo del error}")
        }
    }
}