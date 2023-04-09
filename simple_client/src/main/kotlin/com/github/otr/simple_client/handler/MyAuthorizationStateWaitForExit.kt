package com.github.otr.simple_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.jni.TdApi.AuthorizationStateClosed
import it.tdlight.jni.TdApi.UpdateAuthorizationState

import java.util.concurrent.CountDownLatch

/**
 *
 */
internal class MyAuthorizationStateWaitForExit(
    private val closed: CountDownLatch
    ) : GenericUpdateHandler<UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: UpdateAuthorizationState) {
        if (update.authorizationState is AuthorizationStateClosed) {
            closed.countDown()
        }
    }

}
