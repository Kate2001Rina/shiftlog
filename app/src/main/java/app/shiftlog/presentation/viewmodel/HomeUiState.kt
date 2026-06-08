package app.shiftlog.presentation.viewmodel

import app.shiftlog.domain.model.Shift

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Idle(
        val recentShifts: List<Shift> = emptyList()
    ) : HomeUiState

    data class ActiveShift(
        val shift: Shift,
        val elapsedSeconds: Long = 0L   // тикает каждую секунду
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}