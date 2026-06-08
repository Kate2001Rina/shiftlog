package app.shiftlog.domain.model

import java.time.LocalDateTime

data class Shift(
    val id: Long = 0,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val note: String = "",
    val isSynced: Boolean = false
) {
    val durationMinutes: Long?
        get() = endTime?.let {
            java.time.Duration.between(startTime, it).toMinutes()
        }

    val isActive: Boolean
        get() = endTime == null
}