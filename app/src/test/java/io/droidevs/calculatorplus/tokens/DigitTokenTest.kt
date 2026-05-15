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

    // Test a specific digit token
    private lateinit var digitToken: DigitToken

    // Mocks for previous tokens
    private lateinit var mockOperator: Operator
    private lateinit var mockParenthesis: Parenthesis
    private lateinit var mockOtherComponent: Component

    @Before
    fun setUp() {
        digitToken = DigitToken.OneToken()
        mockOperator = mock(Operator::class.java)
        mockParenthesis = mock(Parenthesis::class.java)
        mockOtherComponent = mock(Component::class.java)
    }

    //
    // Tests for validation with an Operator as the previous token
    //
    @Test
    fun `isValid returns true when preceded by a plus operator`() {
        val prevToken = Operator.Plus
        val argument = ValidationArgument(prevToken, Digit.One)
        assertTrue(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns true when preceded by a minus operator`() {
        val prevToken = Operator.Minus
        val argument = ValidationArgument(prevToken, Digit.Five)
        assertTrue(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns false when preceded by a percent operator`() {
        val prevToken = Operator.Percent
        val argument = ValidationArgument(prevToken, Digit.Nine)
        assertFalse(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns false when preceded by a divide operator and the current digit is zero`() {
        val prevToken = Operator.Divide
        val argument = ValidationArgument(prevToken, Digit.Zero, Special.Empty)
        assertFalse(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns true when preceded by a divide operator and the current digit is not zero`() {
        val prevToken = Operator.Divide
        val argument = ValidationArgument(prevToken, Digit.Seven, Special.Empty)
        assertTrue(digitToken.isValid(argument))
    }

    //
    // Tests for validation with a Parenthesis as the previous token
    //
    @Test
    fun `isValid returns true when preceded by an open parenthesis`() {
        val prevToken = Parenthesis.OpenParenthesis
        val argument = ValidationArgument(prevToken, Digit.One)
        assertTrue(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns false when preceded by a close parenthesis`() {
        val prevToken = Parenthesis.CloseParenthesis
        val argument = ValidationArgument(prevToken, Digit.Three)
        assertFalse(digitToken.isValid(argument))
    }

    //
    // General and Edge Case Tests
    //
    @Test
    fun `isValid returns false when the previous token is null`() {
        val argument = ValidationArgument(Special.Empty, Digit.Two)
        assertTrue(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns false when the previous token is a different digit`() {
        val prevToken = Digit.Three // Assume Digit is a sealed class
        val argument = ValidationArgument(prevToken, Digit.Four)
        assertFalse(digitToken.isValid(argument))
    }

    @Test
    fun `isValid returns false for an unhandled component type`() {
        // A mock component that is neither an Operator nor a Parenthesis
        val argument = ValidationArgument(mockOtherComponent, Digit.Five)
        assertFalse(digitToken.isValid(argument))
    }
}