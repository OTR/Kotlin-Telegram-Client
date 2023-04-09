package com.github.otr.simple_client.handler.auth

import com.github.otr.simple_client.handler.MyDefaultResultHandler

import it.tdlight.client.Authenticable
import it.tdlight.client.AuthenticationData
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

/**
 *
 */
internal class MyAuthorizationStateWaitAuthenticationDataHandler(
    private val client: TelegramClient,
    private val authenticable: Authenticable,
    private val exceptionHandler: ExceptionHandler
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateWaitPhoneNumber) {
            authenticable.getAuthenticationData { authenticationData: AuthenticationData ->
                onAuthData(authenticationData)
            }
        }
    }

    /**
     *
     */
    private fun onAuthData(authenticationData: AuthenticationData) {
        if (authenticationData.isBot) {
            val botToken = authenticationData.botToken
            val response = TdApi.CheckAuthenticationBotToken(botToken)
            client.send(response, MyDefaultResultHandler, exceptionHandler)
        } else if (authenticationData.isQrCode) {
            val response = TdApi.RequestQrCodeAuthentication()
            client.send(response, MyDefaultResultHandler, exceptionHandler)
        } else {
            val phoneSettings = TdApi.PhoneNumberAuthenticationSettings().apply {
                allowFlashCall = false
                allowMissedCall = false
                isCurrentPhoneNumber = false
                allowSmsRetrieverApi = false
                authenticationTokens = null
            }
            val phoneNumber = authenticationData.userPhoneNumber.toString()
            val response = TdApi.SetAuthenticationPhoneNumber(phoneNumber, phoneSettings)
            client.send(response, MyDefaultResultHandler, exceptionHandler)
        }
    }

}
