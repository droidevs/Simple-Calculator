package io.droidevs.calculatorplus.ui.window

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator


@Stable
class WindowSize(
    val widthDp: Dp,
    val heightDp: Dp
) {
    fun getClass() : WindowSizeClass {
        return getWindowClass(this)
    }
}

@Composable
fun calculateWindowSize(activity: Activity) : WindowSize {
    val density = LocalDensity.current
    return calculateWindowSize(density.density,activity)
}

fun calculateWindowSize(density: Float, activity: Activity) : WindowSize {
    var window = WindowMetricsCalculator.getOrCreate()
    val metrics = window.computeCurrentWindowMetrics(activity)
    val widthPx = metrics.bounds.width()
    val heightPx = metrics.bounds.height()

    return with(density){
        WindowSize(
            widthDp = widthPx.toDp(density),
            heightDp = heightPx.toDp(density)
        )
    }
}
private fun Int.toDp(density: Float): Dp {
    return (this / density).dp
}