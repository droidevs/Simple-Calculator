package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.SpecialToken

class ClearUseCase {
    operator fun  invoke(): LinkedToken {
        return SpecialToken.EmptyToken()
    }
} 