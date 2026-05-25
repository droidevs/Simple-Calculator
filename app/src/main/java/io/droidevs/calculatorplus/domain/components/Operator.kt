package io.droidevs.calculatorplus.domain.components

import io.droidevs.calculatorplus.domain.token.OperatorToken

sealed class Operator(text: String) : Component(text = text) {

    companion object {
        fun getAll(): List<Operator> = listOf(Plus, Minus, Multiply, Divide, Percent)

        fun isOperator(input: String): Boolean = getAll().any { it.text == input }
    }

    object Plus : Operator("+")
    object Minus : Operator("-")

    // BUG FIX #8/#19: Changed from "*" to "×" and "/" to "÷" so the display formatter
    // shows proper mathematical symbols. Component.identify() maps both the ASCII chars
    // ('*', '/') and the display chars ('×', '÷') to these operators so tokenization
    // still works when expressions are re-parsed from the stored formatted string.
    object Multiply : Operator("×")
    object Divide : Operator("÷")

    object Percent : Operator("%")
    object Factorial : Operator("!")
    object Power : Operator("^")
}

fun Operator.toToken(): OperatorToken = when {
    isPlus() -> OperatorToken.PlusToken()
    isMinus() -> OperatorToken.MinusToken()
    isMultiply() -> OperatorToken.MultiplyToken()
    isDivide() -> OperatorToken.DivideToken()
    isPercent() -> OperatorToken.PercentToken()
    isPower() -> OperatorToken.PowerToken()
    else -> throw IllegalArgumentException("Invalid operator: $this")
}

fun Operator.isPlus() = this is Operator.Plus
fun Operator.isMinus() = this is Operator.Minus
fun Operator.isMultiply() = this is Operator.Multiply
fun Operator.isDivide() = this is Operator.Divide
fun Operator.isPercent() = this is Operator.Percent
fun Operator.isFactorial() = this is Operator.Factorial
fun Operator.isPower() = this is Operator.Power