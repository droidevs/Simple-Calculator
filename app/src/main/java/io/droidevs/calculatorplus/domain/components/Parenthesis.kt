package io.droidevs.calculatorplus.domain.components

sealed class Parenthesis(text: String) : Special(text) {


    object OpenParenthesis : Parenthesis("(")

    object CloseParenthesis : Parenthesis(")")

}

fun Parenthesis.isOpenParenthesis(): Boolean = this is Parenthesis.OpenParenthesis
fun Parenthesis.isCloseParenthesis(): Boolean = this is Parenthesis.CloseParenthesis
