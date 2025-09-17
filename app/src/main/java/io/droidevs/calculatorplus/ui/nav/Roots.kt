package io.droidevs.calculatorplus.ui.nav

import kotlinx.serialization.Serializable


interface AppDestination {
    val route: String
}

@Serializable
sealed class Screen(override val route: String) : AppDestination {


    @Serializable
    object Settings : Screen("wallpaper_list")

    object Calculator : Screen("wallpaper")

    @Serializable
    object History : Screen("albums")

    companion object {
        val allScreens = listOf(
            Calculator, History, Settings
        )
    }

}

@Serializable
open class Graph(override val route: String) : AppDestination {

    @Serializable
    object App : Graph("app")

    @Serializable
    object Home : Graph("home")

    companion object {
        val allGraphs = listOf(Home)
    }

}



object AppDestinations {
    val allDestinations = Screen.allScreens + Graph.allGraphs
}
