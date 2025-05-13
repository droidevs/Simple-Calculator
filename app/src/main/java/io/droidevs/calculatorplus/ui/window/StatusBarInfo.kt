package io.droidevs.calculatorplus.ui.window

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Data class to represent the current state of the status bar.
data class StatusBarInfo(
    val visible: Boolean = true,  // Whether the status bar is visible.
    val color: Color = Color.Unspecified, // The color of the status bar.
)

// Abstract controller for managing the status bar state.
// This provides a common interface for managing status bar behavior.
abstract class StatusBarController {
    // The current state of the status bar, stored in a mutable state object.
    abstract val state: MutableState<StatusBarInfo>

    // Function to update the status bar state using a transformation function.
    // The `fn` parameter allows for modifications based on the previous state.
    abstract fun update(fn: (prevState: StatusBarInfo) -> StatusBarInfo)

    // Function to toggle the visibility of the status bar.
    abstract fun toggleVisibility()

    // Function to set the visibility of the status bar explicitly.
    abstract fun setVisibility(isVisible: Boolean)

    // Function to update the color of the status bar.
    abstract fun setColor(color: Color)

    abstract fun getColor() : Color

    // Function to reset the status bar state to the default values.
    abstract fun reset()
}

// Default implementation of the StatusBarController.
// Uses a MutableState to hold and manage the current status bar state.
class DefaultStatusBarController(initialState: StatusBarInfo = StatusBarInfo()) : StatusBarController() {

    // Internal state, encapsulated in a MutableState object.
    private val _state: MutableState<StatusBarInfo> = mutableStateOf(initialState)

    // Publicly accessible state.
    override val state: MutableState<StatusBarInfo> = _state

    // Updates the state using a transformation function `fn`.
    override fun update(fn: (prevState: StatusBarInfo) -> StatusBarInfo) {
        val newState = fn(state.value)
        _state.value = newState
    }

    // Toggles the visibility of the status bar between visible and hidden.
    override fun toggleVisibility() {
        update { prevState ->
            prevState.copy(visible = !prevState.visible)
        }
    }

    // Sets the visibility of the status bar explicitly.
    override fun setVisibility(isVisible: Boolean) {
        update { prevState ->
            prevState.copy(visible = isVisible)
        }
    }

    // Updates the color of the status bar.
    override fun setColor(color: Color) {
        update { prevState ->
            prevState.copy(color = color)
        }
    }

    override fun getColor() : Color {
        return state.value.color
    }

    // Resets the status bar to its initial state.
    override fun reset() {
        _state.value = StatusBarInfo() // Reset to the default values.
    }
}

// Provides a CompositionLocal for accessing a StatusBarController.
// Defaults to an instance of DefaultStatusBarController.
val LocalStatusBar: ProvidableCompositionLocal<StatusBarController> = staticCompositionLocalOf {
    // Default instance used when no StatusBarController is provided.
    DefaultStatusBarController(StatusBarInfo())
}
