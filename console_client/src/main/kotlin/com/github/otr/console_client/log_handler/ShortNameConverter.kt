package com.github.otr.console_client.log_handler

import ch.qos.logback.classic.pattern.MessageConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.PropertyDefinerBase
import org.slf4j.LoggerFactory

/**
 * https://tersesystems.com/blog/2019/05/11/application-logging-in-java-part-3/
 */
class ShortNameConverter : MessageConverter() {

    override fun convert(event: ILoggingEvent): String {
        return event.formattedMessage.takeLast(10)
    }

}

/**
 * <b>Sample Usage</b>

 * ```
 * <define name="customProperty" class="com.github.otr.console_client.log_handler.CustomPropertyDefinedOnTheFly" />
 * // ...
 * <pattern> ${customProperty} %n</pattern>
 * ```
 */
class CustomPropertyDefinedOnTheFly: PropertyDefinerBase() {

    override fun getPropertyValue(): String {
        return "Hello!"
    }

}

private fun main() {
    val logger = LoggerFactory.getLogger("test.logger")
    logger.debug("it.tdlight.AuthorizationStateReadyLoadChats")
}
