package com.github.otr.telegram.data.repository

import com.github.otr.telegram.data.datasource.FakeApiService
import com.github.otr.telegram.domain.entity.Chat
import com.github.otr.telegram.domain.repository.ChatListRepository

class FakeChatListRepositoryImpl: ChatListRepository {

    private val apiService: FakeApiService = FakeApiService()

    private var _chatList: MutableList<Chat> = mutableListOf()
    // TODO: Go learn OOP, script kiddy
      private val chatLists: List<Chat>
        get() = _chatList.toList()

    init {
        _chatList = apiService.getChatList().toMutableList()
    }

    override fun getChatList(): List<Chat> {
        return chatLists
    }

    override fun changeFavoriteStatus(chatItem: Chat) {
        val foundItem: Chat = chatLists.find { it.id == chatItem.id }
            ?: throw NoSuchElementException("There is no such element in chatList")
        val itemIndex: Int = chatLists.indexOf(foundItem)
        val oldItem: Chat = chatLists[itemIndex]
        val oldFavoriteStatus: Boolean = oldItem.favorite
        val newItem = oldItem.copy(favorite = !oldFavoriteStatus)
        _chatList[itemIndex] = newItem
    }

}
