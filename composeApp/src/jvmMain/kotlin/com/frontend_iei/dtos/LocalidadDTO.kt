package com.frontend_iei.dtos

import kotlinx.serialization.Serializable

@Serializable
class LocalidadDTO(
    val codigo: Long?,
    val nombre: String,
    val provincia: Long
)