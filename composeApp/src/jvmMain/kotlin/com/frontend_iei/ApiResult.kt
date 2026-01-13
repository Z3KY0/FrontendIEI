import io.ktor.http.*

sealed class ApiResult<out T> {
    data class Success<T>(val data: T, val status: HttpStatusCode) : ApiResult<T>()
    data class NotFound(val status: HttpStatusCode = HttpStatusCode.NotFound) : ApiResult<Nothing>()
    data class HttpError(val status: HttpStatusCode, val body: String?) : ApiResult<Nothing>()
    data class NetworkError(val exception: Throwable) : ApiResult<Nothing>()
}
