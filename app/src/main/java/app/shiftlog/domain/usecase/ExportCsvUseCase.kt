package app.shiftlog.domain.usecase

import android.content.Context
import android.net.Uri
import app.shiftlog.domain.repository.ShiftRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class ExportCsvUseCase @Inject constructor(
    private val repository: ShiftRepository,
    @ApplicationContext private val context: Context
) {
    // Возвращает Uri файла — его можно открыть через Intent или FileProvider
    suspend operator fun invoke(): Result<Uri> = runCatching {
        val now = LocalDate.now()
        val shifts = repository.getAllShifts().first()

        val csv = buildString {
            // Заголовок
            appendLine("Date,Start Time,End Time,Duration (min),Note")
            shifts.forEach { shift ->
                val date = shift.startTime.toLocalDate()
                val start = shift.startTime.toLocalTime().toString().take(5)  // HH:mm
                val end = shift.endTime?.toLocalTime()?.toString()?.take(5) ?: "active"
                val duration = shift.durationMinutes?.toString() ?: "-"
                val note = shift.note.replace(",", ";")  // экранируем запятые в заметках
                appendLine("$date,$start,$end,$duration,$note")
            }
        }

        // Сохраняем во внутреннее хранилище, потом расшариваем через FileProvider
        val fileName = "shiftlog_export_$now.csv"
        val file = java.io.File(context.cacheDir, fileName)
        file.writeText(csv)

        // FileProvider — безопасный способ передать файл другим приложениям
        androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}