package com.github.otr.console_client.handler.chat

import it.tdlight.client.GenericResultHandler
import it.tdlight.jni.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A Helper class that holds a named logger instance
 * and other common methods for all ResultHandlers
 *
 * @see <a href="https://kotlinlang.org/docs/generics.html">
 *     Kotlin's documentation on Generics
 *     </a>
 *
 * @see it.tdlight.client.GenericResultHandler
 *
 */
abstract class ResultHandlerBase<T : TdApi.Object> constructor(
    protected val loggerName: String
    ) : GenericResultHandler<T> {

    protected val logger: Logger = LoggerFactory.getLogger(loggerName)

}
