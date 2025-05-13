package io.droidevs.calculatorplus.domain.components

import io.droidevs.calculatorplus.domain.token.FunctionToken

sealed class ClcFunction(text: String) : Component(text = text) {
    companion object {
        fun getAllFunctionOperators(): List<ClcFunction> {
            return listOf(
                Sin, Cos, Tan, Ln, Log, SquareRoot, Absolute
            )
        }

        fun isFunction(input: String): Boolean {
            return getAllFunctionOperators().any { it.text == input }
        }
    }

    // Function definitions
    object Sin : ClcFunction("sin")

    object Cos : ClcFunction("cos")

    object Tan : ClcFunction("tan")


    object Ln : ClcFunction("ln")

    object Log : ClcFunction("log")


    object SquareRoot : ClcFunction("√")

    object Absolute : ClcFunction("abs")

    // Additional missing operators
    object ASin : ClcFunction("asin")

    object ACos : ClcFunction("acos")

    object ATan : ClcFunction("atan")

    object SinH : ClcFunction("sinh")

    object CosH : ClcFunction("cosh")

    object TanH : ClcFunction("tanh")

    object ASinH : ClcFunction("asinh")

    object ACosH : ClcFunction("acosh")

    object ATanH : ClcFunction("atanh")

}


fun ClcFunction.toToken(): FunctionToken {
    return when {
        isSin() -> return FunctionToken.SinToken()
        isCos() -> return FunctionToken.CosToken()
        isTan() -> return FunctionToken.TanToken()
        isLn() -> return FunctionToken.LnToken()
        isLog() -> return FunctionToken.LogToken()
        isSquareRoot() -> return FunctionToken.SqrtToken()
        //ClcFunction.Absolute -> return FunctionToken.AbsoluteToken()
        isASin() -> return FunctionToken.ASinToken()
        isACos() -> return FunctionToken.ACosToken()
        isATan() -> return FunctionToken.ATanToken()
        isSinH() -> return FunctionToken.SinHToken()
        isCosH() -> return FunctionToken.CosHToken()
        isTanH() -> return FunctionToken.TanHToken()
        isASinH() -> return FunctionToken.ASinHToken()
        isACosH() -> return FunctionToken.ACosHToken()
        isATanH() -> return FunctionToken.ATanHToken()
        //isAbsolute() -> return FunctionToken.AbsoluteToken()
        else -> throw IllegalArgumentException("Invalid function token")
    }
}


fun ClcFunction.isSin() = this is ClcFunction.Sin
fun ClcFunction.isCos() = this is ClcFunction.Cos
fun ClcFunction.isTan() = this is ClcFunction.Tan

fun ClcFunction.isLn() = this is ClcFunction.Ln
fun ClcFunction.isLog() = this is ClcFunction.Log

fun ClcFunction.isSquareRoot() = this is ClcFunction.SquareRoot
fun ClcFunction.isAbsolute() = this is ClcFunction.Absolute

fun ClcFunction.isASin() = this is ClcFunction.ASin
fun ClcFunction.isACos() = this is ClcFunction.ACos
fun ClcFunction.isATan() = this is ClcFunction.ATan

fun ClcFunction.isSinH() = this is ClcFunction.SinH
fun ClcFunction.isCosH() = this is ClcFunction.CosH
fun ClcFunction.isTanH() = this is ClcFunction.TanH

fun ClcFunction.isASinH() = this is ClcFunction.ASinH
fun ClcFunction.isACosH() = this is ClcFunction.ACosH
fun ClcFunction.isATanH() = this is ClcFunction.ATanH
