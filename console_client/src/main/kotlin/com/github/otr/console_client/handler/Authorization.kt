package com.github.otr.console_client.handler

import it.tdlight.jni.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * <b>Sample Usage</b>
 * ```
 * client.addUpdateHandler(TdApi.UpdateAuthorizationState::class.java, ::onUpdateAuthorizationState)
 * ```
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
