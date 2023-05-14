package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.config.RESOURCES_PATH
import com.github.otr.console_client.data.network.handler.CommonUpdatesHandler
import com.github.otr.console_client.data.network.handler.DefaultExceptionHandler
import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.handler.UpdateAuthorizationStateHandler
import com.github.otr.console_client.data.network.handler.onGetMe
import com.github.otr.console_client.data.network.handler.onStopCommand
import com.github.otr.console_client.data.network.handler.onUpdateNewMessage
import com.github.otr.console_client.data.network.login_handler.MyScannerClientInteraction
import com.github.otr.console_client.domain.entity.AuthState

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.ClientInteraction
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.jni.TdApi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlin.io.path.Path

class ConsoleClient(
    val resourcesPath: String = RESOURCES_PATH
) {

    // Create and configure a client
    private val client = SimpleTelegramClient(
        TDLibSettings.create(APIToken.example()).apply {
            databaseDirectoryPath = Path(resourcesPath).resolve("db")
        }
    )

    // A pair of private and public StateFlow's where Updates are emitted to
    private val _stateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.INITIAL)
    val stateFlow: StateFlow<AuthState> = _stateFlow.asStateFlow()

    /**
     *
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

    fun main(
        customAuthMethod: AuthenticationData = AuthenticationData.consoleLogin(),
        useCustomClientInteraction: Boolean = false
//        customClientInteraction: Class<out ClientInteraction>? = null // TODO
    ) {
        // Initialize TDLight native libraries
        Init.start()

        // If clientInteraction is set, implement the custom Client Interaction
        // Otherwise do nothing, `it.tdlight.client.ScannerClientInteraction` will be used
        if (useCustomClientInteraction) {
            val interactor: ClientInteraction = MyScannerClientInteraction(
                SimpleTelegramClient.blockingExecutor, client
            )
            client.setClientInteraction(interactor)
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
 * Just for testing purposes
 */
suspend fun main() {
    val consoleCLI: ConsoleClient = ConsoleClient()
    consoleCLI.addHandlers(HandlerType.COMMON)

    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    val waitForExitJob: Job = scope.launch {
        consoleCLI.main()
    }

    scope.launch {
        consoleCLI.stateFlow.collect {
            println(it)
        }
    }

    waitForExitJob.join()
}
