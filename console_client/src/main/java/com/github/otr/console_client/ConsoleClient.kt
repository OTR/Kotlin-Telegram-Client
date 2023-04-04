package com.github.otr.console_client

import com.github.otr.console_client.handler.finishCommandHandler
import com.github.otr.console_client.handler.onCommonUpdates
import com.github.otr.console_client.handler.onGetMe
import com.github.otr.console_client.handler.onUpdateAuthorizationState
import com.github.otr.console_client.handler.onUpdateNewMessage
import com.github.otr.console_client.handler.stopCommandHandler

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.jni.TdApi
import kotlin.io.path.Path

// The directory where TD Lib settings and cache located
private const val RESOURCES_PATH: String = "console_client/src/main/resources/tdlib/"

fun main() {
    Init.start()

    // Create and configure a client
    val client = SimpleTelegramClient(
        TDLibSettings.create(APIToken.example()).apply {
            databaseDirectoryPath = Path(RESOURCES_PATH).resolve("db")
        }
    )

    // On the first run will establish console dialog were asks for phone number and sends code
    client.start(AuthenticationData.consoleLogin())

    // Print out Authorization stages
    client.addUpdateHandler(TdApi.UpdateAuthorizationState::class.java, ::onUpdateAuthorizationState)

    // Print out its nickname when successfully logged in
    client.addUpdatesHandler(onGetMe(client))

    // Print out a message received from private chat
    client.addUpdateHandler(TdApi.UpdateNewMessage::class.java) {update ->
        onUpdateNewMessage(update, client)
    }

    // Sort of a sink. Will handle all Update Types received from Telegram server
    client.addUpdatesHandler { onCommonUpdates(client) }

    // One way of passing Command Handler. Will trigger on /stop message
    client.addCommandHandler<TdApi.Update>("stop", stopCommandHandler(client))

    // Another way of passing Handler
    client.addCommandHandler<TdApi.Update>("finish") {chat, commandSender, arguments ->
        finishCommandHandler(chat, commandSender, arguments, client)
    }

    client.waitForExit()
}
