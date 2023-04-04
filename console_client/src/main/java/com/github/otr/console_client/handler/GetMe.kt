package com.github.otr.console_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.Result
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

/**
 * <b>Sample Usage</b>
 * ```
 * client.addUpdatesHandler(onGetMe(client))
 * ```
 */
fun onGetMe(
    client: SimpleTelegramClient
): GenericUpdateHandler<TdApi.Update> = GenericUpdateHandler<TdApi.Update> { update ->
    if (update is TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateReady) {
            client.send(TdApi.GetMe()) { res: Result<TdApi.User> ->
                if (!res.isError) {
                    println("Hello, my name is: ${res.get().firstName}")
                }
            }
        }
    }
}
