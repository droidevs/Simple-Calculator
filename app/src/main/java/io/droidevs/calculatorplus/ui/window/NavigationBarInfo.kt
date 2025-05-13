package io.droidevs.calculatorplus.ui.window

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


// Data class representing the current state of the navigation bar.
data class NavigationBarInfo(
    val visible: Boolean = true, // Whether the navigation bar is visible.
    val color: Color = Color.Unspecified, // The color of the navigation bar.
)

// Abstract class to manage the navigation bar state.
// Provides a contract for manipulating the navigation bar behavior.
abstract class NavigationBarController {
    // The current state of the navigation bar stored in a mutable state object.
    abstract val state: MutableState<NavigationBarInfo>

    // Updates the navigation bar state using a transformation function.
    abstract fun update(fn: (prev: NavigationBarInfo) -> NavigationBarInfo)

    // Toggles the visibility of the navigation bar.
    abstract fun toggleVisibility()

    // Sets the visibility of the navigation bar explicitly.
    abstract fun setVisibility(isVisible: Boolean)

    // Updates the color of the navigation bar.
    abstract fun setColor(color: Color)

    // Resets the navigation bar to the default state.
    abstract fun reset()
}

// Default implementation of the NavigationBarController.
// Uses a MutableState object to manage the navigation bar state.
class DefaultNavigationBarController(
    initialState: NavigationBarInfo = NavigationBarInfo()
) : NavigationBarController() {

    // Internal state stored in a MutableState object for Compose reactivity.
    private val _state: MutableState<NavigationBarInfo> = mutableStateOf(initialState)

    // Exposed state for external access.
    override val state: MutableState<NavigationBarInfo> = _state

    // Updates the state using a transformation function.
    override fun update(fn: (prev: NavigationBarInfo) -> NavigationBarInfo) {
        val newState = fn(state.value)
        _state.value = newState
    }

    // Toggles the visibility of the navigation bar.
    override fun toggleVisibility() {
        update { prevState ->
            prevState.copy(visible = !prevState.visible)
        }
    }

    // Sets the visibility explicitly.
    override fun setVisibility(isVisible: Boolean) {
        update { prevState ->
            prevState.copy(visible = isVisible)
        }
    }

    // Updates the color of the navigation bar.
    override fun setColor(color: Color) {
        update { prevState ->
            prevState.copy(color = color)
        }
    }

    // Resets the state to its default values.
    override fun reset() {
        _state.value = NavigationBarInfo() // Reset to default values.
    }
}

// Providable CompositionLocal for accessing a NavigationBarController.
// Defaults to a new instance of DefaultNavigationBarController.
val LocalNavigationBar: ProvidableCompositionLocal<NavigationBarController> = staticCompositionLocalOf {
    DefaultNavigationBarController() // Default instance when none is provided.
}


