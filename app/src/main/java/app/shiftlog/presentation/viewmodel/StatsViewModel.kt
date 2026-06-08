package app.shiftlog.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shiftlog.domain.model.ShiftStats
import app.shiftlog.domain.usecase.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface StatsUiState {
    data object Loading : StatsUiState
    data class Success(val stats: ShiftStats) : StatsUiState
    data class Error(val message: String) : StatsUiState
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    getStats: GetStatsUseCase
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = getStats()
        .map<ShiftStats, StatsUiState> { StatsUiState.Success(it) }
        .catch { e -> emit(StatsUiState.Error(e.message ?: "Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatsUiState.Loading
        )
}