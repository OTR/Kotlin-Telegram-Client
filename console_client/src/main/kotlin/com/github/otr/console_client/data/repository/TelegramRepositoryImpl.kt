package com.github.otr.console_client.data.repository

import com.github.otr.console_client.data.network.ApiService
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class TelegramRepositoryImpl: TelegramRepository {

    private val apiService: ApiService = ApiService

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return apiService.authStateFlow
    }

    override fun setVerificationCode(verificationCode: String) {
        apiService.setVerificationCode(verificationCode)
    }

    override fun setPhoneNumber(phoneNumber: String) {
        apiService.setPhoneNumber(phoneNumber)
    }

}
