package app.shiftlog.data.repository


import app.shiftlog.data.local.ShiftDao
import app.shiftlog.data.local.ShiftEntity
import app.shiftlog.data.remote.ShiftApiService
import app.shiftlog.data.remote.ShiftDto
import app.shiftlog.domain.model.Shift
import app.shiftlog.domain.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(
    private val dao: ShiftDao,
    private val apiService: ShiftApiService
) : ShiftRepository {

    override fun getAllShifts(): Flow<List<Shift>> =
        dao.getAllShifts().map { list -> list.map { it.toDomain() } }

    override fun getShiftsForMonth(year: Int, month: Int): Flow<List<Shift>> {
        val prefix = "$year-${month.toString().padStart(2, '0')}"
        return dao.getShiftsForMonth(prefix).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getActiveShift(): Shift? =
        dao.getActiveShift()?.toDomain()

    override suspend fun startShift(): Shift {
        val entity = ShiftEntity(startTime = LocalDateTime.now().toString())
        val id = dao.insert(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun endShift(shiftId: Long): Shift {
        val active = dao.getActiveShift()
            ?: throw IllegalStateException("No active shift found")
        val updated = active.copy(endTime = LocalDateTime.now().toString())
        dao.update(updated)
        return updated.toDomain()
    }

    override suspend fun updateNote(shiftId: Long, note: String) {
        val entity = dao.getById(shiftId) ?: return
        dao.update(entity.copy(note = note))
    }

    override suspend fun deleteShift(shiftId: Long) =
        dao.deleteById(shiftId)

    override suspend fun syncWithRemote() {
        val unsynced = dao.getUnsyncedShifts()
        if (unsynced.isEmpty()) return
        val dtos = unsynced.map { ShiftDto.fromDomain(it.toDomain()) }
        val synced = apiService.uploadShifts(dtos)
        synced.forEach { dto ->
            dao.update(ShiftEntity.fromDomain(dto.toDomain()))
        }
    }
}