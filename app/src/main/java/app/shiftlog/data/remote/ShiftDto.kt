package app.shiftlog.data.remote

import app.shiftlog.domain.model.Shift
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

// DTO — объект для передачи данных по сети (Data Transfer Object)
// Отделён от domain модели чтобы изменения API не ломали бизнес-логику
@Serializable
data class ShiftDto(
    val id: Long,
    val startTime: String,
    val endTime: String? = null,
    val note: String = ""
) {
    fun toDomain(): Shift = Shift(
        id = id,
        startTime = LocalDateTime.parse(startTime),
        endTime = endTime?.let { LocalDateTime.parse(it) },
        note = note,
        isSynced = true
    )

    companion object {
        fun fromDomain(shift: Shift) = ShiftDto(
            id = shift.id,
            startTime = shift.startTime.toString(),
            endTime = shift.endTime?.toString(),
            note = shift.note
        )
    }
}