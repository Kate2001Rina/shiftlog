package app.shiftlog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShiftEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShiftDatabase : RoomDatabase() {
    abstract fun shiftDao(): ShiftDao
}