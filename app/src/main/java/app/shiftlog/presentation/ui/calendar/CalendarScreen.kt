package app.shiftlog.presentation.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.shiftlog.domain.model.Shift
import app.shiftlog.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(vm: CalendarViewModel = hiltViewModel()) {
    val selectedMonth by vm.selectedMonth.collectAsStateWithLifecycle()
    val shifts by vm.shiftsForMonth.collectAsStateWithLifecycle()

    // Множество дат у которых есть смена — для быстрой проверки в grid
    val shiftDays = remember(shifts) {
        shifts.map { it.startTime.toLocalDate() }.toSet()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Шапка: стрелки + название месяца
        MonthHeader(
            month = selectedMonth,
            onPrev = { vm.onMonthChanged(selectedMonth.minusMonths(1)) },
            onNext = { vm.onMonthChanged(selectedMonth.plusMonths(1)) }
        )

        Spacer(Modifier.height(12.dp))

        // Заголовки дней недели
        WeekDayLabels()

        Spacer(Modifier.height(8.dp))

        // Сетка дней
        CalendarGrid(month = selectedMonth, shiftDays = shiftDays)

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        // Список смен месяца
        ShiftList(shifts)
    }
}

@Composable
private fun MonthHeader(month: LocalDate, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous month")
        }
        Text(
            text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = onNext) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next month")
        }
    }
}

@Composable
private fun WeekDayLabels() {
    val days = listOf("Mo","Tu","We","Th","Fr","Sa","Su")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CalendarGrid(month: LocalDate, shiftDays: Set<LocalDate>) {
    val yearMonth = YearMonth.of(month.year, month.month)
    val firstDay = yearMonth.atDay(1)
    // Сколько пустых ячеек перед 1-м числом (Mon=0 offset)
    val offset = (firstDay.dayOfWeek.value - 1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val today = LocalDate.now()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false
    ) {
        // Пустые ячейки до начала месяца
        items(offset) { Spacer(Modifier.size(40.dp)) }

        items(daysInMonth) { idx ->
            val date = yearMonth.atDay(idx + 1)
            val hasShift = date in shiftDays
            val isToday = date == today

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(2.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            hasShift -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            isToday -> MaterialTheme.colorScheme.secondaryContainer
                            else -> androidx.compose.ui.graphics.Color.Transparent
                        }
                    )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${idx + 1}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isToday) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                    if (hasShift) {
                        Box(
                            Modifier.size(4.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShiftList(shifts: List<Shift>) {
    if (shifts.isEmpty()) {
        Text("No shifts this month", color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    shifts.forEach { shift ->
        val duration = shift.durationMinutes
        ListItem(
            headlineContent = { Text(shift.startTime.toLocalDate().toString()) },
            supportingContent = {
                Text(if (duration != null) "${duration / 60}h ${duration % 60}m"
                else "Active")
            }
        )
        HorizontalDivider()
    }
}