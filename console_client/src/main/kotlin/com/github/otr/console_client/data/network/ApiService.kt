package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.login_handler.ConsoleLoginWithFlow
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.entity.chat.Roster

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class ApiService(
    val roster: Roster
) {

    private val consoleCLI: ConsoleClient = ConsoleClient(roster)

    private val _authStateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.INITIAL)
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        main()
    }

    private fun main() {
        consoleCLI.addHandlers(HandlerType.COMMON)

        val waitForExitJob: Job = scope.launch {
            consoleCLI.main(
                customAuthMethod = ConsoleLoginWithFlow(consoleCLI),
                useCustomClientInteraction = ConsoleLogin.CLIENT_INTERACTION_WITH_FLOW
            )
        }

        scope.launch {
            consoleCLI.stateFlow.collect {
                _authStateFlow.value = it
            }
        }

        scope.launch {
            waitForExitJob.join()
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        val phoneNumberState: AuthState = AuthState.SEND_PHONE_NUMBER
        phoneNumberState.message = phoneNumber
        consoleCLI.emitState(phoneNumberState)
    }

    fun setVerificationCode(verificationCode: String) {
        val verificationCodeState: AuthState = AuthState.SEND_VERIFICATION_CODE
        verificationCodeState.message = verificationCode
        consoleCLI.emitState(verificationCodeState)
    }

}
