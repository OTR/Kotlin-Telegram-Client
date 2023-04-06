package com.github.otr.console_client

import com.github.otr.console_client.handler.*

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.jni.TdApi
import kotlin.io.path.Path

// The directory where TD Lib settings and cache located
private const val RESOURCES_PATH: String = "console_client/src/main/resources/tdlib/"

class ConsoleClient {

    // Create and configure a client
    private val client = SimpleTelegramClient(
        TDLibSettings.create(APIToken.example()).apply {
            databaseDirectoryPath = Path(RESOURCES_PATH).resolve("db")
        }
    )

    /**
     * Subscribe all the handlers for the given Update events
     *
     * @param handlerTypes pass all the enum values each of them represents suitable Update Handler
     *                      from `handler` package
     */
    fun build(vararg handlerTypes: HandlerType) {

        // Iterate over all the passed values and attach a suitable handler to the client
        for (handler in handlerTypes) {
            when (handler) {
                HandlerType.AUTH -> {
                    // Print out Authorization stages
                    client.addUpdateHandler(
                        TdApi.UpdateAuthorizationState::class.java,
                        ::onUpdateAuthorizationState
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
                    client.addUpdatesHandler(CommonUpdatesHandler(client))
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
    }

    fun main() {
        // Initialize TDLight native libraries
        Init.start()

        // On the first run will establish console dialog were asks for phone number and sends code
        client.start(AuthenticationData.consoleLogin())

        // Wait until TDLight is closed
        client.waitForExit()
    }

}

fun main() {
    val client: ConsoleClient = ConsoleClient()
    client.build(HandlerType.AUTH, HandlerType.COMMON)
    client.main()
}
