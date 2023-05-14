package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class GetAuthStateFlowUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }

}
