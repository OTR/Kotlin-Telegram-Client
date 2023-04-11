package com.github.otr.simple_client.handler.auth

import com.github.otr.simple_client.handler.MyDefaultResultHandler

import it.tdlight.client.ClientInteraction
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import it.tdlight.client.ParameterInfoPasswordHint
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

/**
 *
 */
internal class MyAuthorizationStateWaitPasswordHandler(
    private val client: TelegramClient,
    private val clientInteraction: ClientInteraction,
    private val exceptionHandler: ExceptionHandler
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        val authorizationState = update.authorizationState
        if (authorizationState is TdApi.AuthorizationStateWaitPassword) {
            val parameterInfo: ParameterInfo = ParameterInfoPasswordHint(
                authorizationState.passwordHint,
                authorizationState.hasRecoveryEmailAddress,
                authorizationState.recoveryEmailAddressPattern
            )
            clientInteraction.onParameterRequest(
                InputParameter.ASK_PASSWORD, // parameter
                parameterInfo // parameterInfo
            ) { password: String? -> // result
                val response = TdApi.CheckAuthenticationPassword(password)
                client.send(response, MyDefaultResultHandler, exceptionHandler)
            }
        }
    }

}
