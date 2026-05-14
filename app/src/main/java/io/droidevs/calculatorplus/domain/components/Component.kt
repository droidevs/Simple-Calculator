package io.droidevs.calculatorplus.domain.components


sealed class Component(val text : String) {

    companion object {
        fun identify(char: Char): Component {
            char.let {
                if (isZero(it))
                    return Digit.Zero
                if (isOne(it))
                    return Digit.One
                if (isTwo(it))
                    return Digit.Two
                if (isThree(it))
                    return Digit.Three
                if (isFour(it))
                    return Digit.Four
                if (isFive(it))
                    return Digit.Five
                if (isSix(it))
                    return Digit.Six
                if (isSeven(it))
                    return Digit.Seven
                if (isEight(it))
                    return Digit.Eight
                if (isNine(it))
                    return Digit.Nine
                if (isPlus(it))
                    return Operator.Plus
                if (isMinus(it))
                    return Operator.Minus
                if (isMultiply(it))
                    return Operator.Multiply
                if (isDivide(it))
                    return Operator.Divide
                if (isFactorial(it))
                    return Operator.Factorial
                if (isParenthesis(it))
                    return if (isOpenParenthesis(it)) Parenthesis.OpenParenthesis else Parenthesis.CloseParenthesis
                if (isDecimalPoint(it))
                    return Special.Decimal
                if (isExp(it))
                    return Constant.E
                if (isPi(it))
                    return Constant.PI


                return Special.Unknown
            }
        }

        private fun isExp(it: Char): Boolean {
             return it.isExp()
        }

        public fun isPi(char: Char): Boolean {
            return char.isPi()
        }

        /**
         * Checks if the given character is a digit (0-9).
         *
         * @param char The character to check.
         * @return True if the character is a digit, false otherwise.
         */
        fun isNumber(char: Char): Boolean {
            return char.isDigit()
        }

        fun isZero(char: Char): Boolean {
            return char.isZero()
        }

        fun isOne(char: Char): Boolean {
            return char.isOne()
        }

        fun isTwo(char: Char): Boolean {
            return char.isTwo()
        }

        fun isThree(char: Char): Boolean {
            return char.isThree()
        }

        fun isFour(char: Char): Boolean {
            return char.isFour()
        }

        fun isFive(char: Char)  :Boolean {
            return char.isFive()
        }

        fun isSix(char: Char) : Boolean {
            return char.isSix()
        }

        fun isSeven(char: Char) : Boolean {
            return char.isSeven()
        }

        fun isEight(char: Char) : Boolean {
            return char.isEight()
        }

        fun isNine(char: Char) : Boolean {
            return char.isNine()
        }

        /**
         * Checks if the given character is a mathematical operator (+, -, *, /, ^).
         *
         * @param char The character to check.
         * @return True if the character is a mathematical operator, false otherwise.
         */
        fun isOperator(char: Char): Boolean {
            return char.isOperator()
        }

        fun isPlus(char: Char): Boolean {
            return char.isPlus()
        }

        fun isMinus(char: Char): Boolean {
            return char.isMinus()
        }

        fun isMultiply(char: Char): Boolean {
            return char.isMultiply()
        }

        fun isDivide(char: Char): Boolean {
            return char.isDivide()
        }

        fun isFactorial(char: Char): Boolean {
            return char.isFactorial()
        }


        /*
     * Checks if the given character is a valid opening or closing parenthesis.
     *
     * @param char The character to check.
     * @return True if the character is a parenthesis, false otherwise.
     */
        fun isParenthesis(char: Char): Boolean {
            return char.isParenthesis()
        }

        fun isCloseParenthesis(char: Char): Boolean {
            return char.isParenthesisClose()
        }

        fun isOpenParenthesis(char: Char): Boolean {
            return char.isParenthesisOpen()
        }

        /**
         * Checks if the given character is a valid decimal point.
         *
         * @param char The character to check.
         * @return True if the character is a decimal point, false otherwise.
         */
        fun isDecimalPoint(char: Char): Boolean {
            return char.isDecimalPoint()
        }
    }
}


/**
 * Extension function to check if a character is a digit.
 */
private fun Char.isDigit(): Boolean {
    return isZero() ||
            isOne() ||
            isTwo() ||
            isThree() ||
            isFour() ||
            isFive() ||
            isSix() ||
            isSeven() ||
            isEight() ||
            isNine()
}

private fun Char.isZero() : Boolean {
    return this == '0'
}

private fun Char.isOne() : Boolean {
    return this == '1'
}

private fun Char.isTwo() : Boolean {
    return this == '2'
}

private fun Char.isThree() : Boolean {
    return this == '3'
}

private fun Char.isFour() : Boolean {
    return this == '4'
}

private fun Char.isFive() : Boolean {
    return this == '5'
}

private fun Char.isSix() :Boolean {
    return this == '6'
}

private fun Char.isSeven() : Boolean {
    return this == '7'
}

private fun Char.isEight() : Boolean {
    return this == '8'
}

private fun Char.isNine() : Boolean {
    return this == '9'
}

/**
 * Extension function to check if a character is a mathematical operator (+, -, *, /, ^).
 */
private fun Char.isOperator(): Boolean {
    return this in listOf('+', '-', '*', '/', '^')
}

private fun Char.isPlus() : Boolean {
    return this == '+'
}

private fun Char.isMinus() : Boolean {
    return this == '-'
}

private fun Char.isMultiply() : Boolean {
    return this == '*'
}

private fun Char.isDivide() : Boolean {
    return this == '/'
}

private fun Char.isFactorial() : Boolean {
    return this == '!'
}

/**
 * Extension function to check if a character is a valid parenthesis.
 */
private fun Char.isParenthesis(): Boolean {
    return isParenthesisOpen() || isParenthesisClose()
}

private fun Char.isParenthesisOpen(): Boolean {
    return this == '('
}

private fun Char.isParenthesisClose(): Boolean {
    return this == ')'
}

/**
 * Extension function to check if a character is a valid decimal point.
 */
private fun Char.isDecimalPoint(): Boolean {
    return this == '.'
}




private fun Char.isLetter() : Boolean {
    return this in listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
}

private fun Char.isExp() : Boolean {
    return this == 'e'
}

private fun Char.isPi() : Boolean {
    return this == 'π'
}