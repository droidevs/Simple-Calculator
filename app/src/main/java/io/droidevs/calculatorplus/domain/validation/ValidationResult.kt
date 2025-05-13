package io.droidevs.calculatorplus.domain.validation


open class ValidationResult {

    object Valid: ValidationResult()

    object Invalid: ValidationResult()


    fun isValid() : Boolean {
        return this is Valid
    }

    fun isInvalid() : Boolean {
        return this is Invalid
    }

}