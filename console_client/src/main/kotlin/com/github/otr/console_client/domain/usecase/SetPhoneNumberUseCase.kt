package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class SetPhoneNumberUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(phoneNumber: String) {
        repository.setPhoneNumber(phoneNumber)
    }

}
