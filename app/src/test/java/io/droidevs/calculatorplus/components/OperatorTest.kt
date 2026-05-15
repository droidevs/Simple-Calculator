package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.isDivide
import io.droidevs.calculatorplus.domain.components.isFactorial
import io.droidevs.calculatorplus.domain.components.isMinus
import io.droidevs.calculatorplus.domain.components.isMultiply
import io.droidevs.calculatorplus.domain.components.isPercent
import io.droidevs.calculatorplus.domain.components.isPlus
import io.droidevs.calculatorplus.domain.components.isPower
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OperatorTest {

    //
    // Tests for companion object methods
    //
    @Test
    fun `getAll returns correct list of operators`() {
        val expectedOperators = listOf(
            Operator.Plus,
            Operator.Minus,
            Operator.Multiply,
            Operator.Divide,
            Operator.Percent
        )
        assertEquals(expectedOperators, Operator.getAll())
    }

    @Test
    fun `isOperator returns true for valid operator strings`() {
        assertTrue(Operator.isOperator("+"))
        assertTrue(Operator.isOperator("-"))
        assertTrue(Operator.isOperator("*"))
        assertTrue(Operator.isOperator("/"))
        assertTrue(Operator.isOperator("%"))
    }

    @Test
    fun `isOperator returns false for invalid strings`() {
        assertFalse(Operator.isOperator("!"))
        assertFalse(Operator.isOperator("^"))
        assertFalse(Operator.isOperator("x"))
        assertFalse(Operator.isOperator("123"))
    }

    //
    // Tests for toToken() extension function
    //
    @Test
    fun `toToken maps Plus to PlusToken`() {
        val token = Operator.Plus.toToken()
        assertTrue(token is OperatorToken.PlusToken)
    }

    @Test
    fun `toToken maps Minus to MinusToken`() {
        val token = Operator.Minus.toToken()
        assertTrue(token is OperatorToken.MinusToken)
    }

    @Test
    fun `toToken maps Multiply to MultiplyToken`() {
        val token = Operator.Multiply.toToken()
        assertTrue(token is OperatorToken.MultiplyToken)
    }

    @Test
    fun `toToken maps Divide to DivideToken`() {
        val token = Operator.Divide.toToken()
        assertTrue(token is OperatorToken.DivideToken)
    }

    @Test
    fun `toToken maps Percent to PercentToken`() {
        val token = Operator.Percent.toToken()
        assertTrue(token is OperatorToken.PercentToken)
    }

    @Test
    fun `toToken maps Factorial to FactorialToken`() {
        val token = Operator.Factorial.toToken()
        assertTrue(token is OperatorToken.FactorialToken)
    }

    @Test
    fun `toToken maps Power to PowerToken`() {
        val token = Operator.Power.toToken()
        assertTrue(token is OperatorToken.PowerToken)
    }

    //
    // Tests for is...() extension functions
    //
    @Test
    fun `isPlus returns true for Plus and false for others`() {
        assertTrue(Operator.Plus.isPlus())
        assertFalse(Operator.Minus.isPlus())
        assertFalse(Operator.Power.isPlus())
    }

    @Test
    fun `isMinus returns true for Minus and false for others`() {
        assertTrue(Operator.Minus.isMinus())
        assertFalse(Operator.Plus.isMinus())
    }

    @Test
    fun `isMultiply returns true for Multiply and false for others`() {
        assertTrue(Operator.Multiply.isMultiply())
        assertFalse(Operator.Divide.isMultiply())
    }

    @Test
    fun `isDivide returns true for Divide and false for others`() {
        assertTrue(Operator.Divide.isDivide())
        assertFalse(Operator.Percent.isDivide())
    }

    @Test
    fun `isPercent returns true for Percent and false for others`() {
        assertTrue(Operator.Percent.isPercent())
        assertFalse(Operator.Factorial.isPercent())
    }

    @Test
    fun `isFactorial returns true for Factorial and false for others`() {
        assertTrue(Operator.Factorial.isFactorial())
        assertFalse(Operator.Power.isFactorial())
    }

    @Test
    fun `isPower returns true for Power and false for others`() {
        assertTrue(Operator.Power.isPower())
        assertFalse(Operator.Factorial.isPower())
    }
}