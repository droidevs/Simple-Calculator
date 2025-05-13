package io.droidevs.calculatorplus.domain.components

import io.droidevs.calculatorplus.domain.token.DigitToken

open class Digit(text: String) : Component(text) {

    object Zero : Digit("0")

    object One : Digit("1")

    object Two : Digit("2")

    object Three : Digit("3")

    object Four : Digit("4")

    object Five : Digit("5")

    object Six : Digit("6")

    object Seven : Digit("7")

    object Eight : Digit("8")

    object Nine : Digit("9")

}

fun Digit.toToken(): DigitToken {
    return when(this){
        is Digit.Zero -> DigitToken.ZeroToken()
        is Digit.One -> DigitToken.OneToken()
        is Digit.Two -> DigitToken.TwoToken()
        is Digit.Three -> DigitToken.ThreeToken()
        is Digit.Four -> DigitToken.FourToken()
        is Digit.Five -> DigitToken.FiveToken()
        is Digit.Six -> DigitToken.SixToken()
        is Digit.Seven -> DigitToken.SevenToken()
        is Digit.Eight -> DigitToken.EightToken()
        is Digit.Nine -> DigitToken.NineToken()
        else -> { throw IllegalArgumentException("Invalid digit: $this") }
    }
}

fun Digit.isZero() = this is Digit.Zero
fun Digit.isOne() = this is Digit.One
fun Digit.isTwo() = this is Digit.Two
fun Digit.isThree() = this is Digit.Three
fun Digit.isFour() = this is Digit.Four
fun Digit.isFive() = this is Digit.Five
fun Digit.isSix() = this is Digit.Six
fun Digit.isSeven() = this is Digit.Seven
fun Digit.isEight() = this is Digit.Eight
fun Digit.isNine() = this is Digit.Nine

