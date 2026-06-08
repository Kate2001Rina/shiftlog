package app.shiftlog.domain.repository

import app.shiftlog.domain.model.Shift
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ShiftRepository {
    fun getAllShifts(): Flow<List<Shift>>
    fun getShiftsForMonth(year: Int, month: Int): Flow<List<Shift>>
    suspend fun getActiveShift(): Shift?
    suspend fun startShift(): Shift
    suspend fun endShift(shiftId: Long): Shift
    suspend fun updateNote(shiftId: Long, note: String)
    suspend fun deleteShift(shiftId: Long)
    suspend fun syncWithRemote()
}