package com.github.otr.telegram.domain.repository

import com.github.otr.telegram.domain.entity.Chat

interface ChatListRepository {

    fun getChatList(): List<Chat>

    fun changeFavoriteStatus(chatItem: Chat)

}
