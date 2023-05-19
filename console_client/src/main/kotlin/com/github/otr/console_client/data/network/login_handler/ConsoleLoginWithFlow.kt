package com.github.otr.console_client.data.network.login_handler

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.AuthState

import it.tdlight.client.AuthenticationData

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @see it.tdlight.client.AuthorizationStateWaitAuthenticationDataHandler.onAuthData
 */
class ConsoleLoginWithFlow(
    val consoleCLI: ConsoleClient
) : AuthenticationData {

    private companion object {
        val LOCK: Any = Any()
    }

    private var isInitialized = false
    private var phoneNumber: String? = null

    override fun getUserPhoneNumber(): String {
        initializeIfNeeded()
        return phoneNumber ?: throw RuntimeException("Phone number not set")
    }

    private fun initializeIfNeeded() {
        if (this.isInitialized) return

        synchronized(LOCK) {
            if (this.isInitialized) return

            var phoneNumber: String? = null

            runBlocking {
                launch {
                    consoleCLI.stateFlow.collect {
                        if (it == AuthState.SEND_PHONE_NUMBER) {
                            // TODO: Validate phone number
                            phoneNumber = it.message
                            this.coroutineContext.cancel()
                        }
                    }
                }
            }

            this.phoneNumber = phoneNumber
            this.isInitialized = true
        }
    }

    // Not used, but have to be overridden
    override fun isQrCode(): Boolean = false
    override fun isBot(): Boolean = false
    override fun getBotToken(): String = ""

}
