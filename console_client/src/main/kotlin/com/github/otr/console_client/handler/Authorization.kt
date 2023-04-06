package com.github.otr.console_client.handler

import it.tdlight.jni.TdApi


/**
 * <b>Sample Usage</b>
 * ```
 * client.addUpdateHandler(TdApi.UpdateAuthorizationState::class.java, ::onUpdateAuthorizationState)
 * ```
 */
fun onUpdateAuthorizationState(update: TdApi.UpdateAuthorizationState) {
    when (update.authorizationState) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> {
            println("Usually Simple Client handles this for you so you don't need")
        }
        is TdApi.AuthorizationStateReady -> println("Logged in")
        is TdApi.AuthorizationStateClosing -> println("Closing...")
        is TdApi.AuthorizationStateClosed ->  println("Closed")
        is TdApi.AuthorizationStateLoggingOut ->  println("Logging out...")
        else -> println("Unsupported state: ${update.authorizationState}")
    }
}
