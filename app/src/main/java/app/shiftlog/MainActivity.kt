package app.shiftlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.shiftlog.presentation.navigation.Screen
import app.shiftlog.presentation.navigation.ShiftLogNavGraph
import app.shiftlog.presentation.theme.ShiftLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShiftLogTheme {
                val navController = rememberNavController()
                val backStack by navController.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                val items = listOf(
                    Triple(Screen.Home, "Home", Icons.Default.Home),
                    Triple(Screen.Calendar, "Calendar", Icons.Default.DateRange),
                    Triple(Screen.Stats, "Stats", Icons.Default.BarChart),
                    Triple(Screen.Settings, "Settings", Icons.Default.Settings)
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEach { (screen, label, icon) ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(Screen.Home.route) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(icon, label) },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    ShiftLogNavGraph(navController, padding)
                }
            }
        }
    }
}