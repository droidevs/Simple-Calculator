package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
            modifier = Modifier.weight(1.3f)
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
    var showReverse by remember { mutableStateOf(false) }

    val normalActions = listOf(
        FunctionAction.SquareRoot,
        FunctionAction.Square,
        FunctionAction.Power,
        FunctionAction.PowerE,
        FunctionAction.OneDevideX,
        FunctionAction.Sin,
        FunctionAction.Cos,
        FunctionAction.Tan,
        FunctionAction.Ln,
        FunctionAction.Log,
        ConstantAction.PI,
        ConstantAction.E
    )

    val reverseActions = listOf(
        FunctionAction.ASin,
        FunctionAction.ACos,
        FunctionAction.ATan,
        FunctionAction.SinH,
        FunctionAction.CosH,
        FunctionAction.TanH,
        FunctionAction.ASinH,
        FunctionAction.ACosH,
        FunctionAction.ATanH
    )

    val actions = if (showReverse) reverseActions else normalActions
    val rows = actions.chunked(4).map { row ->
        if (row.size < 4) row + List(4 - row.size) { null } else row
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Normal", style = MaterialTheme.typography.labelMedium)
            Switch(
                checked = showReverse,
                onCheckedChange = { showReverse = it },
                modifier = Modifier.width(52.dp)
            )
            Text(text = "Reverse", style = MaterialTheme.typography.labelMedium)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    row.forEach { action ->
                        if (action != null) {
                            val (background, textColor) = actionColors(action)
                            CalculatorButton(
                                action = action,
                                color = background,
                                textColor = textColor,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClick = onAction
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
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
