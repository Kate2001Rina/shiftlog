package app.shiftlog.presentation.ui.stats


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.shiftlog.domain.model.ShiftStats
import app.shiftlog.presentation.viewmodel.StatsUiState
import app.shiftlog.presentation.viewmodel.StatsViewModel

@Composable
fun StatsScreen(vm: StatsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Statistics", style = MaterialTheme.typography.headlineSmall)

        when (val s = state) {
            is StatsUiState.Loading -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StatsUiState.Error -> {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            is StatsUiState.Success -> StatsContent(s.stats)
            else -> ""
        }
    }
}

@Composable
private fun StatsContent(stats: ShiftStats) {
    StatCard(
        title = "This Week",
        value = "${stats.totalMinutesThisWeek / 60}h ${stats.totalMinutesThisWeek % 60}m",
        subtitle = if (stats.overtimeMinutesThisWeek > 0)
            "⚠ ${stats.overtimeMinutesThisWeek / 60}h overtime"
        else "Within 40h limit"
    )
    StatCard(
        title = "This Month",
        value = "${stats.totalMinutesThisMonth / 60}h ${stats.totalMinutesThisMonth % 60}m",
        subtitle = "${stats.totalShiftsThisMonth} shifts"
    )
    StatCard(
        title = "Average Shift",
        value = "${stats.averageShiftMinutes / 60}h ${stats.averageShiftMinutes % 60}m",
        subtitle = "per shift this month"
    )
}

@Composable
private fun StatCard(title: String, value: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}