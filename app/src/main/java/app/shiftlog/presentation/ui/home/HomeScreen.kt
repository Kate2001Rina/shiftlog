package app.shiftlog.presentation.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.shiftlog.presentation.viewmodel.HomeUiState
import app.shiftlog.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(vm: HomeViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val s = state) {
            is HomeUiState.Loading -> CircularProgressIndicator()

            is HomeUiState.Idle -> {
                ShiftStatusText("No active shift")
                Spacer(Modifier.height(32.dp))
                PulsingButton(label = "Start Shift", active = false) {
                    vm.onStartShift()
                }
            }

            is HomeUiState.ActiveShift -> {
                ShiftStatusText("Shift in progress")
                Spacer(Modifier.height(8.dp))
                ElapsedTimer(s.elapsedSeconds)
                Spacer(Modifier.height(32.dp))
                PulsingButton(label = "End Shift", active = true) {
                    vm.onEndShift(s.shift.id)
                }
            }

            is HomeUiState.Error -> {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            else -> ""
        }
    }
}

@Composable
private fun ShiftStatusText(text: String) {
    Text(text, style = MaterialTheme.typography.headlineSmall)
}

@Composable
private fun ElapsedTimer(seconds: Long) {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    Text(
        text = "%02d:%02d:%02d".format(h, m, s),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

// Большая круглая кнопка с пульсирующей анимацией когда смена активна
@Composable
private fun PulsingButton(label: String, active: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (active) 1.08f else 1f,
        animationSpec = if (active) infiniteRepeatable(
            tween(800), RepeatMode.Reverse
        ) else tween(300),
        label = "pulse"
    )
    Button(
        onClick = onClick,
        modifier = Modifier.size(160.dp).scale(scale),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
    }
}