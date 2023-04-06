package com.github.otr.console_client.log_handler

import ch.qos.logback.classic.pattern.LoggerConverter
import ch.qos.logback.classic.pattern.MethodOfCallerConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.PropertyDefinerBase
import org.slf4j.LoggerFactory

/**
 * An extension function for String type that takes Map<String, String>
 * where key is a string to replace and value is a replacement string.
 * Then iterates over the given Map entries and calls replace method.
 *
 * @see <a href="https://stackoverflow.com/a/69998547">Solution</a>
 *
 */
private fun String.replaceByMap(mapping: Map<String, String>): String {
    var str = this
    mapping.forEach { (from, to) -> str = str.replace(from, to) }
    return str
}

/**
 * @see <a href="https://tersesystems.com/blog/2019/05/11/application-logging-in-java-part-3/#custom-converters">
 *     Example of extending ClassicConverter
 * </a>
 */
class ShortLoggerNameConverter : LoggerConverter() {

    private companion object {

        // Replacement rules
        const val TELEGRAM_CLIENT_FROM: String = "it.tdlight.common.TelegramClient"
        const val TELEGRAM_CLIENT_TO: String = "TelegramClient"

        const val TD_LIGHT_FROM: String = "it.tdlight.TDLight"
        const val TD_LIGHT_TO: String = "TDLight"

        const val LOAD_CHATS_FROM: String = "it.tdlight.client.AuthorizationStateReadyLoadChats"
        const val LOAD_CHATS_TO: String = "LoadChats"

        val replacementMap: HashMap<String, String> = hashMapOf(
            TELEGRAM_CLIENT_FROM to TELEGRAM_CLIENT_TO,
            TD_LIGHT_FROM to TD_LIGHT_TO,
            LOAD_CHATS_FROM to LOAD_CHATS_TO
        )
    }

    override fun convert(event: ILoggingEvent): String {
        val oldLoggerName: String = event.loggerName
        val newLoggerName: String = oldLoggerName.replaceByMap(replacementMap)

        return newLoggerName
    }

}

/**
 * Print out a horizontal line after Client shut down.
 *
 * A dirty hack instead of handling Result<TdApi.Ok> of TdApi.Close function call
 * because SimpleTelegramClient doesn't allow adding <TdApi.Object> handlers
 *
 * @see <a href="https://github.com/tdlight-team/tdlight-java/blob/master/src/main/java/it/tdlight/common/internal/InternalClient.java#L197">
 *     Source code of InternalClient.onJVMShutdown() method
 *     </a>
 */
class ShutDownMethodConverter: MethodOfCallerConverter() {

    private companion object {

        const val ON_JVM_SHUT_DOWN: String = "onJVMShutdown"
        const val EMPTY_STRING: String = ""
        const val LINE_SEPARATOR: String = "\n"
        const val LINE_LENGTH: Int = 100
        val HORIZONTAL_LINE: String = "_".repeat(LINE_LENGTH).plus(LINE_SEPARATOR)
    }

    override fun convert(event: ILoggingEvent): String {
        val methodName: String = super.convert(event)
        return if (methodName == ON_JVM_SHUT_DOWN) HORIZONTAL_LINE else EMPTY_STRING
    }
}

/**
 * An example of creating and using custom logback property on the fly
 *
 * <b>Sample Usage:</b>
 *
 * ```
 * <define name="customProperty" class="com.github.otr.console_client.log_handler.CustomPropertyDefinedOnTheFly" />
 * // ...
 * <pattern> ${customProperty} %n</pattern>
 * ```
 * @see <a href="https://logback.qos.ch/manual/configuration.html#definingPropsOnTheFly">
 *     Defining variables, aka properties, on the fly
 *     </a>
 */
class CustomPropertyDefinedOnTheFly : PropertyDefinerBase() {

    override fun getPropertyValue(): String {
        return "Hello!"
    }

}

/**
 * Just for testing purposes.
 */
private fun main() {
    val logger = LoggerFactory.getLogger("test.logger")
    logger.debug("it.tdlight.AuthorizationStateReadyLoadChats")
}
