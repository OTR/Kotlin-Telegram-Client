package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.login_handler.MyConsoleLogin
import com.github.otr.console_client.data.network.login_handler.MyScannerClientInteraction
import com.github.otr.console_client.domain.entity.AuthState
import it.tdlight.client.ClientInteraction

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
object ApiService {

    private val consoleCLI: ConsoleClient = ConsoleClient()

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
                customAuthMethod = MyConsoleLogin(),
                useCustomClientInteraction = true
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
        TODO("Api Service Not yet implemented")
    }

    fun setVerificationCode(verificationCode: String) {
        TODO("Api Service Not yet implemented")
    }

}
