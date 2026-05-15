package io.droidevs.calculatorplus.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.droidevs.calculatorplus.ui.CalculatorViewModel


@Composable
fun DashboardNavGraph(
    appstate: NavigationAppState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = appstate.navController,
        startDestination = appstate.startDestination.route,
        modifier = modifier
    ) {
        this.appNavGraph(appstate, viewModel)
    }
}
