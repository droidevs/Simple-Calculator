package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


open class DigitToken(digit: Digit) : LinkedToken(digit) {


    override fun isValid(argument: ValidationArgument): Boolean {
        return validateDigit(argument.prev,argument.current as Digit)
    }

    private fun validateDigit(prev: Component?, digit: Digit): Boolean {
        return when (prev) {
            // Rule 1: If the previous component is an operator, validate based on operator rules
            is Operator -> {
                // Delegate to the operator-specific validation logic
                validateDigit(prev, digit)
            }

            // Rule 2: If the previous component is a parenthesis, validate based on parenthesis rules
            is Parenthesis -> {
                // Delegate to the parenthesis-specific validation logic
                validateDigit(prev, digit)
            }

            // Rule 3: If the previous component is null or not a recognized type, it's invalid
            else -> {
                // Invalid because the preceding component is not a valid context for a digit
                false
            }
        }
    }
    private fun validateDigit(prev: Parenthesis, digit: Digit): Boolean {
        return when (prev) {
            // Rule 1: A digit is valid immediately after an open parenthesis
            is Parenthesis.OpenParenthesis -> {
                // Valid because a digit can follow an open parenthesis
                true
            }

            // Rule 2: Default case - Any other parenthesis type does not allow a digit to follow
            else -> {
                // Invalid for other parenthesis types
                false
            }
        }
    }

    private fun validateDigit(prev: Operator, digit: Digit): Boolean {
        return when (prev) {
            // Rule 1: Division by zero is invalid
            is Operator.Divide -> {
                if (digit == Digit.Zero) {
                    // Invalid because division by zero is undefined
                    false
                } else {
                    // Valid for any other digit
                    true
                }
            }

            // Rule 2: Percent operator always results in invalid input when followed by a digit
            is Operator.Percent -> {
                // Invalid because percent does not accept digits directly
                false
            }

            // Rule 3: Default case - All other combinations are valid
            else -> {
                // Valid for any other operator and digit combination
                true
            }
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