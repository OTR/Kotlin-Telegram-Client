package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.model.BlankRosterImpl
import com.github.otr.console_client.data.network.config.RESOURCES_PATH
import com.github.otr.console_client.data.network.handler.CommonUpdatesHandler
import com.github.otr.console_client.data.network.handler.DefaultExceptionHandler
import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.handler.UpdateAuthorizationStateHandler
import com.github.otr.console_client.data.network.handler.onGetMe
import com.github.otr.console_client.data.network.handler.onStopCommand
import com.github.otr.console_client.data.network.handler.onUpdateNewMessage
import com.github.otr.console_client.data.network.login_handler.ClientInteractionWithFlow
import com.github.otr.console_client.data.network.login_handler.MyScannerClientInteraction
import com.github.otr.console_client.data.network.login_handler.TestClientInteraction
import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.domain.entity.chat.Roster

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.ClientInteraction
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.jni.TdApi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlin.io.path.Path

/**
 * @param useTestDc pass true to use Telegram test environment instead of the production environment. On Test DC use test phone numbers (like `9996611111`) to authorize
 *
 * @see it.tdlight.client.TDLibSettings.setUseTestDatacenter
 *
 * @see
 * <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.SetTdlibParameters.html#useTestDc">
 *     Documentation on a parameter `useTestDc` of a constructor
 *     </a>
 *
 * @see
 * <a href="https://core.telegram.org/bots/features#dedicated-test-environment">
 *     Documentation on Telegram test environment
 *     </a>
 *
 * @see
 * <a href="https://core.telegram.org/api/auth#test-accounts">
 *     Documentation on test phone numbers
 *     </a>
 */
class ConsoleClient(
    val roster: Roster = BlankRosterImpl(),
    val resourcesPath: String = RESOURCES_PATH,
    private val useTestDc: Boolean = false
) {

    // Create and configure a client
    private val client = SimpleTelegramClient(
        TDLibSettings.create(APIToken.example()).apply {
            databaseDirectoryPath = Path(resourcesPath).resolve("db")
            if (useTestDc) {
                setUseTestDatacenter(useTestDc)
            }
        }
    )

    private val _stateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.INITIAL)
    /**
     * A pair of private and public StateFlow's where Updates are emitted to
     */
    val stateFlow: StateFlow<AuthState> = _stateFlow.asStateFlow()

    /**
     * This method is user for self emitting States during Authorization process
     * in order to UI layer display different screens depend on the State
     * (e.g. A Screen where the User is asked for typing their phone number, or verification code)
     * Also, this method is used to emit user input to the Flow
     * in order the console client to send it to and interact with the TD Lib
     */
    fun emitState(value: AuthState) {
        _stateFlow.value = value
    }

    /**
     * Subscribe all the handlers for the given Update events
     *
     * @param handlerTypes pass all the enum values each of them represents suitable Update Handler
     *                      from `handler` package
     */
    fun addHandlers(vararg handlerTypes: HandlerType) {

        // Iterate over all the passed values and attach a suitable handler to the client
        for (handler in handlerTypes) {
            when (handler) {
                HandlerType.AUTH -> {
                    // Print out Authorization stages
                    client.addUpdateHandler(
                        TdApi.UpdateAuthorizationState::class.java,
                        UpdateAuthorizationStateHandler(this)
                    )
                }
                HandlerType.COMMAND -> {
                    // One way of passing Command Handler. Will trigger on /stop message
                    client.addCommandHandler<TdApi.Update>(
                        "stop",
                        onStopCommand(client)
                    )
                }
                HandlerType.COMMON -> {
                    // Sort of a sink. Will handle all Update Types received from Telegram server
                    // TODO: COMMON Handler includes AUTH handler, so you don't need to mention it
                    // separately
                    client.addUpdatesHandler(CommonUpdatesHandler(this))
                }
                HandlerType.GET_ME -> {
                    // Print out its nickname when successfully logged in
                    client.addUpdatesHandler(onGetMe(client))
                }
                HandlerType.NEW_MESSAGE -> {
                    // Print out a message received from private chat
                    client.addUpdateHandler(TdApi.UpdateNewMessage::class.java) { update ->
                        onUpdateNewMessage(update, client)
                    }
                }
            }
        }

        // Add default exception handler
        client.addDefaultExceptionHandler(DefaultExceptionHandler(this))
    }

    /**
     * Supposed to be the main infinite loop, FIXME: should be refactored
     */
    fun main(
        customAuthMethod: AuthenticationData = AuthenticationData.consoleLogin(),
        useCustomClientInteraction: ConsoleLogin = ConsoleLogin.MY_SCANNER_CLIENT_INTERACTION
//        customClientInteraction: Class<out ClientInteraction>? = null // TODO
    ) {
        // Initialize TDLight native libraries
        Init.start()

        // If clientInteraction is set, implement the custom Client Interaction
        // Otherwise do nothing, `it.tdlight.client.ScannerClientInteraction` will be used
        // FIXME: pass a reference to the preferred Client Interaction **class**
        //  instead of `when` statement
        when (useCustomClientInteraction) {
            ConsoleLogin.MY_SCANNER_CLIENT_INTERACTION -> {
                val interactor: ClientInteraction = MyScannerClientInteraction(
                    SimpleTelegramClient.blockingExecutor, client
                )
                client.setClientInteraction(interactor)
            }
            ConsoleLogin.CLIENT_INTERACTION_WITH_FLOW -> {
                val interactor: ClientInteraction = ClientInteractionWithFlow(
                    SimpleTelegramClient.blockingExecutor,
                    client,
                    this
                )
                client.setClientInteraction(interactor)
            }
            ConsoleLogin.TEST_CLIENT_INTERACTION -> {
                val interactor: ClientInteraction = TestClientInteraction(
                    SimpleTelegramClient.blockingExecutor, client
                )
                client.setClientInteraction(interactor)
            }

        }

        // On the first run will establish console dialog were asks for phone number and sends code
        client.start(customAuthMethod)

        // delay(5000)
        // Wait until TDLight is closed
        // client.sendClose()
        client.waitForExit()
    }

}

/**
 * FIXME: DELETE ME, pass a reference to the preferred Client Interaction class
 *  instead of `when` statement
 */
enum class ConsoleLogin {
    MY_SCANNER_CLIENT_INTERACTION,
    TEST_CLIENT_INTERACTION,
    CLIENT_INTERACTION_WITH_FLOW
}
