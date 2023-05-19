package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.login_handler.MyConsoleLogin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * Just for testing purposes
 */
suspend fun main(args: Array<String>) {
    var useTestDc: Boolean = false
    // Parse arguments
    if (args.isNotEmpty() && args.contains("--test")) {
        useTestDc = true
    }

    // Create an instance of a Console Client and set it up
    val consoleCLI: ConsoleClient = ConsoleClient(useTestDc = useTestDc)
    consoleCLI.addHandlers(HandlerType.COMMON)

    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    val waitForExitJob: Job = scope.launch {
        if (useTestDc) {
            consoleCLI.main(
                customAuthMethod = MyConsoleLogin(),
                useCustomClientInteraction = ConsoleLogin.TEST_CLIENT_INTERACTION
            )
        } else {
            consoleCLI.main()
        }

    }

    scope.launch {
        consoleCLI.stateFlow.collect {
            println(it)
        }
    }

    waitForExitJob.join()
}
