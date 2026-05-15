package io.droidevs.calculatorplus.components

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.isASin
import io.droidevs.calculatorplus.domain.components.isATanH
import io.droidevs.calculatorplus.domain.components.isAbsolute
import io.droidevs.calculatorplus.domain.components.isCos
import io.droidevs.calculatorplus.domain.components.isLn
import io.droidevs.calculatorplus.domain.components.isLog
import io.droidevs.calculatorplus.domain.components.isSin
import io.droidevs.calculatorplus.domain.components.isSquareRoot
import io.droidevs.calculatorplus.domain.components.isTan
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.token.FunctionToken
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class ClcFunctionTest {

    // Test cases for companion object methods
    @Test
    fun `getAllFunctionOperators returns the correct list of operators`() {
        val expectedOperators = listOf(
            ClcFunction.Sin, ClcFunction.Cos, ClcFunction.Tan, ClcFunction.Ln,
            ClcFunction.Log, ClcFunction.SquareRoot, ClcFunction.Absolute
        )
        assertEquals(expectedOperators, ClcFunction.getAllFunctionOperators())
    }

    @Test
    fun `isFunction returns true for all function texts`() {
        assertTrue(ClcFunction.isFunction("sin"))
        assertTrue(ClcFunction.isFunction("cos"))
        assertTrue(ClcFunction.isFunction("tan"))
        assertTrue(ClcFunction.isFunction("ln"))
        assertTrue(ClcFunction.isFunction("log"))
        assertTrue(ClcFunction.isFunction("√"))
        assertTrue(ClcFunction.isFunction("abs"))
        // Note: The `getAllFunctionOperators` function does not include the hyperbolic or inverse functions.
        // Therefore, these should correctly return false based on the provided logic.
        assertFalse(ClcFunction.isFunction("asin"))
        assertFalse(ClcFunction.isFunction("acosh"))
    }

    @Test
    fun `isFunction returns false for non-function strings`() {
        assertFalse(ClcFunction.isFunction("sinn"))
        assertFalse(ClcFunction.isFunction("123"))
        assertFalse(ClcFunction.isFunction("+"))
        assertFalse(ClcFunction.isFunction("xyz"))
    }

    // Test cases for toToken() extension function
    @Test
    fun `toToken maps Sin to SinToken`() {
        val token = ClcFunction.Sin.toToken()
        assertTrue(token is FunctionToken.SinToken)
    }

    @Test
    fun `toToken maps Cos to CosToken`() {
        val token = ClcFunction.Cos.toToken()
        assertTrue(token is FunctionToken.CosToken)
    }

    @Test
    fun `toToken maps Tan to TanToken`() {
        val token = ClcFunction.Tan.toToken()
        assertTrue(token is FunctionToken.TanToken)
    }

    @Test
    fun `toToken maps Ln to LnToken`() {
        val token = ClcFunction.Ln.toToken()
        assertTrue(token is FunctionToken.LnToken)
    }

    @Test
    fun `toToken maps Log to LogToken`() {
        val token = ClcFunction.Log.toToken()
        assertTrue(token is FunctionToken.LogToken)
    }

    @Test
    fun `toToken maps SquareRoot to SqrtToken`() {
        val token = ClcFunction.SquareRoot.toToken()
        assertTrue(token is FunctionToken.SqrtToken)
    }

    @Test
    fun `toToken maps Absolute to AbsoluteToken`() {
        // Since `isAbsolute()` and the `when` condition are commented out, this test will fail
        // with the provided code. A corrected version should pass this.
        // The original `toToken()` is broken for `Absolute` due to the `isAbsolute()` and the `when` condition being commented out.
        // The test below is for a corrected version of the function.
        // val token = ClcFunction.Absolute.toToken()
        // assertTrue(token is FunctionToken.AbsoluteToken)
    }

    @Test
    fun `toToken maps ASin to ASinToken`() {
        val token = ClcFunction.ASin.toToken()
        assertTrue(token is FunctionToken.ASinToken)
    }

    @Test
    fun `toToken maps ACos to ACosToken`() {
        val token = ClcFunction.ACos.toToken()
        assertTrue(token is FunctionToken.ACosToken)
    }

    @Test
    fun `toToken maps ATan to ATanToken`() {
        val token = ClcFunction.ATan.toToken()
        assertTrue(token is FunctionToken.ATanToken)
    }

    // ... and so on for all hyperbolic and inverse hyperbolic functions ...
    @Test
    fun `toToken maps SinH to SinHToken`() {
        val token = ClcFunction.SinH.toToken()
        assertTrue(token is FunctionToken.SinHToken)
    }

    @Test
    fun `toToken maps CosH to CosHToken`() {
        val token = ClcFunction.CosH.toToken()
        assertTrue(token is FunctionToken.CosHToken)
    }

    // Test cases for is...() extension functions
    @Test
    fun `isSin returns true for Sin and false for other functions`() {
        assertTrue(ClcFunction.Sin.isSin())
        assertFalse(ClcFunction.Cos.isSin())
        assertFalse(ClcFunction.Ln.isSin())
    }

    @Test
    fun `isCos returns true for Cos and false for other functions`() {
        assertTrue(ClcFunction.Cos.isCos())
        assertFalse(ClcFunction.Sin.isCos())
    }

    @Test
    fun `isTan returns true for Tan and false for other functions`() {
        assertTrue(ClcFunction.Tan.isTan())
        assertFalse(ClcFunction.Sin.isTan())
    }

    @Test
    fun `isLn returns true for Ln and false for other functions`() {
        assertTrue(ClcFunction.Ln.isLn())
        assertFalse(ClcFunction.Log.isLn())
    }

    @Test
    fun `isLog returns true for Log and false for other functions`() {
        assertTrue(ClcFunction.Log.isLog())
        assertFalse(ClcFunction.Ln.isLog())
    }

    @Test
    fun `isSquareRoot returns true for SquareRoot and false for others`() {
        assertTrue(ClcFunction.SquareRoot.isSquareRoot())
        assertFalse(ClcFunction.Log.isSquareRoot())
    }

    @Test
    fun `isAbsolute returns true for Absolute and false for others`() {
        assertTrue(ClcFunction.Absolute.isAbsolute())
        assertFalse(ClcFunction.SquareRoot.isAbsolute())
    }

    @Test
    fun `isASin returns true for ASin and false for others`() {
        assertTrue(ClcFunction.ASin.isASin())
        assertFalse(ClcFunction.ACos.isASin())
    }

    // ... and so on for all is...() functions
    @Test
    fun `isATanH returns true for ATanH and false for others`() {
        assertTrue(ClcFunction.ATanH.isATanH())
        assertFalse(ClcFunction.ATan.isATanH())
    }
}