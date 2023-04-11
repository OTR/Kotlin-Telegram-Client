package com.github.otr.console_client.domain.repository

import com.github.otr.console_client.domain.entity.AuthState

/**
 *
 */
interface TelegramRepository {

    fun getAuthState(): AuthState

}
