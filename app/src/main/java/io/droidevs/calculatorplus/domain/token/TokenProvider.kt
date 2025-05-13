package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.expression.ArithmeticOperatorExpression
import io.droidevs.calculatorplus.domain.expression.EmptyExpression
import io.droidevs.calculatorplus.domain.expression.Expression
import io.droidevs.calculatorplus.domain.expression.FunctionExpression
import io.droidevs.calculatorplus.domain.expression.NumberExpression
import io.droidevs.calculatorplus.domain.expression.PercentOperatorExpression
import io.droidevs.calculatorplus.domain.expression.PowerOperatorExpression
import kotlin.math.pow


class TokenProvider(val token : LinkedToken) {


    private var ptoken: LinkedToken = token

    /**
     * Entry point to parse a list of tokens into an Expression.
     */
    fun parse(): Expression {
        return parseExpression()
    }

    /**
     * Parses a full expression, handling addition and subtraction.
     */
    fun parseExpression(): Expression {
        // todo : validate the tokens
        var left = parseTerm()

        while (currentToken() == OperatorToken.PlusToken() || currentToken() == OperatorToken.MinusToken()) {
            val operator = consumeToken()!!.component
            val right = parseTerm()
            left = ArithmeticOperatorExpression(operator as Operator, left, right)
        }

        return left
    }

    /**
     * Parses a term, handling multiplication, division, and percent operators.
     */
    private fun parseTerm(): Expression {
        var left = parseFactor()
        var currenttoken = currentToken()
        while(currenttoken != null) {
            when(currenttoken){
                is OperatorToken.MultiplyToken,
                is OperatorToken.DivideToken,
                is OperatorToken.PercentToken -> {
                    val operator = consumeToken().component
                    left = when (operator) {
                        Operator.Percent -> PercentOperatorExpression(left)
                        else -> {
                            val right = parseFactor()
                            ArithmeticOperatorExpression(operator as Operator, left, right)
                        }
                    }
                    currenttoken = currentToken()
                }
                else -> break
            }
        }
        return left
    }

    /**
     * Parses a factor, handling parentheses, numbers, variables, and functions.
     */
    private fun parseFactor(): Expression {
        val current = currentToken()
        when(current) {
            is ParenthesisToken -> {
                if (current.isOpenParenthesis()) {
                    consumeToken() // Skip '('
                    val innerExpression = parseExpression()
                    if (!currentToken().isParenthesis()) {
                        throw IllegalArgumentException("Expected closing parenthesis")
                    }
                    consumeToken() // Skip ')'
                    return innerExpression
                }
                else {
                    return EmptyExpression()
                }
            }
            is DigitToken -> return parseNumber()
            is FunctionToken -> {
                val functionOrVariable = consumeToken().component
                consumeToken()
                if (currentToken().isParenthesis()) {
                    consumeToken() // Skip '('
                    val argument = parseExpression()
                    consumeToken() // Skip ')'
                    FunctionExpression(functionOrVariable as ClcFunction, argument)
                } else {
                    throw IllegalArgumentException("Expected closing parenthesis for function")
                }
            }
            is OperatorToken.PowerToken -> {
                consumeToken() // Skip '^'
                val base = parseFactor()
                val exponent = parseFactor()
                return PowerOperatorExpression(base, exponent)
            }
            else -> {

            }
        }

        throw IllegalArgumentException("Unexpected token: $current")
    }

    /**
     * Parses a number (integer or decimal) into a NumberExpression.
     */
    private fun parseNumber(): NumberExpression {
        val beforeDecimal = mutableListOf<Int>()
        val afterDecimal = mutableListOf<Int>()
        var passedDecimal = false

        while (currentToken() != null && (currentToken().isDigit() || currentToken().isDecimal())) {
            when (val tokenText = consumeToken()!!.component.text) {
                "." -> {
                    if (passedDecimal) throw IllegalArgumentException("Invalid number format: multiple decimals")
                    passedDecimal = true
                }
                else -> {
                    if (!passedDecimal) beforeDecimal.add(tokenText.toInt())
                    else afterDecimal.add(tokenText.toInt())
                }
            }
        }

        var result = 0.0
        val beforeDecimalSize = beforeDecimal.size
        for (i in beforeDecimal.indices) {
            result += beforeDecimal[i] * 10.0.pow((beforeDecimalSize - 1 - i).toDouble())
        }

        for (i in afterDecimal.indices) {
            result += afterDecimal[i] * 10.0.pow(-(i + 1).toDouble())
        }

        return NumberExpression(result)
    }

    /**
     * Gets the current token.
     */
    private fun currentToken(): LinkedToken = ptoken


    /**
     * Consumes and returns the current token, advancing to the next one.
     */
    private fun consumeToken(): LinkedToken {
        val current = ptoken
        ptoken = ptoken.next!!
        return current
    }


    companion object {
        fun construct(token: LinkedToken) : TokenProvider {
            return TokenProvider(token)
        }
    }



}