package com.github.otr.simple_client.handler.auth

import com.github.otr.simple_client.handler.MyDefaultResultHandler

import it.tdlight.client.ClientInteraction
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import it.tdlight.client.ParameterInfoCode
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

/**
 *
 */
internal class MyAuthorizationStateWaitCodeHandler(
    private val client: TelegramClient,
    private val clientInteraction: ClientInteraction,
    private val exceptionHandler: ExceptionHandler
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        val authorizationState = update.authorizationState
        if (authorizationState is TdApi.AuthorizationStateWaitCode) {
            val parameterInfo: ParameterInfo = ParameterInfoCode(
                authorizationState.codeInfo.phoneNumber,
                authorizationState.codeInfo.nextType,
                authorizationState.codeInfo.timeout,
                authorizationState.codeInfo.type
            )
            clientInteraction.onParameterRequest(
                InputParameter.ASK_CODE, // parameter
                parameterInfo // parameterInfo
            ) { code: String? -> // result
                val response = TdApi.CheckAuthenticationCode(code)
                client.send(response, MyDefaultResultHandler, exceptionHandler)
            }
        }
    }

}
