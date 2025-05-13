package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.components.Operator


abstract class OperatorExpression(
    val operator: Operator
) : Expression()

