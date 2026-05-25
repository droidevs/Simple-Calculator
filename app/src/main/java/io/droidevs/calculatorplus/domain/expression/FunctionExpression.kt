package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.TrigMode
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

class FunctionExpression(
    private val function: ClcFunction,
    private val argument: Expression,
    // BUG FIX #3: TrigMode injected so trig functions respect RAD/DEG setting.
    // Previously hardcoded to radians with no way to switch.
    private val trigMode: TrigMode = TrigMode.RADIANS
) : Expression() {

    override fun evaluate(): BigDecimal {
        val argValue = argument.evaluate().toDouble()

        // BUG FIX #22: Converting BigDecimal → Double loses precision for extreme values.
        // Guard against the conversion producing Infinity or NaN before applying the function.
        if (argValue.isInfinite() || argValue.isNaN()) {
            throw ArithmeticException("Argument is out of range for ${function.text}")
        }

        // BUG FIX #3: Convert degrees to radians for trig functions when in DEG mode.
        val radValue = when {
            trigMode == TrigMode.DEGREES && isTrigFunction(function) -> Math.toRadians(argValue)
            else -> argValue
        }

        val result = when (function) {
            ClcFunction.Sin -> sin(radValue)
            ClcFunction.Cos -> cos(radValue)
            ClcFunction.Tan -> {
                // cos(x) == 0 means tan is undefined (e.g. tan(90°))
                val cosVal = cos(radValue)
                if (abs(cosVal) < 1e-15) throw ArithmeticException("tan is undefined at this angle")
                tan(radValue)
            }

            ClcFunction.Ln -> {
                if (argValue <= 0) throw ArithmeticException("ln() requires a positive argument")
                ln(argValue)
            }
            ClcFunction.Log -> {
                if (argValue <= 0) throw ArithmeticException("log() requires a positive argument")
                log10(argValue)
            }

            ClcFunction.SquareRoot -> {
                if (argValue < 0) throw ArithmeticException("sqrt() requires a non-negative argument")
                sqrt(argValue)
            }
            ClcFunction.Absolute -> abs(argValue)

            ClcFunction.ASin -> {
                if (argValue !in -1.0..1.0) throw ArithmeticException("asin() domain: [-1, 1]")
                val r = asin(argValue)
                if (trigMode == TrigMode.DEGREES) Math.toDegrees(r) else r
            }
            ClcFunction.ACos -> {
                if (argValue !in -1.0..1.0) throw ArithmeticException("acos() domain: [-1, 1]")
                val r = acos(argValue)
                if (trigMode == TrigMode.DEGREES) Math.toDegrees(r) else r
            }
            ClcFunction.ATan -> {
                val r = atan(argValue)
                if (trigMode == TrigMode.DEGREES) Math.toDegrees(r) else r
            }

            ClcFunction.SinH -> sinh(argValue)
            ClcFunction.CosH -> cosh(argValue)
            ClcFunction.TanH -> tanh(argValue)

            ClcFunction.ASinH -> asinh(argValue)
            ClcFunction.ACosH -> {
                if (argValue < 1.0) throw ArithmeticException("acosh() requires argument >= 1")
                acosh(argValue)
            }
            ClcFunction.ATanH -> {
                if (argValue !in -1.0..1.0) throw ArithmeticException("atanh() domain: (-1, 1)")
                atanh(argValue)
            }

            else -> throw UnsupportedOperationException("Function ${function.text} is not supported")
        }

        // BUG FIX #22: Check result validity before wrapping in BigDecimal.
        if (result.isNaN() || result.isInfinite()) {
            throw ArithmeticException("${function.text}() produced an undefined result")
        }

        return BigDecimal(result, MathContext.DECIMAL64)
    }

    private fun isTrigFunction(fn: ClcFunction): Boolean = fn is ClcFunction.Sin ||
            fn is ClcFunction.Cos || fn is ClcFunction.Tan

    // Kotlin stdlib doesn't have asinh/acosh/atanh as top-level — implement manually.
    private fun asinh(x: Double) = ln(x + sqrt(x * x + 1.0))
    private fun acosh(x: Double) = ln(x + sqrt(x * x - 1.0))
    private fun atanh(x: Double) = 0.5 * ln((1.0 + x) / (1.0 - x))
}