package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.components.Operator

abstract class OperatorAction(text: String, value: ActionValue) : ApplyAction(text, value) {

    object Plus : OperatorAction("+", ActionValue.of(Operator.Plus)){
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.DoubleArgument -> return argument.argument1.plus(argument.argument2)
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }
    object Minus : OperatorAction("-", ActionValue.of(Operator.Minus)){
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.DoubleArgument -> return argument.argument1.minus(argument.argument2)
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }
    object Multiply : OperatorAction("×", ActionValue.of(Operator.Multiply)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.DoubleArgument -> return argument.argument1.times(argument.argument2)
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }

    object Divide : OperatorAction("÷", ActionValue.of(Operator.Divide)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.DoubleArgument -> return argument.argument1.div(argument.argument2)
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }

    object Percent : OperatorAction("%", ActionValue.of(Operator.Percent)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> return argument.argument.div(100.0)
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }

    object Factorial : OperatorAction("x!", ActionValue.of(Operator.Factorial)) {
        override fun apply(argument: Argument): Double {
            when(argument){
                is Argument.SingleArgument -> return TODO()
                else -> {
                    throw IllegalArgumentException("Argument type not supported for $this operator")
                }
            }
        }
    }

}