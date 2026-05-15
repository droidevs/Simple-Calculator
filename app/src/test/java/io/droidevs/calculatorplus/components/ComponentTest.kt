package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import junit.framework.TestCase.assertEquals
import org.junit.Test

// Test file
class ComponentTest {

    @Test
    fun `identify returns correct Digit components for all numeric characters`() {
        assertEquals(Digit.Zero, Component.Companion.identify('0'))
        assertEquals(Digit.One, Component.Companion.identify('1'))
        assertEquals(Digit.Two, Component.Companion.identify('2'))
        assertEquals(Digit.Three, Component.Companion.identify('3'))
        assertEquals(Digit.Four, Component.Companion.identify('4'))
        assertEquals(Digit.Five, Component.Companion.identify('5'))
        assertEquals(Digit.Six, Component.Companion.identify('6'))
        assertEquals(Digit.Seven, Component.Companion.identify('7'))
        assertEquals(Digit.Eight, Component.Companion.identify('8'))
        assertEquals(Digit.Nine, Component.Companion.identify('9'))
    }

    @Test
    fun `identify returns correct Operator components for all operator characters`() {
        assertEquals(Operator.Plus, Component.Companion.identify('+'))
        assertEquals(Operator.Minus, Component.Companion.identify('-'))
        assertEquals(Operator.Multiply, Component.Companion.identify('×'))
        assertEquals(Operator.Divide, Component.Companion.identify('÷'))
        assertEquals(Operator.Factorial, Component.Companion.identify('!'))
    }

    @Test
    fun `identify returns correct Parenthesis components`() {
        assertEquals(Parenthesis.OpenParenthesis, Component.Companion.identify('('))
        assertEquals(Parenthesis.CloseParenthesis, Component.Companion.identify(')'))
    }

    @Test
    fun `identify returns correct Special components`() {
        assertEquals(Special.Decimal, Component.Companion.identify('.'))
    }

    @Test
    fun `identify returns correct Constant components`() {
        assertEquals(Constant.E, Component.Companion.identify('e'))
        assertEquals(Constant.PI, Component.Companion.identify('π'))
    }

    @Test
    fun `identify returns Unknown for unhandled characters`() {
        assertEquals(Special.Unknown, Component.Companion.identify('a'))
        assertEquals(Special.Unknown, Component.Companion.identify('b'))
        assertEquals(Special.Unknown, Component.Companion.identify('#'))
        assertEquals(Special.Unknown, Component.Companion.identify(' '))
        assertEquals(Special.Unknown, Component.Companion.identify('\n'))
    }
}