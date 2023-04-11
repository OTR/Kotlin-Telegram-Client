package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class GetAuthStateUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(): AuthState {
        return repository.getAuthState()
    }

}
