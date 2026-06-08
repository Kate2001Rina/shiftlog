package app.shiftlog

import android.app.Application
import androidx.work.WorkManager
import app.shiftlog.data.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShiftLogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Запускаем фоновую синхронизацию при старте приложения
        SyncWorker.schedule(WorkManager.getInstance(this))
    }
}