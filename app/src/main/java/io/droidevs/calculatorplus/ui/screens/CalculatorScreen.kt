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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.droidevs.calculatorplus.domain.components.TrigMode
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

    // BUG FIX #25: Landscape display weight was 0.10f — only ~6dp tall on phone.
    // Changed to 0.22f minimum in landscape so expression and result are always readable.
    val displayWeight = when (layoutMode) {
        LayoutMode.PHONE_PORTRAIT -> 0.35f
        else -> 0.22f
    }
    val keyboardWeight = 1f - displayWeight

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            if (layoutMode == LayoutMode.PHONE_PORTRAIT) 12.dp else 8.dp
        )
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

            // BUG FIX #13: The original code used `remember(displayExpression, clampedCursor)`
            // to create a TextFieldValue — this RECREATES the object on every keystroke,
            // resetting the cursor blink animation and causing visible jitter.
            //
            // Fix: Use a local mutableStateOf for the TextFieldValue, and only push external
            // changes (from the ViewModel) when they differ from the current local state.
            // This preserves the cursor blink while still syncing ViewModel-driven changes.
            var textFieldValue by remember { mutableStateOf(TextFieldValue(displayExpression, TextRange(clampedCursor))) }

            // Sync external changes (e.g. history tap, clear) into local state
            val externalValue = TextFieldValue(displayExpression, TextRange(clampedCursor))
            if (textFieldValue.text != externalValue.text) {
                textFieldValue = externalValue
            }

            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    // Only propagate cursor movement, not text edits (keyboard is custom)
                    if (newValue.text == textFieldValue.text) {
                        textFieldValue = newValue
                        val newPos = newValue.selection.end
                        if (newPos != clampedCursor) {
                            onAction(CursorPositionAction(newPos))
                        }
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // BUG FIX #3: Display the active trig mode so users know what sin/cos/tan use
            Text(
                text = if (state.trigMode == TrigMode.RADIANS) "RAD" else "DEG",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row {
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