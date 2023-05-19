package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class GetVerificationCodeUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(): String {
        return repository.getVerificationCode()
    }

}
