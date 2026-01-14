import com.frontend_iei.dtos.EstacionDTO
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.Closeable

private const val BASE_URL = "http://192.168.1.131:8080/api/"

class ApiClient() : Closeable {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 10_000
        }
        expectSuccess = false

        defaultRequest {
            url(BASE_URL)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }

    /* ---------------- GET ---------------- */

    suspend fun getEstacion(idEstacion: Long): ApiResult<EstacionDTO> =
        safeRequest {
            client.get("/api/estacion/getEstacion/$idEstacion")
        }

    suspend fun getEstacionByNombre(nombre: String): ApiResult<EstacionDTO> =
        safeRequest {
            client.get("/api/estacion/getEstacionByNombre") {
                parameter("nombre", nombre)
            }
        }

    suspend fun getAll(): ApiResult<List<EstacionDTO>> =
        safeRequest {
            client.get("/api/estacion/getAll")
        }

    suspend fun filterEstacion(filter: FilterDTO): ApiResult<List<EstacionDTO>> =
        safeRequest {
            client.get("/api/estacion/filterEstacion") {
                contentType(ContentType.Application.Json)
                setBody(filter)
            }
        }

    /* ---------------- POST ---------------- */

    suspend fun cargarEstaciones(fuentes: List<String>): ApiResult<List<String>> =
        safeRequest {
            client.post("/api/estacion/cargarEstaciones") {
                contentType(ContentType.Application.Json)
                setBody(fuentes)
            }
        }

    suspend fun postEstacion(estacion: EstacionDTO): ApiResult<EstacionDTO> =
        safeRequest {
            client.post("/api/estacion/postEstacion") {
                contentType(ContentType.Application.Json)
                setBody(estacion)
            }
        }

    /* ---------------- DELETE ---------------- */

    suspend fun borrarDatos(): ApiResult<String> =
        safeRequest {
            client.delete("/api/estacion/deleteAll")
        }

    /* ---------------- Helper ---------------- */

    private suspend inline fun <reified T> safeRequest(
        crossinline block: suspend () -> HttpResponse
    ): ApiResult<T> {
        return try {
            val response = block()
            when (response.status) {
                HttpStatusCode.OK -> ApiResult.Success(response.body(), response.status)
                HttpStatusCode.NotFound -> ApiResult.NotFound()
                else -> ApiResult.HttpError(response.status, response.bodyAsText())
            }
        } catch (e: Throwable) {
            ApiResult.NetworkError(e)
        }
    }

    override fun close() {
        client.close()
    }
}
