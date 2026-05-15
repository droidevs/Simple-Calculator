package io.droidevs.calculatorplus.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.droidevs.calculatorplus.ui.CalculatorViewModel
import io.droidevs.calculatorplus.ui.screens.AboutScreen
import io.droidevs.calculatorplus.ui.screens.CalculatorScreen
import io.droidevs.calculatorplus.ui.screens.HelpScreen
import io.droidevs.calculatorplus.ui.screens.HistoryScreen
import io.droidevs.calculatorplus.ui.screens.SettingsScreen
import io.droidevs.calculatorplus.ui.screens.SplashScreen

fun NavGraphBuilder.appNavGraph(
    appState: NavigationAppState,
    viewModel: CalculatorViewModel,
) {
    composable(AppDestination.Splash.route) {
        SplashScreen(
            onContinue = {
                appState.navigateTo(AppDestination.Calculator, popUpTo = true, inclusive = true)
            }
        )
    }
    composable(AppDestination.Calculator.route) {
        CalculatorScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
            onHistoryClick = { appState.navigateTo(AppDestination.History) }
        )
    }
    composable(AppDestination.History.route) {
        HistoryScreen(
            historyItems = viewModel.history,
            onItemClick = {
                viewModel.onHistoryItemClick(it)
                appState.navigateTo(AppDestination.Calculator)
            },
            onItemDelete = viewModel::onHistoryItemDelete
        )
    }
    composable(AppDestination.Settings.route) {
        SettingsScreen(
            onNavigateAbout = { appState.navigateTo(AppDestination.About) },
            onNavigateHelp = { appState.navigateTo(AppDestination.Help) }
        )
    }
    composable(AppDestination.About.route) {
        AboutScreen()
    }
    composable(AppDestination.Help.route) {
        HelpScreen()
    }
}
