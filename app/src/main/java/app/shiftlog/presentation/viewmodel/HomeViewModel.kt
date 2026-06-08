package app.shiftlog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shiftlog.domain.usecase.EndShiftUseCase
import app.shiftlog.domain.usecase.StartShiftUseCase
import app.shiftlog.domain.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ShiftRepository,
    private val startShift: StartShiftUseCase,
    private val endShift: EndShiftUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        observeShifts()
    }

    private fun observeShifts() {
        viewModelScope.launch {
            repository.getAllShifts().collect { shifts ->
                val active = shifts.firstOrNull { it.isActive }
                if (active != null) {
                    // Есть активная смена — запускаем таймер
                    val elapsed = Duration.between(active.startTime, LocalDateTime.now())
                    _uiState.update {
                        HomeUiState.ActiveShift(active, elapsed.toSeconds())
                    }
                    startTimer(active.startTime)
                } else {
                    timerJob?.cancel()
                    _uiState.update {
                        HomeUiState.Idle(shifts.take(5))
                    }
                }
            }
        }
    }

    private fun startTimer(since: LocalDateTime) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val elapsed = Duration.between(since, LocalDateTime.now()).toSeconds()
                _uiState.update { current ->
                    if (current is HomeUiState.ActiveShift)
                        current.copy(elapsedSeconds = elapsed)
                    else current
                }
            }
        }
    }

    fun onStartShift() {
        viewModelScope.launch {
            startShift().onFailure { e ->
                _uiState.update { HomeUiState.Error(e.message ?: "Unknown error") }
            }
        }
    }

    fun onEndShift(shiftId: Long) {
        viewModelScope.launch {
            endShift(shiftId).onFailure { e ->
                _uiState.update { HomeUiState.Error(e.message ?: "Unknown error") }
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}