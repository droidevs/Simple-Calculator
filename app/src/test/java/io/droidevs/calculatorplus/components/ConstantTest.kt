package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.isE
import io.droidevs.calculatorplus.domain.components.isPi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConstantTest {

    @Test
    fun `PI object has correct text and value`() {
        // Assertions for the PI object
        assertEquals("π", Constant.PI.text)
        assertEquals(3.14159265358979323846, Constant.PI.apply(), 0.0) // 0.0 is the delta for floating-point comparison
    }

    @Test
    fun `E object has correct text and value`() {
        // Assertions for the E object
        assertEquals("e", Constant.E.text)
        assertEquals(2.71828182845904523536, Constant.E.apply(), 0.0) // 0.0 is the delta for floating-point comparison
    }

    @Test
    fun `isPi returns true for PI object and false for others`() {
        // Test isPi() extension function
        assertTrue(Constant.PI.isPi())
        assertFalse(Constant.E.isPi())
    }

    @Test
    fun `isE returns true for E object and false for others`() {
        // Test isE() extension function
        assertTrue(Constant.E.isE())
        assertFalse(Constant.PI.isE())
    }
}