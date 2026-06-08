package app.shiftlog.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class ShiftApiService @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "https://shiftlog-backend.com/api"

    // Получить все смены пользователя с сервера
    suspend fun getShifts(): List<ShiftDto> =
        client.get("$baseUrl/shifts").body()

    // Отправить несинхронизированные смены на сервер
    suspend fun uploadShifts(shifts: List<ShiftDto>): List<ShiftDto> =
        client.post("$baseUrl/shifts/sync") {
            contentType(ContentType.Application.Json)
            setBody(shifts)
        }.body()
}