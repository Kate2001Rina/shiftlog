package app.shiftlog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.shiftlog.domain.model.Shift
import java.time.LocalDateTime

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: String,
    val endTime: String? = null,
    val note: String = "",
    val isSynced: Boolean = false
) {
    fun toDomain(): Shift = Shift(
        id = id,
        startTime = LocalDateTime.parse(startTime),
        endTime = endTime?.let { LocalDateTime.parse(it) },
        note = note,
        isSynced = isSynced
    )

    companion object {
        fun fromDomain(shift: Shift) = ShiftEntity(
            id = shift.id,
            startTime = shift.startTime.toString(),
            endTime = shift.endTime?.toString(),
            note = shift.note,
            isSynced = shift.isSynced
        )
    }
}