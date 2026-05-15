package io.droidevs.calculatorplus

import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenizerFormatterServiceTest {

    private lateinit var service: TokenizerFormatterService

    @Before
    fun setUp() {
        service = TokenizerFormatterService()
    }

    /**
     * Helper function to convert a linked list of tokens back to a string
     * for easy comparison.
     */
    private fun linkedTokenToString(head: LinkedToken): String {
        if (head is SpecialToken.EmptyToken) return ""
        val sb = StringBuilder()
        var current: LinkedToken? = head
        while (current != null) {
            sb.append(current.component.text)
            current = current.next
        }
        return sb.toString()
    }

    /**
     * Helper to get the last token in a chain.
     */
    private fun getTail(head: LinkedToken): LinkedToken? {
        if (head is SpecialToken.EmptyToken) return null
        var current: LinkedToken? = head
        while (current?.next != null) {
            current = current.next
        }
        return current
    }
    //==========================================================================
    // format() and tokenize() Tests
    //==========================================================================

    @Test
    fun `format with empty string returns empty token`() {
        val head = service.format("")
        assertTrue(head is SpecialToken.EmptyToken)
    }

    @Test
    fun `format with simple expression creates correct token chain`() {
        val expression = "1+2"
        val head = service.format(expression)

        // Verify chain: 1 -> + -> 2
        assertEquals(Digit.One, head.component)

        val plus = head.next
        assertNotNull(plus)
        assertEquals(Operator.Plus, plus!!.component)

        val two = plus.next
        assertNotNull(two)
        assertEquals(Digit.Two, two!!.component)
        assertNull(two.next) // End of chain

        // Verify reverse links
        assertNotNull(two.prev)
        assertEquals(Operator.Plus, two.prev!!.component)

        assertNotNull(plus.prev)
        assertEquals(Digit.One, plus.prev!!.component)
        assertNull(head.prev)

        // Verify full string representation
        assertEquals(expression, linkedTokenToString(head))
    }

    @Test
    fun `format with number separators removes them before tokenizing`() {
        val expressionWithCommas = "1,000,000*2"
        val expectedTokenString = "1000000*2"
        val head = service.format(expressionWithCommas)

        assertEquals(expectedTokenString, linkedTokenToString(head))
    }

    @Test
    fun `format with complex expression creates correct token chain`() {
        // We use single letters for functions based on our test Component.identify()
        val expression = "s(1.5+c(90))"
        val head = service.format(expression)

        assertEquals(expression, linkedTokenToString(head))
        assertNotNull(head)
    }

    @Test
    fun `format with unknown characters skips them`() {
        val expression = "1a+b2_c"
        val expectedTokenString = "1+2"
        val head = service.format(expression)

        assertEquals(expectedTokenString, linkedTokenToString(head))
    }

    @Test
    fun `format with all component types works correctly`() {
        val expression = "1+2-3*4/5^6%sctl(.)"
        val head = service.format(expression)
        assertEquals(expression, linkedTokenToString(head))
    }

    //==========================================================================
    // cursorFormattedToRaw() Tests
    //==========================================================================

    @Test
    fun `cursorFormattedToRaw with no separators returns same index`() {
        val formatted = "12345"
        val raw = "12345"
        assertEquals(3, service.cursorFormattedToRaw(formatted, raw, 3))
    }

    @Test
    fun `cursorFormattedToRaw with cursor at start`() {
        val formatted = "1,234,567"
        val raw = "1234567"
        assertEquals(0, service.cursorFormattedToRaw(formatted, raw, 0))
    }

    @Test
    fun `cursorFormattedToRaw with cursor at end`() {
        val formatted = "1,234"
        val raw = "1234"
        assertEquals(4, service.cursorFormattedToRaw(formatted, raw, 5))
    }

    @Test
    fun `cursorFormattedToRaw with cursor before first separator`() {
        val formatted = "1,234,567"
        val raw = "1234567"
        assertEquals(1, service.cursorFormattedToRaw(formatted, raw, 1))
    }

    @Test
    fun `cursorFormattedToRaw with cursor right on separator`() {
        val formatted = "1,234,567"
        val raw = "1234567"
        // Cursor at index 2 is after the ','
        assertEquals(1, service.cursorFormattedToRaw(formatted, raw, 2))
    }

    @Test
    fun `cursorFormattedToRaw with cursor right after separator`() {
        val formatted = "1,234,567"
        val raw = "1234567"
        // Cursor at index 3 is on the '2'
        assertEquals(2, service.cursorFormattedToRaw(formatted, raw, 3))
    }

    @Test
    fun `cursorFormattedToRaw with cursor in the middle`() {
        val formatted = "1,234,567"
        val raw = "1234567"
        // Cursor at index 6 is on the '5'
        assertEquals(4, service.cursorFormattedToRaw(formatted, raw, 6))
    }

    @Test
    fun `cursorFormattedToRaw with empty strings`() {
        assertEquals(0, service.cursorFormattedToRaw("", "", 0))
    }


    // ⭐️ NEW TESTS START HERE ⭐️

    @Test
    fun `format with whitespace skips whitespace characters`() {
        val expression = " 5 * ( 10 / 2 ) "
        val expected = "5*(10/2)"
        val head = service.format(expression)
        assertEquals(expected, linkedTokenToString(head))
    }

    @Test
    fun `format with only separators returns empty token`() {
        val head = service.format(",,,")
        assertTrue(head is SpecialToken.EmptyToken)
    }

    @Test
    fun `format with leading and trailing separators removes them`() {
        val expression = ",1,234,"
        val expected = "1234"
        val head = service.format(expression)
        assertEquals(expected, linkedTokenToString(head))
    }

    @Test
    fun `tokenize long chain maintains correct prev and next links throughout`() {
        val expression = "1+2-3*4"
        val head = service.format(expression)

        // Forward traversal check
        assertEquals("1+2-3*4", linkedTokenToString(head))

        // Backward traversal check
        val tail = getTail(head)
        assertNotNull(tail)

        val sb = StringBuilder()
        var current: LinkedToken? = tail
        while (current != null) {
            sb.append(current.component.text)
            current = current.prev
        }
        assertEquals("4*3-2+1", sb.toString())
    }

    // ⭐️ NEW TESTS START HERE ⭐️

    @Test
    fun `cursorFormattedToRaw when cursor is out of bounds returns raw length`() {
        val formatted = "1,234"
        val raw = "1234"
        // Cursor at 10 is well beyond the length of "1,234" (5)
        assertEquals(4, service.cursorFormattedToRaw(formatted, raw, 10))
    }

    @Test
    fun `cursorFormattedToRaw with malformed double separators processes correctly`() {
        val formatted = "1,,234" // Invalid format, but should be handled gracefully
        val raw = "1234"
        // Cursor is on '2'
        assertEquals(2, service.cursorFormattedToRaw(formatted, raw, 3))
        // Cursor is at the end
        assertEquals(4, service.cursorFormattedToRaw(formatted, raw, 6))
    }

    @Test
    fun `cursorFormattedToRaw with leading separator`() {
        val formatted = ",123"
        val raw = "123"
        // Cursor is on '1'
        assertEquals(1, service.cursorFormattedToRaw(formatted, raw, 2))
    }
}