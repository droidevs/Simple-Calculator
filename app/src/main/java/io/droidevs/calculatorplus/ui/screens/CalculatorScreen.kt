package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.state.CalculatorState
import io.droidevs.calculatorplus.ui.window.LayoutMode
import io.droidevs.calculatorplus.ui.window.LocalWindow

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = state.expression,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.result,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            state.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }

        val layoutMode = LocalWindow.current.layoutMode
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
        ) {
            if (layoutMode == LayoutMode.PHONE_PORTRAIT) {
                CompactCalculatorKeyboard(
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LandscapeCalculatorKeyboard(
                    onAction = onAction
                )
            }
        }
    }
}
