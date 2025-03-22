package io.droidevs.calculatorplus.token

import io.droidevs.calculatorplus.components.ClcFunction
import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.components.Digit
import io.droidevs.calculatorplus.components.Operator
import io.droidevs.calculatorplus.components.Parenthesis
import io.droidevs.calculatorplus.components.Special
import io.droidevs.calculatorplus.expression.ArithmeticOperatorExpression
import io.droidevs.calculatorplus.expression.EmptyExpression
import io.droidevs.calculatorplus.expression.Expression
import io.droidevs.calculatorplus.expression.FunctionExpression
import io.droidevs.calculatorplus.expression.NumberExpression
import io.droidevs.calculatorplus.expression.PercentOperatorExpression
import io.droidevs.calculatorplus.expression.PowerOperatorExpression
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

        private fun provide(component: Component) : LinkedToken? {
            return when(component) {
                is Digit.Zero -> DigitToken.ZeroToken.get()
                is Digit.One -> DigitToken.OneToken.get()
                is Digit.Two -> DigitToken.TwoToken.get()
                is Digit.Three -> DigitToken.ThreeToken.get()
                is Digit.Four -> DigitToken.FourToken.get()
                is Digit.Five -> DigitToken.FiveToken.get()
                is Digit.Six -> DigitToken.SixToken.get()
                is Digit.Seven -> DigitToken.SevenToken.get()
                is Digit.Eight -> DigitToken.EightToken.get()
                is Digit.Nine -> DigitToken.NineToken.get()
                is ClcFunction.Cos -> FunctionToken.CosToken.get()
                is ClcFunction.Sin -> FunctionToken.SinToken.get()
                is ClcFunction.CosH -> FunctionToken.CosHToken.get()
                is ClcFunction.SinH -> FunctionToken.SinHToken.get()
                is ClcFunction.TanH -> FunctionToken.TanHToken.get()
                is ClcFunction.ACos -> FunctionToken.ACosToken.get()
                is ClcFunction.ASin -> FunctionToken.ASinToken.get()
                is ClcFunction.ATan -> FunctionToken.ATanToken.get()
                is ClcFunction.ACosH -> FunctionToken.ACosHToken.get()
                is ClcFunction.ASinH -> FunctionToken.ASinHToken.get()
                is ClcFunction.ATanH -> FunctionToken.ATanHToken.get()
                is ClcFunction.Tan -> FunctionToken.TanToken.get()
                is ClcFunction.Log -> FunctionToken.LogToken.get()
                is Operator.Plus -> OperatorToken.PlusToken.get()
                is Operator.Minus -> OperatorToken.MinusToken.get()
                is Operator.Multiply -> OperatorToken.MultiplyToken.get()
                is Operator.Divide -> OperatorToken.DivideToken.get()
                is Operator.Power -> OperatorToken.PowerToken.get()
                is Operator.Percent -> OperatorToken.PercentToken.get()
                is Parenthesis.OpenParenthesis -> ParenthesisToken.OpenParenthesisToken.get()
                is Parenthesis.CloseParenthesis -> ParenthesisToken.CloseParenthesisToken.get()
                is Special.Decimal -> SpecialToken.DecimalToken.get()
                else -> null
            }
        }

        /**
         * Tokenizes a mathematical expression string into tokens.
         * @param expression The formatted expression string.
         * @return A TokenProvider containing the head of the linked tokens.
         */
        fun tokenize(expression: String): TokenProvider {
            var head: LinkedToken = SpecialToken.EmptyToken()
            var token: LinkedToken? = null

            for (c in expression) {
                val component = Component.identify(c)

                // Handle the current non-number component.
                val newToken = provide(component)
                if (newToken != null) {
                    if (head == SpecialToken.EmptyToken()) {
                        head = newToken
                        token = head
                    } else {
                        token?.next = newToken
                        token = newToken
                    }
                }
            }

            return construct(head)
        }
    }



}