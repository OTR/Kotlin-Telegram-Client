package com.github.otr.console_client.domain.repository

import com.github.otr.console_client.domain.entity.AuthState

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
interface TelegramRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

}
