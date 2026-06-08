package app.shiftlog.presentation.ui.settings

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.shiftlog.presentation.viewmodel.ExportState
import app.shiftlog.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {
    val exportState by vm.exportState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var notificationsOn by remember { mutableStateOf(true) }
    var overtimeAlertOn by remember { mutableStateOf(true) }
    var showExportDialog by remember { mutableStateOf(false) }

    // Когда экспорт готов — открываем системный шаринг
    LaunchedEffect(exportState) {
        if (exportState is ExportState.Success) {
            val uri = (exportState as ExportState.Success).uri
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share shifts CSV"))
            vm.onExportHandled()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        Text(
            "Notifications",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        Card {
            ListItem(
                headlineContent = { Text("Shift reminders") },
                supportingContent = { Text("Alert after 8h of active shift") },
                leadingContent = { Icon(Icons.Default.Notifications, null) },
                trailingContent = {
                    Switch(checked = notificationsOn, onCheckedChange = { notificationsOn = it })
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Overtime alert") },
                supportingContent = { Text("Warn when weekly hours exceed 40h") },
                leadingContent = { Icon(Icons.Default.Warning, null) },
                trailingContent = {
                    Switch(checked = overtimeAlertOn, onCheckedChange = { overtimeAlertOn = it })
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Data",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        Card {
            ListItem(
                headlineContent = { Text("Export to CSV") },
                supportingContent = { Text("Share all shifts as spreadsheet") },
                leadingContent = { Icon(Icons.Default.Download, null) },
                trailingContent = {
                    when (exportState) {
                        is ExportState.Loading ->
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else ->
                            TextButton(onClick = { showExportDialog = true }) {
                                Text("Export")
                            }
                    }
                }
            )
        }

        // Ошибка экспорта
        if (exportState is ExportState.Error) {
            Text(
                (exportState as ExportState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }

    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export shifts") },
            text = { Text("All your shifts will be exported as a CSV file and shared.") },
            confirmButton = {
                TextButton(onClick = {
                    showExportDialog = false
                    vm.onExport()
                }) { Text("Export") }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) { Text("Cancel") }
            }
        )
    }
}