package io.droidevs.calculatorplus.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.droidevs.calculatorplus.ui.action.Action

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CalculatorButton(
    action: Action,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: (Action) -> Unit
) {
    BoxWithConstraints(modifier = modifier.padding(1.dp)) {
        val density = LocalDensity.current
        val baseSize = minOf(maxWidth, maxHeight)
        val fontSize = with(density) { (baseSize * 0.42f).toSp() }
            .value
            .coerceIn(10f, 16f)
            .sp

        Button(
            onClick = {
                onClick(action)
            },
            colors = ButtonDefaults.buttonColors(containerColor = color),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = action.text,
                fontSize = fontSize,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
