package com.github.otr.console_client.data.network.login_handler

import it.tdlight.client.AuthenticationData
import it.tdlight.common.utils.ScannerUtils

/**
 * @see it.tdlight.client.AuthorizationStateWaitAuthenticationDataHandler.onAuthData
 */
class MyConsoleLogin() : AuthenticationData {

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
            while (phoneNumber.isNullOrBlank()) {
                // TODO: Validate phone number
                 phoneNumber = ScannerUtils.askParameter(
                    "login", "Please type your phone number"
                )
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
