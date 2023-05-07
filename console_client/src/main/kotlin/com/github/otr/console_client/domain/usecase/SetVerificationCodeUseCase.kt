package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class SetVerificationCodeUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(verificationCode: String) {
        repository.setVerificationCode(verificationCode)
    }

}
