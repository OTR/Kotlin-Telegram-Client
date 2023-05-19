package com.github.otr.console_client.domain.repository

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.entity.chat.Roster

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
interface TelegramRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getPhoneNumber(): String

    fun setPhoneNumber(phoneNumber: String)

    fun getVerificationCode(): String

    fun setVerificationCode(verificationCode: String)

    fun getRoster(): Roster

}
