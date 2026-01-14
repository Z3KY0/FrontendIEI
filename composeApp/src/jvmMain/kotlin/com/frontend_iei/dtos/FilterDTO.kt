package com.frontend_iei.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FilterDTO(
    val codigoPostal: String?,
    val localidad: String?,
    val provincia: String?,
    val tipo: TipoEstacion?
)
