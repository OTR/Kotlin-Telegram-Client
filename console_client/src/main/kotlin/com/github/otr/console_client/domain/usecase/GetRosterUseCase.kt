package com.github.otr.console_client.domain.usecase

import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.repository.TelegramRepository

/**
 *
 */
class GetRosterUseCase(
    private val repository: TelegramRepository
) {

    operator fun invoke(): Roster {
        return repository.getRoster()
    }

}
