package com.github.otr.console_client.data.repository

import com.github.otr.console_client.data.model.BlankRosterImpl
import com.github.otr.console_client.data.network.ApiService
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.repository.TelegramRepository

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class TelegramRepositoryConsoleImpl: TelegramRepository {

    private val _roster: Roster = BlankRosterImpl()

    private val apiService: ApiService = ApiService(_roster)

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return apiService.authStateFlow
    }

    // A dummy code that asks the User for a phone number via STDIN
    override fun getPhoneNumber(): String {
        print(">>> Enter phone number: ")
        val phoneNumber: String = readln()
        return phoneNumber
    }

    override fun setPhoneNumber(phoneNumber: String) {
        apiService.setPhoneNumber(phoneNumber)
    }

    // A dummy code that asks the User for a verification code via STDIN
    override fun getVerificationCode(): String {
        print(">>> Enter verification code: ")
        val verificationCode: String = readln()
        return verificationCode
    }

    override fun setVerificationCode(verificationCode: String) {
        apiService.setVerificationCode(verificationCode)
    }

    override fun getRoster(): Roster {
        return _roster
    }

}
