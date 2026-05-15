package io.droidevs.calculatorplus.ui.window

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.window.WindowInfo


val  LocalWindow = compositionLocalOf {
    WindowInfo(
        windowSize = WindowSize(0.dp, 0.dp),
        foldableInfo = null
    )
}
