package io.droidevs.calculatorplus.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.errors.AppError
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.ConstantToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDecimal
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOperator
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import io.droidevs.calculatorplus.domain.token.refreshIndicesFromThisAsHead
import io.droidevs.calculatorplus.domain.usecases.ClearUseCase
import io.droidevs.calculatorplus.domain.usecases.DecimalUseCase
import io.droidevs.calculatorplus.domain.usecases.DeleteUseCase
import io.droidevs.calculatorplus.domain.usecases.DigitUseCase
import io.droidevs.calculatorplus.domain.usecases.FunctionUseCase
import io.droidevs.calculatorplus.domain.usecases.OperationUseCase
import io.droidevs.calculatorplus.domain.usecases.ParenthesesUseCase
import io.droidevs.calculatorplus.ui.action.Action
import io.droidevs.calculatorplus.ui.action.ClearAction
import io.droidevs.calculatorplus.ui.action.ConstantAction
import io.droidevs.calculatorplus.ui.action.CursorPositionAction
import io.droidevs.calculatorplus.ui.action.DecimalAction
import io.droidevs.calculatorplus.ui.action.DeleteAction
import io.droidevs.calculatorplus.ui.action.DigitAction
import io.droidevs.calculatorplus.ui.action.EqualsAction
import io.droidevs.calculatorplus.ui.action.FunctionAction
import io.droidevs.calculatorplus.ui.action.OperatorAction
import io.droidevs.calculatorplus.ui.action.ParenthesisAction
import io.droidevs.calculatorplus.ui.action.ToggleSignAction
import io.droidevs.calculatorplus.ui.model.HistoryUi
import io.droidevs.calculatorplus.ui.state.CalculatorState
import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorViewModel(
    private val tokenizerFormatter: TokenizerFormatterService = TokenizerFormatterService(),
    private val displayFormatter: ExpressionDisplayFormatter = ExpressionDisplayFormatter(),
    private val evaluator: EvaluatorService = EvaluatorService(),
    private val clear: ClearUseCase = ClearUseCase(),
    private val delete: DeleteUseCase = DeleteUseCase(tokenizerFormatter, displayFormatter, evaluator),
    private val digit: DigitUseCase = DigitUseCase(tokenizerFormatter, displayFormatter, evaluator),
    private val decimal: DecimalUseCase = DecimalUseCase(tokenizerFormatter, displayFormatter, evaluator),
    private val parenthesis: ParenthesesUseCase = ParenthesesUseCase(tokenizerFormatter, displayFormatter, evaluator),
    private val operation: OperationUseCase = OperationUseCase(tokenizerFormatter, displayFormatter, evaluator),
    private val function: FunctionUseCase = FunctionUseCase(tokenizerFormatter, displayFormatter, evaluator),
) : ViewModel() {

    private var calculation: Calculation = Calculation()

    var state by mutableStateOf(CalculatorState())
        private set

    var history by mutableStateOf(emptyList<HistoryUi>())
        private set

    init {
        syncState()
    }

    fun onAction(action: Action) {
        when (action) {
            is DigitAction -> update(digit(calculation, action.toDigit()))
            is OperatorAction -> update(operation.invoke(calculation, action.toOperator(), calculation.pos))
            is DecimalAction -> update(decimal.invoke(calculation, calculation.pos))
            is ParenthesisAction -> update(parenthesis.invoke(calculation, calculation.pos))
            is DeleteAction -> update(deleteAtCursor())
            is ClearAction -> clearAll()
            is FunctionAction -> handleFunctionAction(action)
            is ConstantAction -> update(insertConstant(action.toConstant()))
            is CursorPositionAction -> updateCursor(action.position)
            is ToggleSignAction -> update(toggleSign())
            is EqualsAction -> finalizeCalculation()
            else -> Unit
        }
    }

    fun onHistoryItemClick(item: HistoryUi) {
        val tokens = tokenizerFormatter.format(item.expression)
        val expPair = displayFormatter.format(tokens)
        val formattedExp = expPair.second.toString()
        val eval = evaluator.evaluate(tokens)
        calculation = eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = tokens.headToken(),
                    expression = formattedExp,
                    pos = formattedExp.length,
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                Calculation(
                    tokens = tokens.headToken(),
                    expression = formattedExp,
                    pos = formattedExp.length,
                    result = calculation.result,
                    error = error
                )
            }
        )
        syncState()
    }

    fun onHistoryItemDelete(item: HistoryUi) {
        history = history.filterNot { it.timeStamp == item.timeStamp }
    }

    fun onClearHistory() {
        history = emptyList()
    }

    private fun handleFunctionAction(action: FunctionAction) {
        when (action) {
            FunctionAction.Square -> update(applySquare())
            FunctionAction.Power -> update(operation.invoke(calculation, Operator.Power, calculation.pos))
            FunctionAction.PowerE -> update(applyPowerE())
            FunctionAction.OneDevideX -> update(applyReciprocal())
            else -> update(function.invoke(calculation, action.toFunction()))
        }
    }

    private fun clearAll() {
        clear.invoke()
        calculation = Calculation()
        syncState()
    }

    private fun finalizeCalculation() {
        if (calculation.expression.isNotBlank() && calculation.error == null) {
            history = listOf(
                HistoryUi(
                    expression = calculation.expression,
                    result = formatResult(calculation.result),
                    timeStamp = System.currentTimeMillis(),
                    isFavored = false
                )
            ) + history
        }
        syncState()
    }

    private fun update(newCalculation: Calculation) {
        calculation = newCalculation
        syncState()
    }

    private fun updateCursor(position: Int) {
        val max = calculation.expression.length
        calculation = calculation.copy(pos = position.coerceIn(0, max))
        syncState()
    }

    private fun syncState() {
        state = CalculatorState(
            expression = calculation.expression.ifBlank { "0" },
            result = formatResult(calculation.result),
            cursorPosition = calculation.pos,
            errorMessage = errorMessage(calculation.error)
        )
    }

    private fun formatResult(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() < 0) {
            stripped.setScale(0, RoundingMode.HALF_UP).toPlainString()
        } else {
            stripped.toPlainString()
        }
    }

    private fun errorMessage(error: AppError?): String? {
        return when (error) {
            null -> null
            else -> "Invalid expression"
        }
    }

    private fun applySquare(): Calculation {
        val withPower = operation.invoke(calculation, Operator.Power, calculation.pos)
        return digit(withPower, Digit.Two)
    }

    private fun applyPowerE(): Calculation {
        var next = insertConstant(Constant.E)
        next = operation.invoke(next, Operator.Power, next.pos)
        next = parenthesis.invoke(next, next.pos)
        return next
    }

    private fun applyReciprocal(): Calculation {
        var next = digit(calculation, Digit.One)
        next = operation.invoke(next, Operator.Divide, next.pos)
        next = parenthesis.invoke(next, next.pos)
        return next
    }

    private fun insertConstant(constant: Constant): Calculation {
        val currentTokens = calculation.tokens.headToken()
        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        var adjustedPos = rawPos
        var out = currentTokens

        val prev = currentTokens.find { it.endIndex == rawPos - 1 } ?: currentTokens.prev ?: SpecialToken.EmptyToken()
        if (prev.isDigit() || prev.isCloseParenthesis() || prev is ConstantToken) {
            out = out.insertAt(adjustedPos, OperatorToken.MultiplyToken())
            adjustedPos += 1
        }
        out = out.insertAt(adjustedPos, ConstantToken(constant))
        adjustedPos += constant.text.length

        val expPair = displayFormatter.format(out)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()
        val eval = evaluator.evaluate(out)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = out.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = out.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    error = error
                )
            }
        )
    }

    private fun deleteAtCursor(): Calculation {
        val pos = if (calculation.pos == 0 && calculation.expression.isNotBlank()) {
            calculation.expression.length
        } else {
            calculation.pos
        }
        return delete.invoke(calculation, pos)
    }

    private fun toggleSign(): Calculation {
        val currentTokens = calculation.tokens.headToken()
        if (currentTokens.isEmpty()) {
            return operation.invoke(calculation, Operator.Minus, calculation.pos)
        }

        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        val pivot = findNumberPivot(currentTokens, rawPos)
            ?: return operation.invoke(calculation, Operator.Minus, calculation.pos)

        var start = pivot
        while (start.prev != null && start.prev!!.isNotEmpty() && isNumberToken(start.prev!!)) {
            start = start.prev!!
        }

        var end = pivot
        while (end.next != null && end.next!!.isNotEmpty() && isNumberToken(end.next!!)) {
            end = end.next!!
        }

        val beforeStart = start.prev
        val afterEnd = end.next

        val hasWrappedNegative = beforeStart is ParenthesisToken.OpenParenthesisToken &&
            beforeStart.prev is OperatorToken.MinusToken &&
            afterEnd is ParenthesisToken.CloseParenthesisToken

        val hasLeadingMinus = beforeStart is OperatorToken.MinusToken &&
            (beforeStart.prev == null || beforeStart.prev!!.isOperator() || beforeStart.prev!!.isOpenParenthesis() || beforeStart.prev!!.isEmpty())

        var updated = currentTokens
        val newRawPos: Int

        when {
            hasWrappedNegative -> {
                val minusToken = beforeStart.prev as OperatorToken.MinusToken
                removeToken(afterEnd)
                removeToken(beforeStart)
                updated = removeToken(minusToken)
                newRawPos = (rawPos - 2).coerceAtLeast(0)
            }

            hasLeadingMinus -> {
                updated = removeToken(beforeStart)
                newRawPos = (rawPos - 1).coerceAtLeast(0)
            }

            else -> {
                val shouldWrap = beforeStart != null &&
                    (beforeStart.isDigit() || beforeStart is ConstantToken || beforeStart.isCloseParenthesis())

                if (shouldWrap) {
                    updated = updated.insertAt(end.endIndex + 1, ParenthesisToken.CloseParenthesisToken())
                    updated = updated.insertAt(start.startIndex, OperatorToken.MinusToken())
                    updated = updated.insertAt(start.startIndex, ParenthesisToken.OpenParenthesisToken())
                    newRawPos = rawPos + 2
                } else {
                    updated = updated.insertAt(start.startIndex, OperatorToken.MinusToken())
                    newRawPos = rawPos + 1
                }
            }
        }

        val head = updated.headToken().apply { refreshIndicesFromThisAsHead() }
        val expPair = displayFormatter.format(head)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()
        val adjustedPos = newRawPos.coerceAtMost(rawExp.length)
        val eval = evaluator.evaluate(head)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = head,
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = head,
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    error = error
                )
            }
        )
    }

    private fun findNumberPivot(head: LinkedToken, rawPos: Int): LinkedToken? {
        val inToken = head.find { rawPos in it.startIndex..it.endIndex }
        if (inToken != null && isNumberToken(inToken)) return inToken
        val prev = head.find { it.endIndex == rawPos - 1 }
        return if (prev != null && isNumberToken(prev)) prev else null
    }

    private fun isNumberToken(token: LinkedToken): Boolean {
        return token.isDigit() || token.isDecimal() || token is ConstantToken
    }

    private fun removeToken(token: LinkedToken): LinkedToken {
        val prev = token.prev
        val next = token.next
        prev?.next = next
        next?.prev = prev
        return (next ?: prev ?: SpecialToken.EmptyToken()).headToken()
    }
}

private fun DigitAction.toDigit() = when (text) {
    "0" -> Digit.Zero
    "1" -> Digit.One
    "2" -> Digit.Two
    "3" -> Digit.Three
    "4" -> Digit.Four
    "5" -> Digit.Five
    "6" -> Digit.Six
    "7" -> Digit.Seven
    "8" -> Digit.Eight
    "9" -> Digit.Nine
    else -> throw IllegalArgumentException("Invalid digit: $text")
}

private fun OperatorAction.toOperator(): Operator = when (this) {
    OperatorAction.Plus -> Operator.Plus
    OperatorAction.Minus -> Operator.Minus
    OperatorAction.Multiply -> Operator.Multiply
    OperatorAction.Divide -> Operator.Divide
    OperatorAction.Percent -> Operator.Percent
    OperatorAction.Power -> Operator.Power
    OperatorAction.Factorial -> Operator.Factorial
    else -> throw IllegalArgumentException("Invalid operator action: $this")
}

private fun FunctionAction.toFunction(): ClcFunction = when (this) {
    FunctionAction.Sin -> ClcFunction.Sin
    FunctionAction.Cos -> ClcFunction.Cos
    FunctionAction.Tan -> ClcFunction.Tan
    FunctionAction.Ln -> ClcFunction.Ln
    FunctionAction.Log -> ClcFunction.Log
    FunctionAction.SquareRoot -> ClcFunction.SquareRoot
    FunctionAction.ASin -> ClcFunction.ASin
    FunctionAction.ACos -> ClcFunction.ACos
    FunctionAction.ATan -> ClcFunction.ATan
    FunctionAction.SinH -> ClcFunction.SinH
    FunctionAction.CosH -> ClcFunction.CosH
    FunctionAction.TanH -> ClcFunction.TanH
    FunctionAction.ASinH -> ClcFunction.ASinH
    FunctionAction.ACosH -> ClcFunction.ACosH
    FunctionAction.ATanH -> ClcFunction.ATanH
    else -> throw IllegalArgumentException("Invalid function action: $this")
}

private fun ConstantAction.toConstant(): Constant = when (this) {
    ConstantAction.PI -> Constant.PI
    ConstantAction.E -> Constant.E
    else -> throw IllegalArgumentException("Invalid constant action: $this")
}
