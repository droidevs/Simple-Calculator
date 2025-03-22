package io.droidevs.calculatorplus.expression

import io.droidevs.calculatorplus.components.ClcFunction
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

class FunctionExpression(
    private val function: ClcFunction,
    private val argument: Expression
) : Expression() {

    override fun evaluate(): BigDecimal {
        val argValue = argument.evaluate().toDouble() // Convert BigDecimal to Double

        val result = when (function) {
            ClcFunction.Sin -> sin(argValue)
            ClcFunction.Cos -> cos(argValue)
            ClcFunction.Tan -> tan(argValue)

            ClcFunction.Ln -> if (argValue > 0) ln(argValue) else throw ArithmeticException("ln() of negative number")
            ClcFunction.Log -> if (argValue > 0) log10(argValue) else throw ArithmeticException("log() of negative number")

            ClcFunction.SquareRoot -> if (argValue >= 0) sqrt(argValue) else throw ArithmeticException("sqrt() of negative number")
            ClcFunction.Absolute -> abs(argValue)

            ClcFunction.ASin -> if (argValue in -1.0..1.0) asin(argValue) else throw ArithmeticException("asin() domain error")
            ClcFunction.ACos -> if (argValue in -1.0..1.0) acos(argValue) else throw ArithmeticException("acos() domain error")
            ClcFunction.ATan -> atan(argValue)

            ClcFunction.SinH -> sinh(argValue)
            ClcFunction.CosH -> cosh(argValue)
            ClcFunction.TanH -> tanh(argValue)

            ClcFunction.ASinH -> asinh(argValue)
            ClcFunction.ACosH -> acosh(argValue)
            ClcFunction.ATanH -> atanh(argValue)

            else -> throw UnsupportedOperationException("Function ${function.text} is not supported")
        }

        return BigDecimal(result, MathContext.DECIMAL64) // Convert back to BigDecimal
    }
}
