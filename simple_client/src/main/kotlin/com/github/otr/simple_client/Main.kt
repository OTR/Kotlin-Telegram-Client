package com.github.otr.simple_client

import com.github.otr.simple_client.handler.message.ChatResultHandler
import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.jni.TdApi

import kotlin.io.path.Path

// The directory where TD Lib settings and cache located
private const val RESOURCES_PATH: String = "simple_client/src/main/resources/tdlib/"


fun main() {

    // Initialize TDLight native libraries
    Init.start()

    // Create and configure a client
    val client = SimpleTelegramClient(
        TDLibSettings.create(APIToken.example()).apply {
            databaseDirectoryPath = Path(RESOURCES_PATH).resolve("db")
            systemLanguageCode = "English"
        }
    )

    // On the first run will establish console dialog were asks for phone number and sends code
    client.start(AuthenticationData.consoleLogin())

    // Call a function that blocks main thread and asks a user to type their phone number
    // Then prints out that user input and does nothing
    // This line is needed for research on clientInteraction and blockingExecutor
//    client.getAuthenticationData {
//        println(it.userPhoneNumber)
//    }

    // Add a Handler that will be processing Unread Messages
    client.addUpdateHandler(
        TdApi.UpdateNewChat::class.java, // Specify Update Type we want to act on
        ChatResultHandler(client) // A Result Handler with a reference to the client
    )

    // Wait until TDLight is closed
    client.waitForExit()
}
