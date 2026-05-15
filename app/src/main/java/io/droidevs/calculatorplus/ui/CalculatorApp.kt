package io.droidevs.calculatorplus.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import io.droidevs.calculatorplus.R
import io.droidevs.calculatorplus.ui.nav.AppDestination
import io.droidevs.calculatorplus.ui.nav.AppDestinations
import io.droidevs.calculatorplus.ui.nav.DashboardNavGraph
import io.droidevs.calculatorplus.ui.nav.rememberNavigationAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp(
    viewModel: CalculatorViewModel = viewModel()
) {
    val appState = rememberNavigationAppState(startDestination = AppDestination.Splash)
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route?.let { route ->
        AppDestinations.allDestinations.find { it.route == route }
    }
    val showTopBar = currentDestination != null && currentDestination != AppDestination.Splash

    Scaffold(
        topBar = {
            if (showTopBar) {
                val destination = currentDestination!!
                TopAppBar(
                    title = { Text(destination.title.ifBlank { stringResource(id = R.string.app_name) }) },
                    navigationIcon = {
                        if (!destination.showInBottomBar) {
                            IconButton(onClick = { appState.navigateBack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    actions = {
                        if (destination == AppDestination.History && viewModel.history.isNotEmpty()) {
                            IconButton(onClick = viewModel::onClearHistory) {
                                Icon(Icons.Default.Delete, contentDescription = "Clear history")
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        DashboardNavGraph(
            appstate = appState,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
