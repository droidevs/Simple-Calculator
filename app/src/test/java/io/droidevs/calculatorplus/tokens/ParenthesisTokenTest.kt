package io.droidevs.calculatorplus.tokens

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument

import org.junit.Assert.*
import org.junit.Test

class ParenthesisTokenTest {

    // ----- VALIDATION TESTS -----

    @Test
    fun testOpenParenthesisAllowedAtStart() {
        val open = ParenthesisToken.OpenParenthesisToken()
        assertTrue(open.isValid(ValidationArgument(current = Parenthesis.OpenParenthesis)))
    }

    @Test
    fun testCloseParenthesisNotAllowedAtStart() {
        val close = ParenthesisToken.CloseParenthesisToken()
        assertFalse(close.isValid(ValidationArgument(current =  Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testCloseParenthesisValidAfterDigit() {
        val close = ParenthesisToken.CloseParenthesisToken()
        val prev = Digit.Three
        assertTrue(close.isValid(ValidationArgument(prev, Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testOpenParenthesisInvalidAfterDigit() {
        val open = ParenthesisToken.OpenParenthesisToken()
        val prev = Digit.Three
        assertFalse(open.isValid(ValidationArgument(prev, Parenthesis.OpenParenthesis)))
    }

    @Test
    fun testOpenParenthesisValidAfterFunction() {
        val open = ParenthesisToken.OpenParenthesisToken()
        val prev = ClcFunction.Sin
        assertTrue(open.isValid(ValidationArgument(prev, Parenthesis.OpenParenthesis)))
    }

    @Test
    fun testCloseParenthesisInvalidAfterFunction() {
        val close = ParenthesisToken.CloseParenthesisToken()
        val prev = ClcFunction.Cos
        assertFalse(close.isValid(ValidationArgument(prev, Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testOpenParenthesisValidAfterOperator() {
        val open = ParenthesisToken.OpenParenthesisToken()
        val prev = Operator.Plus
        assertTrue(open.isValid(ValidationArgument(prev, Parenthesis.OpenParenthesis)))
    }

    @Test
    fun testCloseParenthesisInvalidAfterOperator() {
        val close = ParenthesisToken.CloseParenthesisToken()
        val prev = Operator.Multiply
        assertFalse(close.isValid(ValidationArgument(prev, Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testOpenParenthesisAfterOpenParenthesisIsValid() {
        val open = ParenthesisToken.OpenParenthesisToken()
        val prev = Parenthesis.OpenParenthesis
        assertTrue(open.isValid(ValidationArgument(prev, Parenthesis.OpenParenthesis)))
    }

    @Test
    fun testCloseParenthesisAfterOpenParenthesisIsInvalid() {
        val close = ParenthesisToken.CloseParenthesisToken()
        val prev = Parenthesis.OpenParenthesis
        assertFalse(close.isValid(ValidationArgument(prev, Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testCloseParenthesisAfterCloseParenthesisIsValid() {
        val close = ParenthesisToken.CloseParenthesisToken()
        val prev = Parenthesis.CloseParenthesis
        assertTrue(close.isValid(ValidationArgument(prev, Parenthesis.CloseParenthesis)))
    }

    @Test
    fun testOpenParenthesisAfterCloseParenthesisIsInvalid() {
        val open = ParenthesisToken.OpenParenthesisToken()
        val prev = Parenthesis.CloseParenthesis
        assertFalse(open.isValid(ValidationArgument(prev, Parenthesis.OpenParenthesis)))
    }

    // ----- TYPE CHECKS -----

    @Test
    fun testIsOpenParenthesis() {
        assertTrue(ParenthesisToken.OpenParenthesisToken().isOpenParenthesis())
        assertFalse(ParenthesisToken.CloseParenthesisToken().isOpenParenthesis())
    }

    @Test
    fun testIsCloseParenthesis() {
        assertTrue(ParenthesisToken.CloseParenthesisToken().isCloseParenthesis())
        assertFalse(ParenthesisToken.OpenParenthesisToken().isCloseParenthesis())
    }
}
