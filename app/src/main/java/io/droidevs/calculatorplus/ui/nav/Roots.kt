package io.droidevs.calculatorplus.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val showInBottomBar: Boolean = false
) {
    object Splash : AppDestination("splash", "Welcome")
    object Calculator : AppDestination(
        route = "calculator",
        title = "Calculator",
        selectedIcon = Icons.Filled.Calculate,
        unselectedIcon = Icons.Outlined.Calculate,
        showInBottomBar = true
    )
    object History : AppDestination(
        route = "history",
        title = "History",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        showInBottomBar = true
    )
    object Settings : AppDestination(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        showInBottomBar = true
    )
    object About : AppDestination(
        route = "about",
        title = "About",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    )
    object Help : AppDestination(
        route = "help",
        title = "Help",
        selectedIcon = Icons.Filled.Help,
        unselectedIcon = Icons.Outlined.HelpOutline
    )
}

object AppDestinations {
    val allDestinations = listOf(
        AppDestination.Splash,
        AppDestination.Calculator,
        AppDestination.History,
        AppDestination.Settings,
        AppDestination.About,
        AppDestination.Help
    )

    val bottomBarDestinations = allDestinations.filter { it.showInBottomBar }
}
