package app.shiftlog.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.shiftlog.presentation.ui.calendar.CalendarScreen
import app.shiftlog.presentation.ui.home.HomeScreen
import app.shiftlog.presentation.ui.settings.SettingsScreen
import app.shiftlog.presentation.ui.stats.StatsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Stats : Screen("stats")
    data object Settings : Screen("settings")
}

@Composable
fun ShiftLogNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Stats.route) { StatsScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}