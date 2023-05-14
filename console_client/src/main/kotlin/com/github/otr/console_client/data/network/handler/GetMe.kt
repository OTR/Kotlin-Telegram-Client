package com.github.otr.console_client.data.network.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.Result
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Send TdApi.GetMe() function, handle response which is TdApi.User update type.
 * Parse User object to get User's first name and greet itself by name.
 *
 * <b>Sample Usage</b>
 * ```
 * client.addUpdatesHandler(onGetMe(client))
 * ```
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.User.html">
 *     Documentation on returned type `TdApi.User`
 *     </a>
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.GetMe.html">
 *      Documentation on requested function `TdApi.GetMe`
 *     </a>
 */
fun onGetMe(
    client: SimpleTelegramClient
): GenericUpdateHandler<TdApi.Update> = GenericUpdateHandler<TdApi.Update> { update ->

    if (update is TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateReady) {
            client.send(TdApi.GetMe()) { result: Result<TdApi.User> ->
                if (!result.isError) {
                    val logger: Logger = LoggerFactory.getLogger("GetMe")
                    val causePrefix: String = "Received"
                    val causeName: String = "TdApi.User"
                    val user: TdApi.User = result.get()
                    val message: String = "Hello, my name is: ${user.firstName}"
                    logger.debug("$causePrefix $causeName, $message")
                }
            }
        }
    }

}
