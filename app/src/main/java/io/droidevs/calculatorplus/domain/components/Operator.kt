package io.droidevs.calculatorplus.domain.components

import io.droidevs.calculatorplus.domain.token.OperatorToken

abstract class Operator(text: String) : Component(text= text) {

    companion object {
        fun getAll(): List<Operator> {
            return listOf(Plus, Minus, Multiply, Divide, Percent)
        }

        fun isOperator(input: String): Boolean {
            return getAll().any { it.text == input }
        }
    }

    object Plus : Operator("+")

    object Minus : Operator("-")

    object Multiply : Operator("*")

    object Divide : Operator("/")

    object Percent : Operator("%")

    object Factorial : Operator("!")

    object Power : Operator("^")

}

fun Operator.toToken(): OperatorToken {
    return when {
        isPlus() -> OperatorToken.PlusToken()
        isMinus() -> OperatorToken.MinusToken()
        isMultiply() -> OperatorToken.MultiplyToken()
        isDivide() -> OperatorToken.DivideToken()
        isPercent() -> OperatorToken.PercentToken()
        //Operator.Factorial -> OperatorToken.FactorialToken()
        isPower() -> OperatorToken.PowerToken()
        else -> { throw IllegalArgumentException("Invalid operator: $this") }
    }
}

fun Operator.isPlus(): Boolean = this is Operator.Plus
fun Operator.isMinus(): Boolean = this is Operator.Minus
fun Operator.isMultiply(): Boolean = this is Operator.Multiply
fun Operator.isDivide(): Boolean = this is Operator.Divide
fun Operator.isPercent(): Boolean = this is Operator.Percent
fun Operator.isFactorial(): Boolean = this is Operator.Factorial
fun Operator.isPower(): Boolean = this is Operator.Power

