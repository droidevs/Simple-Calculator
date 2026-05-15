package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.action.ClearAction
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.DecimalAction
import io.droidevs.calculatorplus.ui.action.DigitAction
import io.droidevs.calculatorplus.ui.action.EqualsAction
import io.droidevs.calculatorplus.ui.action.OperatorAction
import io.droidevs.calculatorplus.ui.action.ParenthesisAction
import io.droidevs.calculatorplus.ui.action.ToggleSignAction
import io.droidevs.calculatorplus.ui.component.CalculatorButton
import io.droidevs.calculatorplus.ui.window.LayoutMode
import io.droidevs.calculatorplus.ui.window.LocalWindow

@Composable
fun CompactCalculatorKeyboard(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainButtonsColor = MaterialTheme.colorScheme.surfaceVariant
    val mainTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val operationButtonsColor = MaterialTheme.colorScheme.primary
    val operationTextColor = MaterialTheme.colorScheme.onPrimary
    val topButtonsColor = MaterialTheme.colorScheme.tertiaryContainer
    val topTextColor = MaterialTheme.colorScheme.onTertiaryContainer

    val windowInfo = LocalWindow.current
    val minDim = minOf(windowInfo.windowSize.widthDp, windowInfo.windowSize.heightDp)
    val layoutMode = windowInfo.layoutMode
    val maxSpacing = if (layoutMode == LayoutMode.PHONE_PORTRAIT) 10.dp else 14.dp
    val maxPadding = if (layoutMode == LayoutMode.PHONE_PORTRAIT) 12.dp else 16.dp
    val spacing = (minDim * 0.012f).coerceIn(4.dp, maxSpacing)
    val padding = (minDim * 0.02f).coerceIn(8.dp, maxPadding)

    val rows = compactRows()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEach { spec ->
                    if (spec.action != null) {
                        val (background, textColor) = when (spec.action) {
                            is EqualsAction -> operationButtonsColor to operationTextColor
                            is OperatorAction -> if (spec.action == OperatorAction.Percent) topButtonsColor to topTextColor else operationButtonsColor to operationTextColor
                            is ClearAction, is ParenthesisAction, is ToggleSignAction -> topButtonsColor to topTextColor
                            else -> mainButtonsColor to mainTextColor
                        }
                        CalculatorButton(
                            action = spec.action,
                            color = background,
                            textColor = textColor,
                            modifier = Modifier
                                .weight(spec.weight)
                                .fillMaxHeight(),
                            onClick = onAction
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(spec.weight)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

private data class ButtonSpec(val action: Action?, val weight: Float = 1f)

private fun compactRows(): List<List<ButtonSpec>> {
    return listOf(
        listOf(
            ButtonSpec(ClearAction()),
            ButtonSpec(ParenthesisAction()),
            ButtonSpec(OperatorAction.Percent),
            ButtonSpec(OperatorAction.Divide)
        ),
        listOf(
            ButtonSpec(DigitAction.SevenAction),
            ButtonSpec(DigitAction.EightAction),
            ButtonSpec(DigitAction.NineAction),
            ButtonSpec(OperatorAction.Multiply)
        ),
        listOf(
            ButtonSpec(DigitAction.FourAction),
            ButtonSpec(DigitAction.FiveAction),
            ButtonSpec(DigitAction.SixAction),
            ButtonSpec(OperatorAction.Minus)
        ),
        listOf(
            ButtonSpec(DigitAction.OneAction),
            ButtonSpec(DigitAction.TwoAction),
            ButtonSpec(DigitAction.ThreeAction),
            ButtonSpec(OperatorAction.Plus)
        ),
        listOf(
            ButtonSpec(ToggleSignAction()),
            ButtonSpec(DigitAction.ZeroAction),
            ButtonSpec(DecimalAction),
            ButtonSpec(EqualsAction)
        )
    )
}
