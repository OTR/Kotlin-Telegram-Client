package com.github.otr.console_client

import it.tdlight.client.APIToken
import it.tdlight.client.AuthenticationData
import it.tdlight.client.Result
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
    client.start(AuthenticationData.consoleLogin())

    // Print out its nickname when successfully logged in
    client.addUpdatesHandler { update ->
        if (update is TdApi.UpdateAuthorizationState){
            if (update.authorizationState is TdApi.AuthorizationStateReady) {
                client.send(TdApi.GetMe()) {res: Result<TdApi.User> ->
                    if (!res.isError) {
                        println("Hello, my name is: ${ res.get().firstName }")
                    }
                }
            }
        }
    }

    client.waitForExit()
}
