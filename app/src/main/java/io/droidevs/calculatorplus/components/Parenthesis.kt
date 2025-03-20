package io.droidevs.calculatorplus.components

open class Parenthesis(text: String) : Special(text) {


    object OpenParenthesis : Parenthesis("(")

    object CloseParenthesis : Parenthesis(")")

}