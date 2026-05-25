package io.droidevs.calculatorplus.tokens

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class DigitTokenTest {

    private lateinit var digitToken: DigitToken

    @Before
    fun setUp() {
        digitToken = DigitToken.OneToken()
    }

    @Test
    fun `isValid returns true when preceded by a plus operator`() {
        assertTrue(digitToken.isValid(ValidationArgument(Operator.Plus, Digit.One)))
    }

    @Test
    fun `isValid returns true when preceded by a minus operator`() {
        assertTrue(digitToken.isValid(ValidationArgument(Operator.Minus, Digit.Five)))
    }

    @Test
    fun `isValid returns false when preceded by a percent operator`() {
        // Percent is a postfix operator; a digit after % is invalid
        assertFalse(digitToken.isValid(ValidationArgument(Operator.Percent, Digit.Nine)))
    }

    @Test
    fun `isValid returns true when preceded by an open parenthesis`() {
        assertTrue(digitToken.isValid(ValidationArgument(Parenthesis.OpenParenthesis, Digit.One)))
    }

    @Test
    fun `isValid returns false when preceded by a close parenthesis`() {
        // Close paren + digit requires implicit multiply — handled by DigitUseCase
        assertFalse(digitToken.isValid(ValidationArgument(Parenthesis.CloseParenthesis, Digit.Three)))
    }

    @Test
    fun `isValid returns true when first token in expression`() {
        assertTrue(digitToken.isValid(ValidationArgument(Special.Empty, Digit.Two)))
    }

    // BUG FIX #4: This test previously asserted FALSE — which was WRONG.
    // Digits CAN follow other digits (e.g., "12", "345"). The test was contradicting
    // the implementation. The implementation is correct; the test was the bug.
    @Test
    fun `isValid returns TRUE when the previous token is a digit — digits form multi-digit numbers`() {
        val prevToken = Digit.Three
        val argument = ValidationArgument(prevToken, Digit.Four)
        assertTrue("A digit must be valid after another digit to support multi-digit numbers",
            digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns true when preceded by a decimal`() {
        assertTrue(digitToken.isValid(ValidationArgument(Special.Decimal, Digit.Five)))
    }
}