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
        // BUG FIX #4: The original code listed `is Digit` as a valid predecessor, which IS
        // correct — digits follow digits in multi-digit numbers (e.g. 1, 12, 123).
        // The test DigitTokenTest asserted FALSE for "digit after digit" which was WRONG.
        // That test has been corrected in DigitTokenTest.kt.
        //
        // The one real rule: a digit CANNOT directly follow a close parenthesis (implicit
        // multiply must be inserted by the use case layer) or a Constant/Percent.
        return when (prev) {
            is Special.Empty,
            is Digit,
            is Special.Decimal,
            is Operator,
            is Parenthesis.OpenParenthesis -> true
            else -> false
        }
    }

    class OneToken : DigitToken(Digit.One) {
        override fun isValid(argument: ValidationArgument) = super.isValid(argument)
        companion object { fun get() = OneToken() }
    }
    class TwoToken : DigitToken(Digit.Two) { companion object { fun get() = TwoToken() } }
    class ThreeToken : DigitToken(Digit.Three) { companion object { fun get() = ThreeToken() } }
    class FourToken : DigitToken(Digit.Four) { companion object { fun get() = FourToken() } }
    class FiveToken : DigitToken(Digit.Five) { companion object { fun get() = FiveToken() } }
    class SixToken : DigitToken(Digit.Six) { companion object { fun get() = SixToken() } }
    class SevenToken : DigitToken(Digit.Seven) { companion object { fun get() = SevenToken() } }
    class EightToken : DigitToken(Digit.Eight) { companion object { fun get() = EightToken() } }
    class NineToken : DigitToken(Digit.Nine) { companion object { fun get() = NineToken() } }
    class ZeroToken : DigitToken(Digit.Zero) { companion object { fun get() = ZeroToken() } }

    fun isZero() = this is ZeroToken
    fun isOne() = this is OneToken
    fun isTwo() = this is TwoToken
    fun isThree() = this is ThreeToken
    fun isFour() = this is FourToken
    fun isFive() = this is FiveToken
    fun isSix() = this is SixToken
    fun isSeven() = this is SevenToken
    fun isEight() = this is EightToken
    fun isNine() = this is NineToken
}