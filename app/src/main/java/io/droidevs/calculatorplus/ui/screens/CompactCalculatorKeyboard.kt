package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.action.ClearAction
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.DecimalAction
import io.droidevs.calculatorplus.ui.action.DeleteAction
import io.droidevs.calculatorplus.ui.action.DigitAction
import io.droidevs.calculatorplus.ui.action.EqualsAction
import io.droidevs.calculatorplus.ui.action.OperatorAction
import io.droidevs.calculatorplus.ui.action.ParenthesisAction
import io.droidevs.calculatorplus.ui.component.CalculatorButton

@Composable
fun CompactCalculatorKeyboard(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonTextColor = Color.White
    val mainButtonsColor = Color(0xFF2D2D2D)
    val operationButtonsColor = Color(0xFFFF9500)
    val topButtonsColor = Color(0xFFA5A5A5)
    val backgroundColor = Color(0xFF000000)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Number buttons
        val buttonRows = provideActions()
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { button ->
                    val buttonColor = when (button) {
                        is EqualsAction -> operationButtonsColor
                        is OperatorAction -> if (button == OperatorAction.Percent) topButtonsColor else operationButtonsColor
                        is ClearAction, is DeleteAction, is ParenthesisAction -> topButtonsColor
                        else -> mainButtonsColor
                    }
                    CalculatorButton(
                        action = button,
                        color = buttonColor,
                        textColor = buttonTextColor,
                        modifier = Modifier.weight(1f),
                        onClick = onAction
                    )
                }
            }
        }
    }
}

fun provideActions(): List<List<Action>> {
    return listOf(
        listOf(ClearAction(), ParenthesisAction(), OperatorAction.Percent, DeleteAction()),
        listOf(DigitAction.SevenAction, DigitAction.EightAction, DigitAction.NineAction, OperatorAction.Divide),
        listOf(DigitAction.FourAction, DigitAction.FiveAction, DigitAction.SixAction, OperatorAction.Multiply),
        listOf(DigitAction.OneAction, DigitAction.TwoAction, DigitAction.ThreeAction, OperatorAction.Minus),
        listOf(DigitAction.ZeroAction, DecimalAction, EqualsAction, OperatorAction.Plus)
    )
}
