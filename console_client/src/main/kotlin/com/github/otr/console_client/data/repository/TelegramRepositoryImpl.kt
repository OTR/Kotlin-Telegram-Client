package com.github.otr.console_client.data.repository

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *
 */
class TelegramRepositoryImpl: TelegramRepository {

    private val _authStateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.INITIAL)

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return _authStateFlow.asStateFlow()
    }

}
