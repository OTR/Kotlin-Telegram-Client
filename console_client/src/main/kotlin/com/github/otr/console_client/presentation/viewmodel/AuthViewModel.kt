package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.data.repository.TelegramRepositoryImpl
import com.github.otr.console_client.domain.repository.TelegramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *
 */
class AuthViewModel {

    private val repository: TelegramRepository = TelegramRepositoryImpl()


    private val _authState: MutableStateFlow<String> = MutableStateFlow("")
    val authState: StateFlow<String> = _authState.asStateFlow()

}
