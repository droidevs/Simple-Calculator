package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


open class FunctionToken(function: ClcFunction) : LinkedToken(function) {


    override fun isValid(argument: ValidationArgument): Boolean {
        val prev = argument.prev
        return when (prev) {
            is Special.Empty -> true
            is Operator -> prev !is Operator.Percent
            is Parenthesis.OpenParenthesis -> true
            else -> false
        }
    }


    class SinToken() : FunctionToken(ClcFunction.Sin) {

        companion object {

            fun get() : SinToken {
                return SinToken()
            }
        }
    }

    class CosToken() : FunctionToken(ClcFunction.Cos) {

        companion object {
            fun get() : CosToken {
                return CosToken()
            }
        }
    }

    class TanToken() : FunctionToken(ClcFunction.Tan) {

        companion object {
            fun get() : TanToken {
                return TanToken()
            }
        }
    }

    class ACosToken() : FunctionToken(ClcFunction.ACos) {

        companion object {
            fun get() : ACosToken {
                return ACosToken()
            }
        }
    }

    class ASinToken() : FunctionToken(ClcFunction.ASin) {

        companion object {
            fun get(): ASinToken {
                return ASinToken()
            }
        }
    }

    class ATanToken() : FunctionToken(ClcFunction.ATan) {

        companion object {
            fun get() : ATanToken {
                return ATanToken()
            }
        }
    }

    class SinHToken() : FunctionToken(ClcFunction.SinH) {

        companion object {
            fun get() : SinHToken {
                return SinHToken()
            }
        }
    }

    class CosHToken() : FunctionToken(ClcFunction.CosH) {

        companion object {
            fun get() : CosHToken {
                return CosHToken()
            }
        }
    }

    class TanHToken() : FunctionToken(ClcFunction.TanH) {

        companion object {
            fun get() : TanHToken {
                return TanHToken()
            }
        }
    }

    class ASinHToken() : FunctionToken(ClcFunction.ASinH) {

        companion object {
            fun get() : ASinHToken {
                return ASinHToken()
            }
        }

    }

    class ACosHToken() : FunctionToken(ClcFunction.ACosH) {

        companion object {
            fun get(): ACosHToken {
                return ACosHToken()
            }
        }

    }

    class ATanHToken() : FunctionToken(ClcFunction.ATanH) {

        companion object {
            fun get(): ATanHToken {
                return ATanHToken()
            }
        }
    }

    class SqrtToken() : FunctionToken( ClcFunction.SquareRoot) {

        companion object {
            fun get() : SqrtToken {
                return SqrtToken()
            }
        }
    }

    class LogToken() : FunctionToken(ClcFunction.Log) {

        companion object {
            fun get() : LogToken {
                return LogToken()
            }
        }
    }

    class LnToken() : FunctionToken(ClcFunction.Ln) {

        companion object {
            fun get() : LnToken {
                return LnToken()
            }
        }

    }

    fun isCosine() : Boolean {
        return this is CosToken
    }

    fun isSine() : Boolean {
        return this is SinToken
    }

    fun isTangent() : Boolean {
        return this is TanToken
    }

    fun isArcCosine() : Boolean {
        return this is ACosToken
    }

    fun isArcSine() : Boolean {
        return this is ASinToken
    }

    fun isArcTangent() : Boolean {
        return this is ATanToken
    }

    fun isHyperbolicSine() : Boolean {
        return this is SinHToken
    }

    fun isHyperbolicCosine() : Boolean {
        return this is CosHToken
    }

    fun isHyperbolicTangent() : Boolean {
        return this is TanHToken
    }

    fun isInverseHyperbolicSine() : Boolean {
        return this is ASinHToken
    }

    fun isInverseHyperbolicCosine() : Boolean {
        return this is ACosHToken
    }

    fun isInverseHyperbolicTangent() : Boolean {
        return this is ATanHToken
    }

    fun isSquareRoot() : Boolean {
        return this is SqrtToken
    }

    fun isNaturalLogarithm() : Boolean {
        return this is LnToken
    }

    fun isCommonLogarithm() : Boolean {
        return this is LogToken
    }


}