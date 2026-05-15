package io.droidevs.calculatorplus.tokens

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.ConstantToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConstantTokenTest {

    private val constant = Constant.PI // example constant (assume you have PI, E, etc.)
    private val token = ConstantToken(constant)

    @Test
    fun `constant valid when first`() {
        val arg = ValidationArgument(current =  constant)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `constant valid after operator plus`() {
        val prev = Operator.Plus
        val arg = ValidationArgument(prev = prev, current = constant)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `constant valid after operator multiply`() {
        val prev = Operator.Multiply
        val arg = ValidationArgument(prev = prev, current = constant)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `constant invalid after operator percent`() {
        val prev = Operator.Percent
        val arg = ValidationArgument(prev = prev, current = constant)
        assertFalse(token.isValid(arg))
    }

    @Test
    fun `constant valid after open parenthesis`() {
        val prev = Parenthesis.OpenParenthesis
        val arg = ValidationArgument(prev = prev, current = constant)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `constant invalid after close parenthesis`() {
        val prev = Parenthesis.CloseParenthesis
        val arg = ValidationArgument(prev = prev, current = constant)
        assertFalse(token.isValid(arg))
    }

    @Test
    fun `constant invalid after digit`() {
        val prev = Digit.Five
        val arg = ValidationArgument(prev = prev, current = constant)
        assertFalse(token.isValid(arg))
    }

    @Test
    fun `constant invalid after function`() {
        val prev = ClcFunction.Sin
        val arg = ValidationArgument(prev = prev, current = constant)
        assertFalse(token.isValid(arg))
    }

    @Test
    fun `constant invalid after special decimal`() {
        val prev = Special.Decimal
        val arg = ValidationArgument(prev = prev, current = constant)
        assertFalse(token.isValid(arg))
    }
}
