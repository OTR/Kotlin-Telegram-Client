package com.github.otr.console_client.data.network.handler

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.AuthState
import it.tdlight.client.TelegramError
import it.tdlight.common.ExceptionHandler

class DefaultExceptionHandler(
    private val consoleCLI: ConsoleClient
) : ExceptionHandler {

    override fun onException(e: Throwable?) {
        if (e is TelegramError) {
            val errorCode: Int = e.errorCode
            val errorMessage: String = e.errorMessage
            when(errorMessage) {
                "PHONE_CODE_INVALID" -> {
                    // FIXME: Monkey Patching
                    val error = AuthState.ERROR
                    error.message = "PHONE_CODE_INVALID"
                    consoleCLI.emitState(error)
                }
            }
        }
    }

}

