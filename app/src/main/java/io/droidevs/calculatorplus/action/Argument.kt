package io.droidevs.calculatorplus.action

open class Argument {


    fun isNoArgument() : Boolean {
        return this is NoArgument
    }

    fun isSingleArgument() : Boolean {
        return this is SingleArgument
    }

    fun isDoubleArguments() : Boolean {
        return this is DoubleArgument
    }

    companion object {

        fun none() : Argument {
            return of()
        }
        fun of(vararg arguments : Double) : Argument {
            if (arguments.isEmpty())
                return NoArgument()
            if (arguments.size == 1)
                return SingleArgument(arguments[0])
            else if (arguments.size == 2)
                return DoubleArgument(arguments[0], arguments[1])
            else
                throw IllegalArgumentException("Invalid number of arguments")
        }
    }


    class NoArgument() : Argument()

    class SingleArgument(val argument : Double) : Argument()

    class DoubleArgument(val argument1 : Double, val argument2 : Double) : Argument()

}