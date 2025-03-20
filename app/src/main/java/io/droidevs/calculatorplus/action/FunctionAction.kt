package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.components.ClcFunction
import io.droidevs.calculatorplus.components.Constant
import io.droidevs.calculatorplus.components.Digit
import io.droidevs.calculatorplus.components.Operator
import io.droidevs.calculatorplus.components.Parenthesis
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.acosh
import kotlin.math.asin
import kotlin.math.asinh
import kotlin.math.atan
import kotlin.math.atanh
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.tanh


abstract class FunctionAction(text: String, value: ActionValue) : ApplyAction(text, value) {


    object Sin : FunctionAction("sin", ActionValue.of(ClcFunction.Sin, Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return sin(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for sin function")
            }
        }
    }

    object Cos : FunctionAction("cos", ActionValue.of(ClcFunction.Cos,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return cos(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for cos function")
            }
        }
    }

    object Tan : FunctionAction("tan", ActionValue.of(ClcFunction.Tan,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return tan(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for tan function")
            }
        }
    }

    object Ln : FunctionAction("ln", ActionValue.of(ClcFunction.Ln,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return ln(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for ln function")
            }
        }
    }

    object Log : FunctionAction("log", ActionValue.of(ClcFunction.Log,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return log10(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for log function")
            }
        }
    }

    object Square : FunctionAction("x²", ActionValue.of(Operator.Power, Digit.Two)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return argument.argument.pow(2.0)
                }
                else -> throw IllegalArgumentException("Invalid argument type for square function")
            }
        }
    }

    object SquareRoot : FunctionAction("√", ActionValue.of(ClcFunction.SquareRoot,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return sqrt(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for squareRoot function")
            }
        }
    }

    object Absolute : FunctionAction("|x|", ActionValue.of(ClcFunction.Absolute,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return abs(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for absolute function")
            }
        }
    }

    object Power : FunctionAction("xʸ", ActionValue.of(Operator.Power,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.DoubleArgument -> {
                    return argument.argument1.pow(argument.argument2)
                }
                else -> throw IllegalArgumentException("Invalid argument type for power function")
            }
        }
    }

    // Additional missing operators
    object ASin : FunctionAction("sin⁻¹", ActionValue.of(ClcFunction.ASin,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return asin(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for asin function")
            }
        }
    }

    object ACos : FunctionAction("cos⁻¹", ActionValue.of(ClcFunction.ACos,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return acos(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for acos function")
            }
        }
    }

    object ATan : FunctionAction("tan⁻¹", ActionValue.of(ClcFunction.ATan,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return atan(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for atan function")
            }
        }
    }

    object SinH : FunctionAction("sinh", ActionValue.of(ClcFunction.SinH,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return sinh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for sinh function")
            }
        }
    }

    object CosH : FunctionAction("cosh", ActionValue.of(ClcFunction.CosH,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return cosh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for cosh function")
            }
        }
    }

    object TanH : FunctionAction("tanh", ActionValue.of(ClcFunction.TanH,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return tanh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for tanh function")
            }
        }
    }

    object ASinH : FunctionAction("sinh⁻¹", ActionValue.of(ClcFunction.ASinH,Parenthesis.OpenParenthesis)) {

        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return asinh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for asinh function")
            }
        }
    }

    object ACosH : FunctionAction("cosh⁻¹", ActionValue.of(ClcFunction.ACosH,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return acosh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for acosh function")
            }
        }
    }

    object ATanH : FunctionAction("tanh⁻¹", ActionValue.of(ClcFunction.ATanH,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return atanh(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for atanh function")
            }
        }
    }

    object Cube : FunctionAction("x³", ActionValue.of(Operator.Power,Digit.Three)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return argument.argument.pow(3.0)
                }
                else -> throw IllegalArgumentException("Invalid argument type for cube function")
            }
        }
    }

    object PowerE : FunctionAction("exp", ActionValue.of(Constant.E,Operator.Power,Parenthesis.OpenParenthesis)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> {
                    return exp(argument.argument)
                }
                else -> throw IllegalArgumentException("Invalid argument type for exp function")
            }
        }
    }

}