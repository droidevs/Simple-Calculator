package io.droidevs.calculatorplus.tokens

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.count
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.getTokenAt
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.replaceAt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LinkedTokenTest {

    private lateinit var digit3: LinkedToken
    private lateinit var multiply: LinkedToken
    private lateinit var sinFunction: LinkedToken
    private lateinit var openParen: LinkedToken
    private lateinit var digit3_2: LinkedToken
    private lateinit var digit2_2: LinkedToken
    private lateinit var closeParen: LinkedToken

    @Before
    fun setUp() {
        // Create the token instances
        digit3 = DigitToken(Digit.Three)
        multiply = OperatorToken(Operator.Multiply)
        sinFunction = FunctionToken(ClcFunction.Sin)
        openParen = ParenthesisToken.OpenParenthesisToken()
        digit3_2 = DigitToken(Digit.Three)
        digit2_2 = DigitToken(Digit.Two)
        closeParen = ParenthesisToken.CloseParenthesisToken()

        // Link them in order
        digit3.next = multiply
        multiply.prev = digit3

        multiply.next = sinFunction
        sinFunction.prev = multiply

        sinFunction.next = openParen
        openParen.prev = sinFunction

        openParen.next = digit3_2
        digit3_2.prev = openParen

        digit3_2.next = digit2_2
        digit2_2.prev = digit3_2

        digit2_2.next = closeParen
        closeParen.prev = digit2_2
    }

    @Test
    fun `the token chain is correctly constructed and linked`() {
        // Verify the next pointers
        assertEquals(multiply, digit3.next)
        assertEquals(sinFunction, multiply.next)
        assertEquals(openParen, sinFunction.next)
        assertEquals(digit3_2, openParen.next)
        assertEquals(digit2_2, digit3_2.next)
        assertEquals(closeParen, digit2_2.next)

        // Verify the prev pointers
        assertEquals(digit3, multiply.prev)
        assertEquals(multiply, sinFunction.prev)
        assertEquals(sinFunction, openParen.prev)
        assertEquals(openParen, digit3_2.prev)
        assertEquals(digit3_2, digit2_2.prev)
        assertEquals(digit2_2, closeParen.prev)
    }

    @Test
    fun `the start and end indices are correctly calculated`() {
        // The chain is 3*sin(32)
        // Lengths: 1, 1, 3, 1, 1, 1, 1
        // Indices: 0, 1, 2, 5, 6, 7, 8
        assertEquals(0, digit3.startIndex)
        assertEquals(0, digit3.endIndex)

        assertEquals(1, multiply.startIndex)
        assertEquals(1, multiply.endIndex)

        assertEquals(2, sinFunction.startIndex)
        assertEquals(4, sinFunction.endIndex)

        assertEquals(5, openParen.startIndex)
        assertEquals(5, openParen.endIndex)

        assertEquals(6, digit3_2.startIndex)
        assertEquals(6, digit3_2.endIndex)

        assertEquals(7, digit2_2.startIndex)
        assertEquals(7, digit2_2.endIndex)

        assertEquals(8, closeParen.startIndex)
        assertEquals(8, closeParen.endIndex)
    }

    @Test
    fun `count correctly finds and counts all tokens in the chain`() {
        // Count all non-empty tokens in the chain
        assertEquals(7, digit3.count { it.isNotEmpty() })
        // Count a specific token type
        assertEquals(1, digit3.count { it is OperatorToken })
        assertEquals(2, digit3.count { it is ParenthesisToken })
        assertEquals(1, digit3.count { it is FunctionToken })
    }

    @Test
    fun `find correctly locates tokens by type and value`() {
        // Find the first digit
        val firstDigit = digit3.find { it is DigitToken }
        assertEquals(digit3, firstDigit)

        // Find the sin function
        val sinToken = digit3.find { it is FunctionToken }
        assertEquals(sinFunction, sinToken)

        // Find a token that doesn't exist
        val nonExistentToken = digit3.find { it.component.text == "abs" }
        assertNull(nonExistentToken)
    }

    @Test
    fun `getTokenAt returns the correct token based on its startIndex`() {
        // Get token by index
        assertEquals(digit3, digit3.getTokenAt(0))
        assertEquals(multiply, digit3.getTokenAt(1))
        assertEquals(sinFunction, digit3.getTokenAt(2))
        assertEquals(openParen, digit3.getTokenAt(5))
        assertEquals(closeParen, digit3.getTokenAt(8))

        // Ensure null is returned for an index outside the chain
        assertNull(digit3.getTokenAt(9))
        assertNull(digit3.getTokenAt(-1))
    }

    @Test
    fun `insertAt correctly inserts a new token into the middle of the chain`() {
        val newOperator = OperatorToken(Operator.Minus) // A new token to insert

        // Insert it between 'sin' and '('
        sinFunction.insertAt(openParen.startIndex, newOperator)

        // Verify the new token is correctly linked
        assertEquals(newOperator, sinFunction.next)
        assertEquals(openParen, newOperator.next)
        assertEquals(sinFunction, newOperator.prev)
        assertEquals(newOperator, openParen.prev)

        // Verify that indices are correctly updated
        assertEquals(5, newOperator.startIndex) // New token starts at sinFunction.endIndex + 1
        assertEquals(6, openParen.startIndex)
        assertEquals(7, digit3_2.startIndex)
    }

    @Test
    fun `replaceAt correctly replaces a token and updates the chain`() {
        val newFunction = FunctionToken(ClcFunction.Cos)

        // Replace 'sin' with 'cos'
        digit3.replaceAt(sinFunction.startIndex, newFunction)

        // Verify the new token is in place
        assertEquals(multiply.next, newFunction)
        assertEquals(newFunction.next, openParen)
        assertEquals(2, newFunction.startIndex)

        // Ensure the original 'sin' token is detached and the indices are corrected
        assertNull(sinFunction.prev)
        assertNull(sinFunction.next)
        assertEquals(5, openParen.startIndex)
    }
}