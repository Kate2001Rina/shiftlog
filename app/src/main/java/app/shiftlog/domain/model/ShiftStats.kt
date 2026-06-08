package app.shiftlog.domain.model

data class ShiftStats(
    val totalMinutesThisWeek: Long,
    val totalMinutesThisMonth: Long,
    val averageShiftMinutes: Long,
    val totalShiftsThisMonth: Int,
    val overtimeMinutesThisWeek: Long
)