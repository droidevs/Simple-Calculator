package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


open class DigitToken(digit: Digit) : LinkedToken(digit) {


    override fun isValid(argument: ValidationArgument): Boolean {
        val prev = argument.prev
        return when (prev) {
            is Special.Empty,
            is Digit,
            is Special.Decimal,
            is Operator,
            is Parenthesis.OpenParenthesis -> true

            else -> false
        }
    }


    class OneToken() : DigitToken(Digit.One) {

        override fun isValid(argument: ValidationArgument): Boolean {
            return super.isValid(argument)
        }
        companion object {
            fun get() : OneToken {
                return OneToken()
            }
        }
    }

    class TwoToken() : DigitToken(Digit.Two) {
        companion object {
            fun get() : TwoToken {
                return TwoToken()
            }
        }
    }

    class ThreeToken() : DigitToken(Digit.Three) {
        companion object {
            fun get() : ThreeToken {
                return ThreeToken()
            }
        }
    }

    class FourToken() : DigitToken(Digit.Four) {
        companion object {
            fun get() : FourToken {
                return FourToken()
            }
        }
    }

    class FiveToken() : DigitToken(Digit.Five) {

        companion object {
            fun get() : FiveToken {
                return FiveToken()
            }
        }
    }

    class SixToken() : DigitToken(Digit.Six) {

        companion object {
            fun get() : SixToken {
                return SixToken()
            }
        }
    }

    class SevenToken() : DigitToken(Digit.Seven) {

        companion object {
            fun get() : SevenToken {
                return SevenToken()
            }
        }
    }

    class EightToken() : DigitToken(Digit.Eight) {

        companion object {
            fun get() : EightToken {
                return EightToken()
            }
        }
    }

    class NineToken() : DigitToken(Digit.Nine) {

        companion object {
            fun get() : NineToken {
                return NineToken()
            }
        }
    }

    class ZeroToken() : DigitToken(Digit.Zero) {

        companion object {
            fun get() : ZeroToken {
                return ZeroToken()
            }
        }
    }

    fun isZero(): Boolean {
        return this is ZeroToken
    }

    fun isOne(): Boolean {
        return this is OneToken
    }

    fun isTwo(): Boolean {
        return this is TwoToken
    }

    fun isThree(): Boolean {
        return this is ThreeToken
    }

    fun isFour(): Boolean {
        return this is FourToken
    }

    fun isFive(): Boolean {
        return this is FiveToken
    }

    fun isSix(): Boolean {
        return this is SixToken
    }

    fun isSeven(): Boolean {
        return this is SevenToken
    }

    fun isEight(): Boolean {
        return this is EightToken
    }

    fun isNine(): Boolean {
        return this is NineToken
    }


}