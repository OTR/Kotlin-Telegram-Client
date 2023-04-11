package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.data.repository.TelegramRepositoryImpl
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *
 */
class AuthViewModel {

    private val repository: TelegramRepository = TelegramRepositoryImpl()


    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.INITIAL)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

}
