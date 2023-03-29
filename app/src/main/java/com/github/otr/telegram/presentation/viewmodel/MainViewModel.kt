package com.github.otr.telegram.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.github.otr.telegram.data.repository.FakeChatListRepositoryImpl
import com.github.otr.telegram.domain.entity.Chat
import com.github.otr.telegram.domain.repository.ChatListRepository
import com.github.otr.telegram.domain.usecase.ChangeFavoriteStatusUseCase
import com.github.otr.telegram.domain.usecase.GetChatListUseCase

class MainViewModel : ViewModel() {

    private val repository: ChatListRepository = FakeChatListRepositoryImpl()
    private val getChatListUseCase: GetChatListUseCase = GetChatListUseCase(repository)
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase = ChangeFavoriteStatusUseCase(repository)

    private var _chatListLiveData: MutableLiveData<List<Chat>> = MutableLiveData(listOf())
    val chatListLiveData: LiveData<List<Chat>>
        get() = _chatListLiveData

    init {
        updateChatList()
    }

    fun changeFavoriteStatus(chatItem: Chat) {
        changeFavoriteStatusUseCase(chatItem)
        updateChatList()
    }

    private fun updateChatList() {
        _chatListLiveData.value = getChatListUseCase()
    }

}
