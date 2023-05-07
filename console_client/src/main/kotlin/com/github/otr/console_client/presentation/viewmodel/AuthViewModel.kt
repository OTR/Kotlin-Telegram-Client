package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.data.repository.TelegramRepositoryImpl
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class AuthViewModel {

    private val repository: TelegramRepository = TelegramRepositoryImpl()

    val authStateFlow: StateFlow<AuthState> = repository.getAuthStateFlow()

}
