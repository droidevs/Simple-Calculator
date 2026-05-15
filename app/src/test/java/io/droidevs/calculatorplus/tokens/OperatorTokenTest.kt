package io.droidevs.calculatorplus.tokens


import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import org.junit.Assert.*
import org.junit.Test

class OperatorTokenTest {

    // ----- VALIDATION TESTS -----

    @Test
    fun testMinusAllowedAtStart() {
        val minus = OperatorToken.MinusToken()
        assertTrue(minus.isValid(ValidationArgument( current = Operator.Minus)))
    }

    @Test
    fun testOtherOperatorsNotAllowedAtStart() {
        val plus = OperatorToken.PlusToken()
        val multiply = OperatorToken.MultiplyToken()
        val divide = OperatorToken.DivideToken()
        assertFalse(plus.isValid(ValidationArgument(current = Operator.Plus)))
        assertFalse(multiply.isValid(ValidationArgument(current =  Operator.Multiply)))
        assertFalse(divide.isValid(ValidationArgument(current =  Operator.Divide)))
    }

    @Test
    fun testOperatorAfterDigitIsValid() {
        val plus = OperatorToken.PlusToken()
        val prev = Digit.Seven
        assertTrue(plus.isValid(ValidationArgument(prev, Operator.Plus)))
    }

    @Test
    fun testOperatorAfterDecimalIsInvalid() {
        val multiply = OperatorToken.MultiplyToken()
        val prev = Special.Decimal
        assertFalse(multiply.isValid(ValidationArgument(prev, Operator.Multiply)))
    }

    @Test
    fun testOperatorAfterFunctionIsInvalid() {
        val divide = OperatorToken.DivideToken()
        val prev = ClcFunction.Sin
        assertFalse(divide.isValid(ValidationArgument(prev, Operator.Divide)))
    }

    @Test
    fun testOperatorAfterCloseParenthesisIsValid() {
        val power = OperatorToken.PowerToken()
        val prev = Parenthesis.CloseParenthesis
        assertTrue(power.isValid(ValidationArgument(prev, Operator.Power)))
    }

    @Test
    fun testMinusAfterOpenParenthesisIsValid() {
        val minus = OperatorToken.MinusToken()
        val prev = Parenthesis.OpenParenthesis
        assertTrue(minus.isValid(ValidationArgument(prev, Operator.Minus)))
    }

    @Test
    fun testOtherOperatorsAfterOpenParenthesisInvalid() {
        val plus = OperatorToken.PlusToken()
        val prev = Parenthesis.OpenParenthesis
        assertFalse(plus.isValid(ValidationArgument(prev, Operator.Plus)))
    }

    @Test
    fun testConsecutivePercentOperatorsInvalid() {
        val percent = OperatorToken.PercentToken()
        val prev = Operator.Percent
        assertFalse(percent.isValid(ValidationArgument(prev, Operator.Percent)))
    }

    @Test
    fun testOperatorAfterPercentIsValid() {
        val plus = OperatorToken.PlusToken()
        val prev = Operator.Percent
        assertTrue(plus.isValid(ValidationArgument(prev, Operator.Plus)))
    }

    @Test
    fun testOnlyPlusOrMinusAllowedAfterPower() {
        val prev = Operator.Power
        assertTrue(OperatorToken.PlusToken().isValid(ValidationArgument(prev, Operator.Plus)))
        assertTrue(OperatorToken.MinusToken().isValid(ValidationArgument(prev, Operator.Minus)))
        assertFalse(OperatorToken.MultiplyToken().isValid(ValidationArgument(prev, Operator.Multiply)))
    }

    // ----- OPERATOR TYPE CHECK TESTS -----

    @Test
    fun testIsPlus() {
        assertTrue(OperatorToken.PlusToken().isPlus())
        assertFalse(OperatorToken.MinusToken().isPlus())
    }

    @Test
    fun testIsMinus() {
        assertTrue(OperatorToken.MinusToken().isMinus())
        assertFalse(OperatorToken.PlusToken().isMinus())
    }

    @Test
    fun testIsMultiply() {
        assertTrue(OperatorToken.MultiplyToken().isMultiply())
        assertFalse(OperatorToken.DivideToken().isMultiply())
    }

    @Test
    fun testIsDivide() {
        assertTrue(OperatorToken.DivideToken().isDivide())
        assertFalse(OperatorToken.MultiplyToken().isDivide())
    }

    @Test
    fun testIsPower() {
        assertTrue(OperatorToken.PowerToken().isPower())
        assertFalse(OperatorToken.PlusToken().isPower())
    }

    @Test
    fun testIsPercent() {
        assertTrue(OperatorToken.PercentToken().isPercent())
        assertFalse(OperatorToken.MinusToken().isPercent())
    }
}
