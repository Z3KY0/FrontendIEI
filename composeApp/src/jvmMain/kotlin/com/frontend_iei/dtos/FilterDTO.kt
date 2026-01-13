import kotlinx.serialization.Serializable

@Serializable
data class FilterDTO(
    val nombre: String? = null,
    val tipo: String? = null,
    val localidadId: Long? = null
)
