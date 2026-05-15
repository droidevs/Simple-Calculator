package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.isEight
import io.droidevs.calculatorplus.domain.components.isFive
import io.droidevs.calculatorplus.domain.components.isFour
import io.droidevs.calculatorplus.domain.components.isNine
import io.droidevs.calculatorplus.domain.components.isOne
import io.droidevs.calculatorplus.domain.components.isSeven
import io.droidevs.calculatorplus.domain.components.isSix
import io.droidevs.calculatorplus.domain.components.isThree
import io.droidevs.calculatorplus.domain.components.isTwo
import io.droidevs.calculatorplus.domain.components.isZero
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.token.DigitToken
import junit.framework.TestCase
import org.junit.Test

class DigitTest {

    // Test cases for toToken()
    @Test
    fun `toToken maps Digit Zero to ZeroToken`() {
        val token = Digit.Zero.toToken()
        TestCase.assertTrue(token is DigitToken.ZeroToken)
    }

    @Test
    fun `toToken maps Digit One to OneToken`() {
        val token = Digit.One.toToken()
        TestCase.assertTrue(token is DigitToken.OneToken)
    }

    @Test
    fun `toToken maps Digit Two to TwoToken`() {
        val token = Digit.Two.toToken()
        TestCase.assertTrue(token is DigitToken.TwoToken)
    }

    @Test
    fun `toToken maps Digit Three to ThreeToken`() {
        val token = Digit.Three.toToken()
        TestCase.assertTrue(token is DigitToken.ThreeToken)
    }

    @Test
    fun `toToken maps Digit Four to FourToken`() {
        val token = Digit.Four.toToken()
        TestCase.assertTrue(token is DigitToken.FourToken)
    }

    @Test
    fun `toToken maps Digit Five to FiveToken`() {
        val token = Digit.Five.toToken()
        TestCase.assertTrue(token is DigitToken.FiveToken)
    }

    @Test
    fun `toToken maps Digit Six to SixToken`() {
        val token = Digit.Six.toToken()
        TestCase.assertTrue(token is DigitToken.SixToken)
    }

    @Test
    fun `toToken maps Digit Seven to SevenToken`() {
        val token = Digit.Seven.toToken()
        TestCase.assertTrue(token is DigitToken.SevenToken)
    }

    @Test
    fun `toToken maps Digit Eight to EightToken`() {
        val token = Digit.Eight.toToken()
        TestCase.assertTrue(token is DigitToken.EightToken)
    }

    @Test
    fun `toToken maps Digit Nine to NineToken`() {
        val token = Digit.Nine.toToken()
        TestCase.assertTrue(token is DigitToken.NineToken)
    }

    // Test cases for is...() functions
    @Test
    fun `isZero returns true for Digit Zero and false for others`() {
        TestCase.assertTrue(Digit.Zero.isZero())
        TestCase.assertFalse(Digit.One.isZero())
        TestCase.assertFalse(Digit.Five.isZero())
    }

    @Test
    fun `isOne returns true for Digit One and false for others`() {
        TestCase.assertTrue(Digit.One.isOne())
        TestCase.assertFalse(Digit.Zero.isOne())
        TestCase.assertFalse(Digit.Nine.isOne())
    }

    @Test
    fun `isTwo returns true for Digit Two and false for others`() {
        TestCase.assertTrue(Digit.Two.isTwo())
        TestCase.assertFalse(Digit.Three.isTwo())
    }

    @Test
    fun `isThree returns true for Digit Three and false for others`() {
        TestCase.assertTrue(Digit.Three.isThree())
        TestCase.assertFalse(Digit.Four.isThree())
    }

    @Test
    fun `isFour returns true for Digit Four and false for others`() {
        TestCase.assertTrue(Digit.Four.isFour())
        TestCase.assertFalse(Digit.Five.isFour())
    }

    @Test
    fun `isFive returns true for Digit Five and false for others`() {
        TestCase.assertTrue(Digit.Five.isFive())
        TestCase.assertFalse(Digit.Six.isFive())
    }

    @Test
    fun `isSix returns true for Digit Six and false for others`() {
        TestCase.assertTrue(Digit.Six.isSix())
        TestCase.assertFalse(Digit.Seven.isSix())
    }

    @Test
    fun `isSeven returns true for Digit Seven and false for others`() {
        TestCase.assertTrue(Digit.Seven.isSeven())
        TestCase.assertFalse(Digit.Eight.isSeven())
    }

    @Test
    fun `isEight returns true for Digit Eight and false for others`() {
        TestCase.assertTrue(Digit.Eight.isEight())
        TestCase.assertFalse(Digit.Nine.isEight())
    }

    @Test
    fun `isNine returns true for Digit Nine and false for others`() {
        TestCase.assertTrue(Digit.Nine.isNine())
        TestCase.assertFalse(Digit.Zero.isNine())
    }
}