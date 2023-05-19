package com.github.otr.console_client.data.network.login_handler

import it.tdlight.client.Authenticable
import it.tdlight.client.AuthenticationData
import it.tdlight.client.ClientInteraction
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo

import java.util.concurrent.ExecutorService
import java.util.function.Consumer

internal class TestClientInteraction(
    private val blockingExecutor: ExecutorService,
    private val authenticable: Authenticable
) : ClientInteraction {

    companion object {

        private const val VERIFICATION_CODE: String = "11111"
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

                // Biggest When Block you've ever seen
                if (parameter == InputParameter.ASK_CODE) {
                    // Provide the taken user input to result Consumer
                    resultCons.accept(VERIFICATION_CODE)
                }
            }
        }
    }

}
