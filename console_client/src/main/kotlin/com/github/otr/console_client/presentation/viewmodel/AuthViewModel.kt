package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.data.repository.TelegramRepositoryConsoleImpl
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.repository.TelegramRepository
import com.github.otr.console_client.domain.usecase.GetAuthStateFlowUseCase
import com.github.otr.console_client.domain.usecase.GetPhoneNumberUseCase
import com.github.otr.console_client.domain.usecase.GetRosterUseCase
import com.github.otr.console_client.domain.usecase.GetVerificationCodeUseCase
import com.github.otr.console_client.domain.usecase.SetPhoneNumberUseCase
import com.github.otr.console_client.domain.usecase.SetVerificationCodeUseCase

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class AuthViewModel : RosterOwner {

    private val repository: TelegramRepository = TelegramRepositoryConsoleImpl()
    private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
    private val getPhoneNumberUseCase = GetPhoneNumberUseCase(repository)
    private val setPhoneNumberUseCase = SetPhoneNumberUseCase(repository)
    private val getVerificationCodeUseCase = GetVerificationCodeUseCase(repository)
    private val setVerificationCodeUseCase = SetVerificationCodeUseCase(repository)
    private val getRosterUseCase = GetRosterUseCase(repository)

    val authStateFlow: StateFlow<AuthState> = getAuthStateFlowUseCase()

    fun getPhoneNumber(): String {
        return getPhoneNumberUseCase()
    }

    fun setPhoneNumber(phoneNumber: String) {
        setPhoneNumberUseCase(phoneNumber)
    }

    fun getVerificationCode(): String {
        return getVerificationCodeUseCase()
    }

    fun setVerificationCode(verificationCode: String) {
        setVerificationCodeUseCase(verificationCode)
    }

    override fun getRoster(): Roster {
        return getRosterUseCase()
    }

}
