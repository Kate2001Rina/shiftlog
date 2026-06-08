package app.shiftlog.di

import android.content.Context
import androidx.room.Room
import app.shiftlog.data.local.ShiftDao
import app.shiftlog.data.local.ShiftDatabase
import app.shiftlog.data.repository.ShiftRepositoryImpl
import app.shiftlog.domain.repository.ShiftRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): ShiftDatabase =
        Room.databaseBuilder(ctx, ShiftDatabase::class.java, "shiftlog.db").build()

    @Provides
    @Singleton
    fun provideShiftDao(db: ShiftDatabase): ShiftDao = db.shiftDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShiftRepository(
        impl: ShiftRepositoryImpl
    ): ShiftRepository
}