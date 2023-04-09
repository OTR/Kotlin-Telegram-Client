package com.github.otr.simple_client

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init

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
        }
    )

    // On the first run will establish console dialog were asks for phone number and sends code
    client.start(AuthenticationData.consoleLogin())

    // Wait until TDLight is closed
    client.waitForExit()
}
