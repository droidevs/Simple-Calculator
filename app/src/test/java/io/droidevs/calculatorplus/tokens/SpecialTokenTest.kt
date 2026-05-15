package io.droidevs.calculatorplus.tokens

import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SpecialTokenTest {

    @Test
    fun `decimal cannot be first`() {
        val token = SpecialToken.DecimalToken.get()
        val arg = ValidationArgument(current = Special.Decimal)
        assertFalse(token.isValid(arg))
    }


    @Test
    fun `empty can be first`() {
        val token = SpecialToken.EmptyToken.get()
        val arg = ValidationArgument(current = Special.Empty)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `decimal valid between digits`() {
        val token = SpecialToken.DecimalToken.get()
        val prev = Digit.One
        val next = Digit.Two
        val arg = ValidationArgument(prev, Special.Decimal, next)
        assertTrue(token.isValid(arg))
    }

    @Test
    fun `decimal invalid if not followed by digit`() {
        val token = SpecialToken.DecimalToken.get()
        val prev = Digit.One
        val next = Operator.Plus // not a digit
        val arg = ValidationArgument(prev, Special.Decimal, next)
        assertFalse(token.isValid(arg))
    }


    @Test
    fun `empty always valid after empty`() {
        val token = SpecialToken.EmptyToken.get()
        val prev = Special.Empty
        val next = Special.Empty
        val arg = ValidationArgument(prev, Special.Empty, next)
        assertTrue(token.isValid(arg))
    }
}
