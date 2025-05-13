package io.droidevs.calculatorplus.ui.window

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Stable
class WindowSizeClass(
    val widthClass: SizeClass,
    val heightClass: SizeClass,
)


fun getWindowClass(size: WindowSize) : WindowSizeClass {
    return WindowSizeClass(
        widthClass = getSizeClass(size.widthDp),
        heightClass = getSizeClass(size.heightDp)
    )
}

private fun getSizeClass(size: Dp): SizeClass {
    return when(size){
        in 0.dp..600.dp -> SizeClass.Compact
        in 480.dp..840.dp -> SizeClass.Medium
        else -> SizeClass.Expanded
    }
}

sealed class SizeClass {
    object Compact: SizeClass()
    object Medium: SizeClass()
    object Expanded: SizeClass()
}

data class ScreenSizeClass(
    val widthClass: SizeClass,
    val heightClass: SizeClass
)
