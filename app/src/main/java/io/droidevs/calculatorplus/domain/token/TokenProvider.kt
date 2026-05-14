package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.expression.ArithmeticOperatorExpression
import io.droidevs.calculatorplus.domain.expression.Expression
import io.droidevs.calculatorplus.domain.expression.FunctionExpression
import io.droidevs.calculatorplus.domain.expression.NumberExpression
import io.droidevs.calculatorplus.domain.expression.PercentOperatorExpression
import io.droidevs.calculatorplus.domain.expression.PowerOperatorExpression
import io.droidevs.calculatorplus.domain.result.Result
import io.droidevs.calculatorplus.domain.result.errors.InvalidExpressionFormat
import io.droidevs.calculatorplus.domain.result.fold
import java.math.BigDecimal
import java.math.MathContext


class TokenProvider(private val token: LinkedToken) {

    private var current: LinkedToken? = token

    fun parse(): Result<Expression> {
        val validation = token.validate()
        if (validation.isInvalid()) return Result.Error(InvalidExpressionFormat())

        val expr = parseExpression()
        return expr.fold(
            onSuccess = {
                if (current?.isNotEmpty() == true) Result.Error(InvalidExpressionFormat()) else Result.Success(it)
            },
            onFailure = { Result.Error(it) }
        )
    }

    private fun parseExpression(): Result<Expression> {
        var leftRes = parseTerm()
        if (leftRes is Result.Error) return leftRes

        var left = (leftRes as Result.Success).result

        while (true) {
            val op = currentOperator()
            if (op !is Operator.Plus && op !is Operator.Minus) break

            consume()
            val rightRes = parseTerm()
            if (rightRes is Result.Error) return rightRes

            val right = (rightRes as Result.Success).result
            left = ArithmeticOperatorExpression(op, left, right)
        }

        return Result.Success(left)
    }

    private fun parseTerm(): Result<Expression> {
        var leftRes = parsePower()
        if (leftRes is Result.Error) return leftRes

        var left = (leftRes as Result.Success).result

        while (true) {
            val op = currentOperator()
            if (op !is Operator.Multiply && op !is Operator.Divide) break

            consume()
            val rightRes = parsePower()
            if (rightRes is Result.Error) return rightRes

            val right = (rightRes as Result.Success).result
            left = ArithmeticOperatorExpression(op, left, right)
        }

        return Result.Success(left)
    }

    private fun parsePower(): Result<Expression> {
        val baseRes = parseUnary()
        if (baseRes is Result.Error) return baseRes

        val base = (baseRes as Result.Success).result
        val op = currentOperator()

        if (op is Operator.Power) {
            consume()
            val expRes = parsePower()
            if (expRes is Result.Error) return expRes
            val exp = (expRes as Result.Success).result
            return Result.Success(PowerOperatorExpression(base, exp))
        }

        return Result.Success(base)
    }

    private fun parseUnary(): Result<Expression> {
        val op = currentOperator()

        return when (op) {
            is Operator.Plus -> {
                consume()
                parseUnary()
            }

            is Operator.Minus -> {
                consume()
                val innerRes = parseUnary()
                if (innerRes is Result.Error) innerRes
                else {
                    val inner = (innerRes as Result.Success).result
                    Result.Success(
                        ArithmeticOperatorExpression(
                            Operator.Minus,
                            NumberExpression(BigDecimal.ZERO),
                            inner
                        )
                    )
                }
            }

            else -> parsePostfix()
        }
    }

    private fun parsePostfix(): Result<Expression> {
        val primaryRes = parsePrimary()
        if (primaryRes is Result.Error) return primaryRes

        var expr = (primaryRes as Result.Success).result

        while (true) {
            val op = currentOperator()
            if (op !is Operator.Percent) break

            consume()
            expr = PercentOperatorExpression(expr)
        }

        return Result.Success(expr)
    }

    private fun parsePrimary(): Result<Expression> {
        val t = current ?: return Result.Error(InvalidExpressionFormat())

        return when (t) {
            is ParenthesisToken.OpenParenthesisToken -> {
                consume()
                val inner = parseExpression()
                if (inner is Result.Error) return inner

                if (current !is ParenthesisToken.CloseParenthesisToken) return Result.Error(InvalidExpressionFormat())
                consume()
                inner
            }

            is DigitToken,
            is SpecialToken.DecimalToken -> parseNumber()

            is ConstantToken -> {
                val constant = t.component as Constant
                consume()
                Result.Success(NumberExpression(BigDecimal.valueOf(constant.apply())))
            }

            is FunctionToken -> {
                val fn = t.component as ClcFunction
                consume()

                if (current !is ParenthesisToken.OpenParenthesisToken) return Result.Error(InvalidExpressionFormat())
                consume()

                val argRes = parseExpression()
                if (argRes is Result.Error) return argRes

                if (current !is ParenthesisToken.CloseParenthesisToken) return Result.Error(InvalidExpressionFormat())
                consume()

                Result.Success(FunctionExpression(fn, (argRes as Result.Success).result))
            }

            else -> Result.Error(InvalidExpressionFormat())
        }
    }

    private fun parseNumber(): Result<Expression> {
        val sb = StringBuilder()
        var seenDecimal = false

        while (current?.isNotEmpty() == true && (current!!.isDigit() || current!!.isDecimal())) {
            val text = current!!.component.text
            if (text == ".") {
                if (seenDecimal) return Result.Error(InvalidExpressionFormat())
                seenDecimal = true
                sb.append('.')
            } else {
                sb.append(text)
            }
            consume()
        }

        val numText = sb.toString()
        if (numText.isBlank() || numText == ".") return Result.Error(InvalidExpressionFormat())

        return try {
            Result.Success(NumberExpression(BigDecimal(numText, MathContext.DECIMAL64)))
        } catch (_: Throwable) {
            Result.Error(InvalidExpressionFormat())
        }
    }

    private fun currentOperator(): Operator? {
        val opToken = current as? OperatorToken ?: return null
        return opToken.component as? Operator
    }

    private fun consume(): LinkedToken {
        val t = current ?: return SpecialToken.EmptyToken()
        current = t.next
        return t
    }

    companion object {
        fun construct(token: LinkedToken): TokenProvider = TokenProvider(token)
    }
}