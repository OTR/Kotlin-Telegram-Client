package com.github.otr.console_client.data.network.login_handler

import it.tdlight.client.AuthenticationData

/**
 * This implementation is used in tests only
 * This will work out only when authenticating on Telegram Test Datacenters
 * This handler always returns the same phone number `9996611111`
 */
object TestDcLogin : AuthenticationData {

    private const val TEST_PHONE_NUMBER: String = "9996611111"

    override fun getUserPhoneNumber(): String = TEST_PHONE_NUMBER

    // Not used, but have to be overridden
    override fun isQrCode(): Boolean = false
    override fun isBot(): Boolean = false
    override fun getBotToken(): String = ""

}
