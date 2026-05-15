package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.ClearAction
import io.droidevs.calculatorplus.ui.action.ConstantAction
import io.droidevs.calculatorplus.ui.action.DeleteAction
import io.droidevs.calculatorplus.ui.action.FunctionAction
import io.droidevs.calculatorplus.ui.action.OperatorAction
import io.droidevs.calculatorplus.ui.action.ParenthesisAction
import io.droidevs.calculatorplus.ui.component.CalculatorButton


@Composable
fun LandscapeCalculatorKeyboard(
    onAction: (Action) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        SideCalculatorKeyboard(
            onAction = onAction,
            modifier = Modifier.weight(1f)
        )
        CompactCalculatorKeyboard(
            onAction = onAction,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SideCalculatorKeyboard(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        ClearAction(),
        ParenthesisAction(),
        OperatorAction.Percent,
        DeleteAction(),
        FunctionAction.SquareRoot,
        FunctionAction.Square,
        FunctionAction.Power,
        FunctionAction.PowerE,
        FunctionAction.Sin,
        FunctionAction.Cos,
        FunctionAction.Tan,
        FunctionAction.ASin,
        FunctionAction.ACos,
        FunctionAction.ATan,
        FunctionAction.SinH,
        FunctionAction.CosH,
        FunctionAction.TanH,
        FunctionAction.ASinH,
        FunctionAction.ACosH,
        FunctionAction.ATanH,
        FunctionAction.Ln,
        FunctionAction.Log,
        FunctionAction.OneDevideX,
        ConstantAction.PI,
        ConstantAction.E
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(actions.size) { index ->
            val action = actions[index]
            val (background, textColor) = actionColors(action)
            CalculatorButton(
                action = action,
                color = background,
                textColor = textColor,
                modifier = Modifier.fillMaxWidth(),
                onClick = onAction
            )
        }
    }
}

@Composable
private fun actionColors(action: Action) = when (action) {
    is OperatorAction -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    is ClearAction, is DeleteAction, is ParenthesisAction -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    is ConstantAction -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
    is FunctionAction -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
}
