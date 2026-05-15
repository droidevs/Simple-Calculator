package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.CursorPositionAction
import io.droidevs.calculatorplus.ui.action.DeleteAction
import io.droidevs.calculatorplus.ui.state.CalculatorState
import io.droidevs.calculatorplus.ui.window.LayoutMode
import io.droidevs.calculatorplus.ui.window.LocalWindow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (Action) -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val layoutMode = LocalWindow.current.layoutMode
    val displayWeight = if (layoutMode == LayoutMode.PHONE_PORTRAIT) 0.35f else 0.10f
    val keyboardWeight = 1f - displayWeight
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(if (layoutMode == LayoutMode.PHONE_PORTRAIT) 12.dp else 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(displayWeight)
                .weight(displayWeight),
            verticalArrangement = Arrangement.Bottom
        ) {
            val displayExpression = state.expression
            val clampedCursor = state.cursorPosition.coerceIn(0, displayExpression.length)
            val value = remember(displayExpression, clampedCursor) {
                TextFieldValue(text = displayExpression, selection = TextRange(clampedCursor))
            }

            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    val newPos = newValue.selection.end
                    if (newPos != clampedCursor) {
                        onAction(CursorPositionAction(newPos))
                    }
                },
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.isFocused) keyboardController?.hide() },
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = displayExpression,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { onAction(DeleteAction()) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(keyboardWeight)
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
