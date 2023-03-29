package com.github.otr.telegram.data.repository

import com.github.otr.telegram.domain.entity.Chat
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeChatListRepositoryImplTest {

    @Test
    fun getChatList() {
        val repository: FakeChatListRepositoryImpl = FakeChatListRepositoryImpl()
        // WHEN
        val list: List<Chat> = repository.getChatList()
        // THEN
        val expected: Int = 4
        val actual: Int = list.size
        assertEquals(expected, actual)
    }

    @Test
    fun changeFavoriteStatus() {
        val repository: FakeChatListRepositoryImpl = FakeChatListRepositoryImpl()
        val list: List<Chat> = repository.getChatList()
        val firstItem: Chat = list[0].copy()
        assertEquals(firstItem.favorite, false)
        // WHEN
        repository.changeFavoriteStatus(firstItem)
        // THEN
        val expected: Boolean =  true
        val actual: Boolean = repository.getChatList()[0].favorite
        assertEquals(expected, actual)
    }
}
