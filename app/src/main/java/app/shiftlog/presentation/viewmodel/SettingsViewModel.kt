package app.shiftlog.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shiftlog.domain.usecase.ExportCsvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ExportState {
    data object Idle : ExportState
    data object Loading : ExportState
    data class  Success(val uri: Uri) : ExportState
    data class  Error(val message: String) : ExportState
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportCsv: ExportCsvUseCase
) : ViewModel() {

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState.asStateFlow()

    fun onExport() {
        viewModelScope.launch {
            _exportState.update { ExportState.Loading }
            exportCsv().fold(
                onSuccess = { uri -> _exportState.update { ExportState.Success(uri) } },
                onFailure = { e  -> _exportState.update { ExportState.Error(e.message ?: "Export failed") } }
            )
        }
    }

    // После того как UI обработал Uri — сбрасываем состояние
    fun onExportHandled() = _exportState.update { ExportState.Idle }
}