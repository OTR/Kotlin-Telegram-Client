package com.github.otr.simple_client.helper

import com.github.otr.simple_client.MySimpleTelegramClient
import it.tdlight.client.ClientInteraction
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import org.slf4j.Logger

import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.function.Consumer

/**
 * Whenever you need to block Main Thread and ask for user input
 * This class is Called
 */
class MySimpleTelegramClientInteraction(
    private val blockingExecutor: ExecutorService,
    private val clientInteraction: ClientInteraction
) : ClientInteraction {

    private val logger: Logger = MySimpleTelegramClient.LOG

    /**
     *
     */
    override fun onParameterRequest(
        parameter: InputParameter,
        parameterInfo: ParameterInfo,
        result: Consumer<String>
    ) {
        try {
            blockingExecutor.execute {
                clientInteraction.onParameterRequest(parameter, parameterInfo, result)
            }
        } catch (ex: RejectedExecutionException) {
            logger.error("Failed to execute onParameterRequest. Returning an empty string", ex)
            result.accept("")
        } catch (ex: NullPointerException) {
            logger.error("Failed to execute onParameterRequest. Returning an empty string", ex)
            result.accept("")
        }
    }

}
