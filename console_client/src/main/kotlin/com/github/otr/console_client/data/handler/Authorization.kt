package com.github.otr.console_client.data.handler

import it.tdlight.jni.TdApi

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handle Authorization States which have sent from the TD Lib C++ module.
 * Each State represents a certain stage of the whole Authorization process.
 * If there is no 2FA set that casual Authorization Lifecycle is kind of the following:
 *   1. Always starts with **TdApi.AuthorizationStateWaitTdlibParameters** - it means that
 *     TD Lib waits for the client sending it's configuration
 *   2. If it's the first log in and no sessions have saved locally in file-based database
 *     TD Lib will asks for user's phone number by **TdApi.AuthorizationStateWaitPhoneNumber** state
 *   3. TODO: Fill me **TdApi.AuthorizationStateWaitCode**
 *   4. By **TdApi.AuthorizationStateReady** TD Lib says that you are successfully logged in.
 *     Since that stage initialization is over and you can send a variety of **TdApi.Function**'s
 *
 * <b>Sample Usage</b>
 * ```
 * client.addUpdateHandler(TdApi.UpdateAuthorizationState::class.java, ::onUpdateAuthorizationState)
 * ```
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.UpdateAuthorizationState.html">
 *     Documentation on returned type TdApi.UpdateAuthorizationState
 *     </a>
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.AuthorizationState.html">
 *     Which is a container for it's property of base abstract type TdApi.AuthorizationState
 *     </a>
 */
fun onUpdateAuthorizationState(update: TdApi.UpdateAuthorizationState) {

    val logger: Logger = LoggerFactory.getLogger("AuthUpdate")
    val causePrefix: String = "Received"

    when (update.authorizationState) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> {
            val causeName: String = "AuthorizationStateWaitTdlibParameters"
            val message: String = "Usually Simple Client handles this for you so you don't need"
            logger.debug("$causePrefix $causeName, $message")
        }
        is TdApi.AuthorizationStateReady -> {
            val causeName: String = "AuthorizationStateReady"
            val message: String = "Logged in"
            logger.debug("$causePrefix $causeName, $message")
        }
        is TdApi.AuthorizationStateClosing -> {
            val causeName: String = "AuthorizationStateClosing"
            val message: String = "Closing..."
            logger.debug("$causePrefix $causeName, $message")
        }
        is TdApi.AuthorizationStateClosed -> {
            val causeName: String = "AuthorizationStateClosed"
            val message: String = "Closed"
            logger.debug("$causePrefix $causeName, $message")
        }
        is TdApi.AuthorizationStateLoggingOut -> {
            val causeName: String = "AuthorizationStateLoggingOut"
            val message: String = "Logging out..."
            logger.debug("$causePrefix $causeName, $message")
        }
        else -> {
            val causeName: String = "${update.authorizationState}"
            val message: String = "Unsupported state"
            logger.debug("$causePrefix $causeName, $message")
        }
    }

}
