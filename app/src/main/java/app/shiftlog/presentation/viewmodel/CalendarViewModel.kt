package app.shiftlog.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shiftlog.domain.model.Shift
import app.shiftlog.domain.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: ShiftRepository
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(LocalDate.now())
    val selectedMonth: StateFlow<LocalDate> = _selectedMonth.asStateFlow()

    // flatMapLatest — при смене месяца автоматически переподписывается
    val shiftsForMonth: StateFlow<List<Shift>> = _selectedMonth
        .flatMapLatest { date ->
            repository.getShiftsForMonth(date.year, date.monthValue)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onMonthChanged(date: LocalDate) {
        _selectedMonth.update { date }
    }
}