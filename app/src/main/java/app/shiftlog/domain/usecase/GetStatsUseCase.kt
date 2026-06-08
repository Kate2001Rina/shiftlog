package app.shiftlog.domain.usecase

import app.shiftlog.domain.model.ShiftStats
import app.shiftlog.domain.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: ShiftRepository
) {
    operator fun invoke(): Flow<ShiftStats> {
        val now = LocalDate.now()
        return repository.getShiftsForMonth(now.year, now.monthValue).map { shifts ->
            val weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weekShifts = shifts.filter {
                it.startTime.toLocalDate() >= weekStart
            }
            val weekMinutes = weekShifts.sumOf { it.durationMinutes ?: 0 }
            val monthMinutes = shifts.sumOf { it.durationMinutes ?: 0 }
            ShiftStats(
                totalMinutesThisWeek = weekMinutes,
                totalMinutesThisMonth = monthMinutes,
                averageShiftMinutes = if (shifts.isEmpty()) 0 else monthMinutes / shifts.size,
                totalShiftsThisMonth = shifts.size,
                overtimeMinutesThisWeek = maxOf(0, weekMinutes - 2400) // 40ч = 2400мин
            )
        }
    }
}