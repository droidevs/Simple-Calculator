package io.droidevs.calculatorplus.tokens


import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import org.junit.Assert.*
import org.junit.Test

class FunctionTokenTest {

    // ----- VALIDATION TESTS -----

    @Test
    fun testFunctionAfterConstantIsInvalid() {
        val func = FunctionToken.SinToken()
        val constant = Constant.E
        assertFalse(func.isValid(ValidationArgument(constant, ClcFunction.Sin)))
    }

    @Test
    fun testFunctionAfterDigitIsInvalid() {
        val func = FunctionToken.CosToken()
        val digit = Digit.Five
        assertFalse(func.isValid(ValidationArgument(digit, ClcFunction.Cos)))
    }

    @Test
    fun testFunctionAfterDecimalIsInvalid() {
        val func = FunctionToken.TanToken()
        val decimal = Special.Decimal
        assertFalse(func.isValid(ValidationArgument(decimal, ClcFunction.Tan)))
    }

    @Test
    fun testFunctionAfterAnotherFunctionIsInvalid() {
        val func = FunctionToken.SinToken()
        val prevFunc = ClcFunction.Cos
        assertFalse(func.isValid(ValidationArgument(prev = prevFunc, current = ClcFunction.Sin)))
    }

    @Test
    fun testFunctionAfterOpenParenthesisIsValid() {
        val func = FunctionToken.LogToken()
        val prev = Parenthesis.OpenParenthesis
        assertTrue(func.isValid(ValidationArgument(prev, ClcFunction.Log)))
    }

    @Test
    fun testFunctionAfterCloseParenthesisIsInvalid() {
        val func = FunctionToken.LnToken()
        val prev = Parenthesis.CloseParenthesis
        assertFalse(func.isValid(ValidationArgument(prev, ClcFunction.Ln)))
    }

    @Test
    fun testFunctionAfterAdditionOperatorIsValid() {
        val func = FunctionToken.SqrtToken()
        val prev = Operator.Plus
        assertTrue(func.isValid(ValidationArgument(prev, ClcFunction.SquareRoot)))
    }

    @Test
    fun testFunctionAfterPercentOperatorIsInvalid() {
        val func = FunctionToken.SqrtToken()
        val prev = Operator.Percent
        assertFalse(func.isValid(ValidationArgument(prev, ClcFunction.SquareRoot)))
    }

    // ----- FUNCTION TYPE CHECK TESTS -----

    @Test
    fun testIsSine() {
        assertTrue(FunctionToken.SinToken().isSine())
        assertFalse(FunctionToken.CosToken().isSine())
    }

    @Test
    fun testIsCosine() {
        assertTrue(FunctionToken.CosToken().isCosine())
        assertFalse(FunctionToken.SinToken().isCosine())
    }

    @Test
    fun testIsTangent() {
        assertTrue(FunctionToken.TanToken().isTangent())
        assertFalse(FunctionToken.SinToken().isTangent())
    }

    @Test
    fun testIsArcFunctions() {
        assertTrue(FunctionToken.ACosToken().isArcCosine())
        assertTrue(FunctionToken.ASinToken().isArcSine())
        assertTrue(FunctionToken.ATanToken().isArcTangent())
    }

    @Test
    fun testIsHyperbolicFunctions() {
        assertTrue(FunctionToken.SinHToken().isHyperbolicSine())
        assertTrue(FunctionToken.CosHToken().isHyperbolicCosine())
        assertTrue(FunctionToken.TanHToken().isHyperbolicTangent())
    }

    @Test
    fun testIsInverseHyperbolicFunctions() {
        assertTrue(FunctionToken.ASinHToken().isInverseHyperbolicSine())
        assertTrue(FunctionToken.ACosHToken().isInverseHyperbolicCosine())
        assertTrue(FunctionToken.ATanHToken().isInverseHyperbolicTangent())
    }

    @Test
    fun testIsSquareRoot() {
        assertTrue(FunctionToken.SqrtToken().isSquareRoot())
    }

    @Test
    fun testIsLogarithms() {
        val log = FunctionToken.LogToken()
        val ln = FunctionToken.LnToken()

        // ⚠ depends on naming, see logic review above
        assertTrue(log.isCommonLogarithm())
        assertTrue(ln.isNaturalLogarithm())
    }
}
