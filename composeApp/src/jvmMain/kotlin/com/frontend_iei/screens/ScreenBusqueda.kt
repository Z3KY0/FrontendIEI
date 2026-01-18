package com.frontend_iei.screens

import ApiClient
import ApiResult

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend_iei.dtos.EstacionDTO
import com.frontend_iei.dtos.FilterDTO
import com.frontend_iei.dtos.LocalidadDTO
import com.frontend_iei.dtos.ProvinciaDTO
import com.frontend_iei.dtos.TipoEstacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.input.PanMouseInputListener
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter
import org.jxmapviewer.viewer.DefaultTileFactory
import org.jxmapviewer.viewer.DefaultWaypoint
import org.jxmapviewer.viewer.GeoPosition
import org.jxmapviewer.viewer.WaypointPainter
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import javax.swing.JOptionPane


@Composable
@Preview
fun ScreenBusqueda() {

    val apiClient = ApiClient()
    val scope = rememberCoroutineScope() // Añade esto arriba con tus otros estados
    var localidad by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Todas") }

    var resultadosLoc: LocalidadDTO?= null
    var resultadosProv: ProvinciaDTO
    var resultados by remember { mutableStateOf(listOf<EstacionDTO>()) }
    var estacionSeleccionada by remember { mutableStateOf<EstacionDTO?>(null) }

    // --- DIÁLOGO DE INFORMACIÓN ---

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
                        tipo = "Todas"
                        resultados = emptyList()
                    }) {
                        Text("Cancelar")
                    }

                    Button(onClick = {
                        // 1. Usamos el scope para lanzar la función suspendida
                        scope.launch {
                            //val result = apiClient.getAll()
                            var tipoEstacion: TipoEstacion? = null
                            if (tipo == "Fija") tipoEstacion= TipoEstacion.Fija
                            if (tipo == "Movil") tipoEstacion= TipoEstacion.Movil
                            if (tipo == "Otro") tipoEstacion= TipoEstacion.Otro
                            val result = apiClient.filterEstacion(
                                FilterDTO(
                                    codigoPostal,
                                    localidad,
                                    provincia,
                                    tipoEstacion
                                )
                            )
                            // 2. Manejamos el ApiResult (suponiendo que es un sealed class)
                            when (result) {
                                is ApiResult.Success -> {
                                    // Extraemos la lista del interior del objeto Success
                                    resultados = result.data
                                }

                                else -> {println("Error al cargar: $result")}
                            }
                        }
                    }) {
                        Text("Buscar")
                    }
                }
            }

            Spacer(modifier = Modifier.width(30.dp))

            // ---------- MAPA (simulado) ----------
            // 1. Cargamos todas las estaciones al iniciar la pantalla de forma segura
            val todasLasEstaciones by produceState<List<EstacionDTO>>(initialValue = emptyList()) {
                val result = apiClient.getAll()
                if (result is ApiResult.Success) value = result.data
            }

// 2. Decidimos qué mostrar: si hay resultados de búsqueda, mostramos esos.
// Si no, mostramos todas (o nada, según prefieras).
            val estacionesAMostrar = if (resultados.isNotEmpty()) resultados else todasLasEstaciones

            Box(modifier = Modifier.weight(1f).height(300.dp).border(1.dp, Color.Black)) {
                OsmDesktopMap(
                    resultados = estacionesAMostrar,
                    apiClient = apiClient,
                    scope = scope,
                    onEstacionClick = { estacion ->
                        estacionSeleccionada = estacion
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Resultados de la búsqueda:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))

        // ---------- TABLA ----------
        // ---------- TABLA ----------
        Column(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // Cabecera
            Row(
                Modifier.fillMaxWidth().height(IntrinsicSize.Min), // Min para que el separador vertical llene la fila
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableHeader("Nombre", Modifier.weight(2f))
                VerticalDivider()
                TableHeader("Tipo", Modifier.weight(1.5f))
                VerticalDivider()
                TableHeader("Dirección", Modifier.weight(2f))
                VerticalDivider()
                TableHeader("Localidad", Modifier.weight(1.5f))
                VerticalDivider()
                TableHeader("CP", Modifier.weight(0.8f))
                VerticalDivider()
                TableHeader("Provincia", Modifier.weight(1.5f))
                VerticalDivider()
                TableHeader("Descripción", Modifier.weight(3f))
            }

            Divider(color = Color.Black, thickness = 2.dp)

            LazyColumn {
                items(resultados) { estacion ->
                    // Gestionamos el nombre de la localidad de forma segura
                    val localidadNombre by produceState(initialValue = "Cargando...", estacion.localidad) {
                        if (estacion.localidad != null) {
                            val res = apiClient.getLocalidadById(estacion.localidad!!)
                            value = if (res is ApiResult.Success) res.data.nombre else "Error"
                        } else {
                            value = "-"
                        }
                    }

                    // Gestionamos el nombre de la provincia de forma segura
                    val provinciaNombre by produceState(initialValue = "Cargando...", estacion.localidad) {
                        if (estacion.localidad != null) {
                            val resLoc = apiClient.getLocalidadById(estacion.localidad!!)
                            if (resLoc is ApiResult.Success) {
                                val resProv = apiClient.getProvinciaById(resLoc.data.provincia!!)
                                value = if (resProv is ApiResult.Success) resProv.data.nombre else "Error"
                            }
                        } else {
                            value = "-"
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(estacion.nombre, Modifier.weight(2f))
                        VerticalDivider()
                        TableCell(estacion.tipo, Modifier.weight(1.5f))
                        VerticalDivider()
                        TableCell(estacion.direccion ?: "-", Modifier.weight(2f))
                        VerticalDivider()

                        // Usamos las variables seguras que hemos creado arriba
                        TableCell(localidadNombre, Modifier.weight(1.5f))
                        VerticalDivider()
                        TableCell(estacion.codigo_postal, Modifier.weight(0.8f))
                        VerticalDivider()
                        TableCell(provinciaNombre, Modifier.weight(1.5f))
                        VerticalDivider()

                        TableCell(estacion.descripcion, Modifier.weight(3f))
                    }
                    Divider(color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun TableHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(4.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun TableCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(4.dp),
        fontSize = 13.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}
@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 4.dp)
            .background(Color.LightGray)
    )
}
@Composable
fun OsmDesktopMap(
    resultados: List<EstacionDTO>,
    apiClient: ApiClient,          // <--- Nuevo
    scope: CoroutineScope,         // <--- Nuevo
    modifier: Modifier = Modifier,
    onEstacionClick: (EstacionDTO) -> Unit
) {
    val currentResultados by rememberUpdatedState(resultados)

    val mapViewer = remember {
        JXMapViewer().apply {
            val info = OSMTileFactoryInfo("JXMapViewer2", "https://tile.openstreetmap.org")
            tileFactory = DefaultTileFactory(info)

            // Centrado en España y zoom fijo (ajusta a 12 o 13 según prefieras)
            setAddressLocation(GeoPosition(40.4168, -3.7038))
            zoom = 14

            // 1. Añadir soporte para arrastrar el mapa con el ratón
            val mia = PanMouseInputListener(this)
            addMouseListener(mia)
            addMouseMotionListener(mia)
// 2. Añadir soporte para zoom con la rueda del ratón
            addMouseWheelListener(ZoomMouseWheelListenerCenter(this))
            // --- DETECCIÓN DE CLIC EN PUNTOS ---
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.button != MouseEvent.BUTTON1) return

                    val clickPt = e.point

                    val encontrada = currentResultados.find { estacion ->
                        val gp = GeoPosition(estacion.latitud, estacion.longitud)
                        val gpPt = getTileFactory().geoToPixel(gp, zoom)
                        val localX = gpPt.getX() - viewportBounds.getX()
                        val localY = gpPt.getY() - viewportBounds.getY()

                        Point2D.distance(localX, localY, clickPt.getX(), clickPt.getY()) < 20
                    }

                    encontrada?.let { estacion ->
                        var resultadosLoc: LocalidadDTO? = null
                        scope.launch {
                            // Pedimos la localidad
                            val result = apiClient.getLocalidadById(estacion.localidad!!)
                            val nombreLocalidad = if (result is ApiResult.Success) result.data.nombre else "Desconocida"

                            // EL DIÁLOGO DEBE IR AQUÍ DENTRO
                            JOptionPane.showMessageDialog(
                                this@apply,
                                """
            ESTACIÓN: ${estacion.nombre}
            ------------------------------------------
            Dirección: ${estacion.direccion}
            Localidad: $nombreLocalidad
            C.P.: ${estacion.codigo_postal}
            """.trimIndent(),
                                "Detalles",
                                JOptionPane.INFORMATION_MESSAGE
                            )
                        }
                    }
                }
            })
        }
    }

    // Actualizamos solo los puntos visuales cuando cambien los resultados
    LaunchedEffect(resultados) {
        val waypoints = resultados.map { DefaultWaypoint(GeoPosition(it.latitud, it.longitud)) }.toSet()
        mapViewer.overlayPainter = WaypointPainter<DefaultWaypoint>().apply {
            setWaypoints(waypoints)
        }
    }

    SwingPanel(
        modifier = modifier.fillMaxSize(),
        factory = { mapViewer }
    )
}
@Composable
fun DropdownMenuTipo(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // Usamos un Box para contener el TextField y el Menú
    Box(modifier = Modifier.width(200.dp)) {
        OutlinedTextField(
            value = selected,
            onValueChange = { },
            readOnly = true, // Evita que el usuario escriba
            modifier = Modifier.fillMaxWidth(),
            // Añadimos el icono de la flecha
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            androidx.compose.material.icons.Icons.Default.KeyboardArrowUp
                        else
                            androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                        contentDescription = "Flecha"
                    )
                }
            },
            // Al hacer clic en el campo, se abre el menú
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        // Un componente invisible encima para detectar el clic en todo el campo
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            listOf("Todas", TipoEstacion.Fija.name, TipoEstacion.Movil.name, TipoEstacion.Otro.name).forEach { opcion ->
                DropdownMenuItem(onClick = {
                    onSelected(opcion)
                    expanded = false
                }) {
                    Text(opcion)
                }
            }
        }
    }
}