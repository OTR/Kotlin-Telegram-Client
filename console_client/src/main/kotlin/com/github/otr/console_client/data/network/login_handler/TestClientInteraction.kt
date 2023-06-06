package com.github.otr.console_client.data.network.login_handler

import it.tdlight.client.Authenticable
import it.tdlight.client.ClientInteraction
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo

import java.util.concurrent.ExecutorService
import java.util.function.Consumer

/**
 * This implementation is used in tests only
 * This will work out only when authenticating on Telegram Test Datacenters
 * This handler always returns the same verification code `11111`
 */
internal class TestClientInteraction(
    private val blockingExecutor: ExecutorService,
    private val authenticable: Authenticable
) : ClientInteraction {

    companion object {

        private const val TEST_VERIFICATION_CODE: String = "11111"
    }

    /**
     *
     */
    override fun onParameterRequest(
        parameter: InputParameter,
        parameterInfo: ParameterInfo,
        resultCons: Consumer<String>
    ) {
        authenticable.getAuthenticationData {
            blockingExecutor.execute {
                // If we are asked for verification code
                if (parameter == InputParameter.ASK_CODE) {
                    // Provide the test verification code to result Consumer
                    resultCons.accept(TEST_VERIFICATION_CODE)
                }
            }
        }
    }

}
