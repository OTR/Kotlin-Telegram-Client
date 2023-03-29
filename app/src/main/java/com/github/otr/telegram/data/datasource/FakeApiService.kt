package com.github.otr.telegram.data.datasource

import com.github.otr.telegram.domain.entity.Chat

class FakeApiService {

    /**
     * Return a list of fake chats
     * TODO: For testing purposes only
     */
    fun getChatList(): List<Chat> {

        return listOf(
            Chat(1L, "Andrew", false),
            Chat(2L, "Bob", false),
            Chat(3L, "Charly", false),
            Chat(4L, "Danny", false),
        )
    }

}
