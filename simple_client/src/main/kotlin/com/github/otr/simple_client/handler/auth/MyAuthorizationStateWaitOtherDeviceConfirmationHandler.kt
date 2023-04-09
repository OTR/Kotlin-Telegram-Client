package com.github.otr.simple_client.handler.auth

import it.tdlight.client.ClientInteraction
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import it.tdlight.client.ParameterInfoNotifyLink
import it.tdlight.jni.TdApi

/**
 *
 */
internal class MyAuthorizationStateWaitOtherDeviceConfirmationHandler(
    private val clientInteraction: ClientInteraction
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        val authorizationState = update.authorizationState
        if (authorizationState is TdApi.AuthorizationStateWaitOtherDeviceConfirmation) {
            val parameterInfo: ParameterInfo = ParameterInfoNotifyLink(authorizationState.link)
            clientInteraction.onParameterRequest(
                InputParameter.NOTIFY_LINK,
                parameterInfo,
                { ignored: String? -> } // FIXME:
            )
        }
    }

}
