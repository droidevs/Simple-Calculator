package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.isCloseParenthesis
import io.droidevs.calculatorplus.domain.components.isOpenParenthesis
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParenthesisTest {

    @Test
    fun `isOpenParenthesis returns true for OpenParenthesis`() {
        // Test case for the open parenthesis
        assertTrue(Parenthesis.OpenParenthesis.isOpenParenthesis())
    }

    @Test
    fun `isOpenParenthesis returns false for CloseParenthesis`() {
        // Ensure the open parenthesis check fails for the close parenthesis
        assertFalse(Parenthesis.CloseParenthesis.isOpenParenthesis())
    }

    @Test
    fun `isCloseParenthesis returns true for CloseParenthesis`() {
        // Test case for the close parenthesis
        assertTrue(Parenthesis.CloseParenthesis.isCloseParenthesis())
    }

    @Test
    fun `isCloseParenthesis returns false for OpenParenthesis`() {
        // Ensure the close parenthesis check fails for the open parenthesis
        assertFalse(Parenthesis.OpenParenthesis.isCloseParenthesis())
    }
}