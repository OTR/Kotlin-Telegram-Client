package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.data.repository.TelegramRepositoryImpl
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.repository.TelegramRepository
import com.github.otr.console_client.domain.usecase.GetAuthStateFlowUseCase
import com.github.otr.console_client.domain.usecase.SetPhoneNumberUseCase
import com.github.otr.console_client.domain.usecase.SetVerificationCodeUseCase

import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
class AuthViewModel {

    private val repository: TelegramRepository = TelegramRepositoryImpl()
    private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
    private val setPhoneNumberUseCase = SetPhoneNumberUseCase(repository)
    private val setVerificationCodeUseCase = SetVerificationCodeUseCase(repository)

    val authStateFlow: StateFlow<AuthState> = getAuthStateFlowUseCase()

    fun setPhoneNumber(phoneNumber: String) {
        setPhoneNumberUseCase(phoneNumber)
    }

    fun setVerificationCode(verificationCode: String) {
        setVerificationCodeUseCase(verificationCode)
    }

}
