package io.droidevs.calculatorplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import io.droidevs.calculatorplus.ui.CalculatorApp
import io.droidevs.calculatorplus.ui.theme.SimpleCalculatorTheme
import io.droidevs.calculatorplus.ui.theme.EdgeToEdge
import io.droidevs.calculatorplus.ui.window.DefaultNavigationBarController
import io.droidevs.calculatorplus.ui.window.DefaultStatusBarController
import io.droidevs.calculatorplus.ui.window.LocalWindow
import io.droidevs.calculatorplus.ui.window.LocalNavigationBar
import io.droidevs.calculatorplus.ui.window.LocalStatusBar
import io.droidevs.calculatorplus.ui.window.WindowInfo
import io.droidevs.calculatorplus.ui.window.calculateWindowSize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleCalculatorTheme {
                val statusBarController = remember { DefaultStatusBarController() }
                val navigationBarController = remember { DefaultNavigationBarController() }
                val windowSize = calculateWindowSize(this)
                CompositionLocalProvider(
                    LocalWindow provides WindowInfo(
                        windowSize = windowSize,
                        foldableInfo = null
                    ),
                    LocalStatusBar provides statusBarController,
                    LocalNavigationBar provides navigationBarController
                ) {
                    EdgeToEdge(
                        statusBarVisible = statusBarController.state.value.visible,
                        statusBarColor = statusBarController.state.value.color,
                        navigationBarVisible = navigationBarController.state.value.visible,
                        navigationBarColor = navigationBarController.state.value.color
                    )
                    CalculatorApp()
                }
            }
        }
    }
}
