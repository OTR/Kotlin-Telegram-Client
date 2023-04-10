package com.github.otr.simple_client.helper

import com.github.otr.simple_client.MySimpleTelegramClient

import it.tdlight.common.utils.ScannerUtils

import org.slf4j.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.function.Consumer

/**
 * Whenever you want to interrupt a Main Thread and ask a user for some input
 * you should call a User Input Handler that extends this abstract class
 *
 * @param parameter an argument you want to define depend on user input
 * @param question additional information you want to provide when asking a user for an input
 */
abstract class UserInputScanner(
    private val parameter: String,
    private val question: String
) {

    // Request and assign a reference to SimpleTelegramClient's Logger
    private val logger: Logger = MySimpleTelegramClient.LOG

    //
    private val blockingExecutor: ExecutorService = MySimpleTelegramClient.blockingExecutor

    /**
     *
     * @param resultConsumer a function that will be called and user input will be passed
     */
    fun passUserInputTo(resultConsumer: Consumer<String>) {
        try {
            blockingExecutor.execute {
                val userInput: String = ScannerUtils.askParameter(parameter, question)
                resultConsumer.accept(userInput.trim { it <= ' ' })
            }
        } catch (ex: RejectedExecutionException) {
            logger.error("Failed to execute onParameterRequest. Returning an empty string", ex)
            resultConsumer.accept("")
        } catch (ex: NullPointerException) {
            logger.error("Failed to execute onParameterRequest. Returning an empty string", ex)
            resultConsumer.accept("")
        }
    }

}
