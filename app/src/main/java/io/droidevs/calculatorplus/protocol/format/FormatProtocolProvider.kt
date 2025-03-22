package io.droidevs.calculatorplus.protocol.format

import io.droidevs.calculatorplus.components.ClcFunction
import io.droidevs.calcus.domain.model.protocol.format.Format

class FormatProtocolProvider {

    /**
     * Extracts FormatProtocol instances for all Function operators.
     */
    fun extractProtocols(): List<FormatProtocol> {
        return ClcFunction.getAllFunctionOperators().map { function ->
            val replaceValue = when (function.text) {
                "sin" -> Format.SIN
                "cos" -> Format.COS
                "tan" -> Format.TAN
                "log" -> Format.LOG
                "asin" -> Format.ASIN
                "acos" -> Format.ACOS
                "atan" -> Format.ATAN
                "sinh" -> Format.SINH
                "cosh" -> Format.COSH
                "tanh" -> Format.TANH
                "asinh" -> Format.ASINH
                "acosh" -> Format.ACOSH
                "atanh" -> Format.ATANH
                else -> function.text // Default to the function's text if no match is found
            }
            FormatProtocol(function, replace = replaceValue)
        }
    }
}