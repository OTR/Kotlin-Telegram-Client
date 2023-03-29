package com.github.otr.telegram.domain.usecase

import com.github.otr.telegram.domain.entity.Chat
import com.github.otr.telegram.domain.repository.ChatListRepository

class ChangeFavoriteStatusUseCase(
    private val repository: ChatListRepository
) {

    operator fun invoke(chatItem: Chat) {
        repository.changeFavoriteStatus(chatItem)
    }

}
