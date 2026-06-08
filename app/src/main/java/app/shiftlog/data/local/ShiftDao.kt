package app.shiftlog.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shifts ORDER BY startTime DESC")
    fun getAllShifts(): Flow<List<ShiftEntity>>

    @Query("""
        SELECT * FROM shifts
        WHERE startTime LIKE :yearMonth || '%'
        ORDER BY startTime DESC
    """)
    fun getShiftsForMonth(yearMonth: String): Flow<List<ShiftEntity>>

    @Query("SELECT * FROM shifts WHERE endTime IS NULL LIMIT 1")
    suspend fun getActiveShift(): ShiftEntity?

    @Query("SELECT * FROM shifts WHERE isSynced = 0")
    suspend fun getUnsyncedShifts(): List<ShiftEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shift: ShiftEntity): Long

    @Update
    suspend fun update(shift: ShiftEntity)

    @Query("DELETE FROM shifts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM shifts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ShiftEntity?

}