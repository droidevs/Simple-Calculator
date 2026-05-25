package io.droidevs.calculatorplus.domain.components

sealed class Component(val text: String) {

    companion object {
        fun identify(char: Char): Component {
            return when (char) {
                '0' -> Digit.Zero
                '1' -> Digit.One
                '2' -> Digit.Two
                '3' -> Digit.Three
                '4' -> Digit.Four
                '5' -> Digit.Five
                '6' -> Digit.Six
                '7' -> Digit.Seven
                '8' -> Digit.Eight
                '9' -> Digit.Nine
                '+' -> Operator.Plus
                '-' -> Operator.Minus
                // BUG FIX #8: Accept both ASCII ('*', '/') and display ('×', '÷') forms
                // so expressions stored as formatted strings can be re-tokenized correctly.
                '*', '×' -> Operator.Multiply
                '/', '÷' -> Operator.Divide
                '!' -> Operator.Factorial
                '^' -> Operator.Power
                '%' -> Operator.Percent
                '(' -> Parenthesis.OpenParenthesis
                ')' -> Parenthesis.CloseParenthesis
                '.' -> Special.Decimal
                'e' -> Constant.E
                'π' -> Constant.PI
                else -> Special.Unknown
            }
        }

        fun isPi(char: Char): Boolean = char == 'π'
        fun isNumber(char: Char): Boolean = char.isDigit()
        fun isZero(char: Char) = char == '0'
        fun isOne(char: Char) = char == '1'
        fun isTwo(char: Char) = char == '2'
        fun isThree(char: Char) = char == '3'
        fun isFour(char: Char) = char == '4'
        fun isFive(char: Char) = char == '5'
        fun isSix(char: Char) = char == '6'
        fun isSeven(char: Char) = char == '7'
        fun isEight(char: Char) = char == '8'
        fun isNine(char: Char) = char == '9'
        fun isOperator(char: Char) = char in listOf('+', '-', '*', '/', '^', '×', '÷')
        fun isPlus(char: Char) = char == '+'
        fun isMinus(char: Char) = char == '-'
        fun isMultiply(char: Char) = char == '*' || char == '×'
        fun isDivide(char: Char) = char == '/' || char == '÷'
        fun isFactorial(char: Char) = char == '!'
        fun isParenthesis(char: Char) = char == '(' || char == ')'
        fun isCloseParenthesis(char: Char) = char == ')'
        fun isOpenParenthesis(char: Char) = char == '('
        fun isDecimalPoint(char: Char) = char == '.'
    }
}