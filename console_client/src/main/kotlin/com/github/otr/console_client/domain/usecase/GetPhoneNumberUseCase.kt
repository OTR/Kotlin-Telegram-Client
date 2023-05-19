package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class GetPhoneNumberUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(): String {
        return repository.getPhoneNumber()
    }

}
