package io.droidevs.calculatorplus.domain.services

// Preprocessor to clean and standardize input expressions
// before doing anything else like simplifying or evaluating expression [tokenizing]
class ExpressionPreprocessor {

    fun preprocess(expression: String): String {
        return expression.replace("""\s+""".toRegex(), "") // Remove all whitespace
    }

}