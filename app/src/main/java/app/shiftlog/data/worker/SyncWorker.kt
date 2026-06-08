package app.shiftlog.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import app.shiftlog.domain.repository.ShiftRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ShiftRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = runCatching {
        repository.syncWithRemote()
    }.fold(
        onSuccess = { Result.success() },
        onFailure = { Result.retry() }
    )

    companion object {
        const val WORK_NAME = "shift_sync"

        fun schedule(workManager: WorkManager) {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(
                repeatInterval = 15, TimeUnit.MINUTES
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}