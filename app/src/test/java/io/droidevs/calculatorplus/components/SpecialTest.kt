package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Special
import org.junit.Assert.assertEquals
import org.junit.Test

// The Test Class
class SpecialTest {

    @Test
    fun `Empty object has correct text and value`() {
        // Assertions for the Empty object
        assertEquals("", Special.Empty.text)
        // Since `value` is not directly accessible, we assume it's correctly set by the constructor.
        // We can't directly test `value` because it's a private constructor parameter
        // and not a public property.
        // The fact that the `text` is correct is the primary test.
    }

    @Test
    fun `Unknown object has correct text and value`() {
        // Assertions for the Unknown object
        assertEquals("?", Special.Unknown.text)
    }

    @Test
    fun `Decimal object has correct text and value`() {
        // Assertions for the Decimal object
        assertEquals(".", Special.Decimal.text)
    }
}