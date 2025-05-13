package io.droidevs.calculatorplus.ui.window

import androidx.compose.runtime.Composable

object Window{}

val Window.info
    @Composable
    get() = LocalWindow.current


val Window.statusBar
    @Composable
    get() = LocalStatusBar.current.state.value

val Window.navigationBar
    @Composable
    get() = LocalNavigationBar.current.state.value





