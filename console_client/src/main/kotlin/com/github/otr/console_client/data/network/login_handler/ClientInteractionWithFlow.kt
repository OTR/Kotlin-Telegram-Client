package com.github.otr.console_client.data.network.login_handler

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.AuthState
import it.tdlight.client.Authenticable
import it.tdlight.client.ClientInteraction
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import it.tdlight.common.utils.ScannerUtils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import java.util.concurrent.ExecutorService
import java.util.function.Consumer

/**
 * @see it.tdlight.client.ScannerClientInteraction
 */
internal class ClientInteractionWithFlow (
    private val blockingExecutor: ExecutorService,
    private val authenticable: Authenticable,
    private val consoleCLI: ConsoleClient
) : ClientInteraction {

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

                // Process each branch depending on what we've been asked for
                when (parameter) {
                    InputParameter.ASK_CODE -> {
                        val result: String = collectUntilGotVerificationCode()
                        resultCons.accept(result)
                    }
                    InputParameter.ASK_FIRST_NAME -> { TODO("Firstname Not implemented yet")}
                    InputParameter.ASK_LAST_NAME -> { TODO("Lastname Not implemented yet") }
                    InputParameter.ASK_PASSWORD -> { TODO("Password Not implemented yet") }
                    InputParameter.NOTIFY_LINK -> { TODO("Notify link Not implemented yet") }
                    InputParameter.TERMS_OF_SERVICE -> { TODO("TOS Not implemented yet") }
                    else -> { TODO("Else branch is not implemented yet") }
                }
            }
        }
    }

    /**
     *
     */
    private fun collectUntilGotVerificationCode(): String {
        var verificationCode: String = ""
        runBlocking {
            launch {
                consoleCLI.stateFlow.collect {
                    if (it == AuthState.SEND_VERIFICATION_CODE) {
                        // TODO: Validate verification code
                        verificationCode = it.message
                        this.coroutineContext.cancel()
                    }
                }
            }
        }
        return verificationCode
    }

}
