package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.components.Parenthesis

class ParenthesisAction: Action("()", ActionValue.of(Parenthesis.OpenParenthesis, Parenthesis.CloseParenthesis))