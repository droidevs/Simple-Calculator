package io.droidevs.calculatorplus.components

import io.droidevs.calcus.domain.model.protocol.format.Format

open class Component(val text : String) {

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
                if (isSin(it))
                    return ClcFunction.Sin
                if (isCos(it))
                    return ClcFunction.Cos
                if (isTan(it))
                    return ClcFunction.Tan
                if (isACos(it))
                    return ClcFunction.ACos
                if (isASin(it))
                    return ClcFunction.ASin
                if (isATan(it))
                    return ClcFunction.ATan
                if (isCosH(it))
                    return ClcFunction.CosH
                if (isSinH(it))
                    return ClcFunction.SinH
                if (isTanH(it))
                    return ClcFunction.TanH
                if (isACosH(it))
                    return ClcFunction.ACosH
                if (isASinH(it))
                    return ClcFunction.ASinH
                if (isATanH(it))
                    return ClcFunction.ATanH
                if (isLog(it))
                    return ClcFunction.Log
                if (isExp(it))
                    return Constant.E
                if (isPi(it))
                    return Constant.PI


                return Special.Unknown
            }
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

        /**
         * Checks if the given character is a valid character for scientific functions (e.g., sin, cos, log).
         *
         * @param char The character to check.
         * @return True if the character is valid for scientific functions, false otherwise.
         */
        fun isFunction(char: Char): Boolean {
            /*
        char.let {
            return isSin(it)
                    && isCos(it)
                    && isSin(it)
                    && isTan(it)
                    && isACos(it)
                    && isASin(it)
                    && isATan(it)
                    && isACosH(it)
                    && isASinH(it)
                    && isATanH(it)
        }*/
            return char.isFunction()
        }

        fun isSin(char: Char): Boolean {
            return char.isSin()
        }

        fun isCos(char: Char): Boolean {
            return char.isCos()
        }

        fun isTan(char: Char): Boolean {
            return char.isATan()
        }

        fun isACos(char: Char): Boolean {
            return char.isACos()
        }

        fun isASin(char: Char): Boolean {
            return char.isASin()
        }

        fun isATan(char: Char): Boolean {
            return char.isATan()
        }

        fun isCosH(char: Char): Boolean {
            return char.isCosH()
        }

        fun isSinH(char: Char): Boolean {
            return char.isSinH()
        }

        fun isTanH(char: Char): Boolean {
            return char.isTanH()
        }

        fun isACosH(char: Char): Boolean {
            return char.isACosH()
        }

        fun isASinH(char: Char): Boolean {
            return char.isASinH()
        }

        fun isATanH(char: Char): Boolean {
            return char.isATanH()
        }

        fun isExp(char: Char): Boolean {
            return char.isExp()
        }

        fun isLog(char: Char): Boolean {
            return char.isLog()
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
    return this == '('
}

/**
 * Extension function to check if a character is a valid decimal point.
 */
private fun Char.isDecimalPoint(): Boolean {
    return this == '.'
}

/**
 * Extension function to check if a character is part of a scientific function.
 * Scientific functions can include 's', 'i', 'n', 'c', 'o', 'l', 'g', 't', etc.,
 * which are parts of functions like sin, cos, log, tan.
 */
private fun Char.isFunction(): Boolean {
    return isSin() ||
            isCos() ||
            isSin() ||
            isTan() ||
            isACos() ||
            isASin() ||
            isATan() ||
            isACosH() ||
            isASinH() ||
            isATanH() ||
            isLog() ||
            isExp()
}



private fun Char.isSin() : Boolean {
    return this.toString() == Format.SIN
}

private fun Char.isCos() : Boolean {
    return this.toString() == Format.COS
}

private fun Char.isTan() : Boolean {
    return this.toString() == Format.TAN
}

private fun Char.isLog() : Boolean {
    return this.toString() == Format.LOG
}

private fun Char.isACos() : Boolean {
    return this.toString() == Format.ACOS
}

private fun Char.isASin() : Boolean {
    return this.toString() == Format.ASIN
}

private fun Char.isATan() : Boolean {
    return this.toString() == Format.ATAN
}

private fun Char.isCosH() : Boolean {
    return this.toString() == Format.COSH
}

private fun Char.isSinH() : Boolean {
    return this.toString() == Format.SINH
}

private fun Char.isTanH() : Boolean {
    return this.toString() == Format.TANH
}
private fun Char.isACosH() : Boolean {
    return this.toString() == Format.ACOSH
}

private fun Char.isASinH() : Boolean {
    return this.toString() == Format.ASINH
}

private fun Char.isATanH() : Boolean {
    return this.toString() == Format.ATANH
}

private fun Char.isExp() : Boolean {
    return this == 'e'
}

private fun Char.isPi() : Boolean {
    return this == 'π'
}