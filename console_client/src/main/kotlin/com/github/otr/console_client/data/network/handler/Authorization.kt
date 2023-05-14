package com.github.otr.console_client.data.network.handler

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.AuthState

import it.tdlight.client.GenericUpdateHandler
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
class UpdateAuthorizationStateHandler(
    private val consoleCLI: ConsoleClient
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger("AuthUpdate")
        private const val causePrefix: String = "Received"
    }

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        when (update.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                consoleCLI.emitState(AuthState.WAIT_TD_LIB_PARAMETERS)
                logAuthState(
                    causeName = "AuthorizationStateWaitTdlibParameters",
                    message = "Usually Simple Client handles this for you so you don't need"
                )
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                consoleCLI.emitState(AuthState.WAIT_PHONE_NUMBER)
                logAuthState(causeName = "AuthorizationStateWaitPhoneNumber", message = "TODO")
            }
            is TdApi.AuthorizationStateWaitEmailAddress -> {
                consoleCLI.emitState(AuthState.WAIT_EMAIL_ADDRESS)
                logAuthState(causeName = "AuthorizationStateWaitEmailAddress", message = "TODO")
            }
            is TdApi.AuthorizationStateWaitEmailCode -> {
                consoleCLI.emitState(AuthState.WAIT_EMAIL_CODE)
                logAuthState(causeName = "AuthorizationStateWaitEmailCode", message = "TODO")
            }
            is TdApi.AuthorizationStateWaitCode -> {
                consoleCLI.emitState(AuthState.WAIT_CODE)
                logAuthState(causeName = "AuthorizationStateWaitCode", message = "TODO")
            }
            is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
                consoleCLI.emitState(AuthState.WAIT_OTHER_DEVICE_CONFIRMATION)
                logAuthState(
                    causeName = "AuthorizationStateWaitOtherDeviceConfirmation",
                    message = "TODO"
                )
            }
            is TdApi.AuthorizationStateWaitRegistration -> {
                consoleCLI.emitState(AuthState.WAIT_REGISTRATION)
                logAuthState(causeName = "AuthorizationStateWaitRegistration", message = "TODO")
            }
            is TdApi.AuthorizationStateWaitPassword -> {
                consoleCLI.emitState(AuthState.WAIT_PASSWORD)
                logAuthState(causeName = "AuthorizationStateWaitPassword", message = "TODO")
            }
            is TdApi.AuthorizationStateReady -> {
                consoleCLI.emitState(AuthState.READY)
                logAuthState(causeName = "AuthorizationStateReady", message = "Logged in")
            }
            is TdApi.AuthorizationStateLoggingOut -> {
                consoleCLI.emitState(AuthState.LOGGING_OUT)
                logAuthState(causeName = "AuthorizationStateLoggingOut", message = "Logging out...")
            }
            is TdApi.AuthorizationStateClosing -> {
                consoleCLI.emitState(AuthState.CLOSING)
                logAuthState(causeName = "AuthorizationStateClosing", message = "Closing...")
            }
            is TdApi.AuthorizationStateClosed -> {
                consoleCLI.emitState(AuthState.CLOSED)
                logAuthState(causeName = "AuthorizationStateClosed", message = "Closed")
            }
            else -> {
                logAuthState(
                    causeName = "${update.authorizationState}",
                    message = "Unsupported state"
                )
            }
        }
    }

    /**
     *
     */
    private fun logAuthState(causeName: String, message: String) {
        logger.debug("$causePrefix $causeName, $message")
    }

}
