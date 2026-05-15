package io.droidevs.calculatorplus.ui.component


import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.DecimalAction
import io.droidevs.calculatorplus.ui.action.DigitAction
import io.droidevs.calculatorplus.ui.action.EqualsAction
import io.droidevs.calculatorplus.ui.action.OperatorAction

@Composable
fun CalculatorButton(
    action: Action,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: (Action) -> Unit
) {
    Button(
        onClick = {
            onClick(action)
        },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f) // Ensures square buttons
    ) {
        Text(text = action.text, fontSize = 24.sp, color = textColor)
    }
}
