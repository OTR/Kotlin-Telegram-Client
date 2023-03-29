package com.github.otr.telegram.domain.usecase

import com.github.otr.telegram.domain.entity.Chat
import com.github.otr.telegram.domain.repository.ChatListRepository

class GetChatListUseCase(
    private val repository: ChatListRepository
) {

    operator fun invoke(): List<Chat> {
        return repository.getChatList()
    }

}
