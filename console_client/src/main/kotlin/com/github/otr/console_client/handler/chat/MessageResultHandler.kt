package com.github.otr.console_client.handler.chat

import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Message.html">
 *     Documentation on returned type of `TdApi.Message`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageSender.html">
 *     Documentation on returned type of `TdApi.MessageSender`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageContent.html">
 *     Documentation on returned type of `TdApi.MessageContent`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageText.html">
 *     Documentation on returned type of `TdApi.MessageText`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.FormattedText.html">
 *     Documentation on returned type of `TdApi.FormattedText`
 *     </a>
 *
 */
class MessageResultHandler(
    loggerName: String
) : ResultHandlerBase<TdApi.Message>(loggerName) {

    override fun onResult(result: Result<TdApi.Message>) {
        val message: TdApi.Message = result.get()
        // Message identifier; unique for the chat to which the message belongs.
        val messageId: Long = message.id
        // Identifier of the sender of the message.
        val messageSender: TdApi.MessageSender = message.senderId
        val senderId: Long = when (messageSender) {
            is TdApi.MessageSenderUser -> {
                messageSender.userId
            }
            is TdApi.MessageSenderChat -> {
                messageSender.chatId
            }
            else -> {
                val typeName: String = messageSender.javaClass.simpleName
                val reason: String = "Unknown message sender type: $typeName"
                logger.warn(reason)
                throw IllegalStateException(reason)
            }
        }
        // Chat identifier. Same as senderId?
        val chatId: Long = message.chatId
        // message.canBeForwarder message.canBeSaved message.isChannelPost
        // Point in time (Unix timestamp) when the message was sent.
        val date: Int = message.date
        // This class is an abstract base class. Contains the content of a message.
        val content: TdApi.MessageContent = message.content
        when (content) {
            is TdApi.MessageText -> {
                // A preview of the web page that's mentioned in the text; may be null.
                // val webPage: TdApi.WebPage = content.webPage
                // A text with some entities.
                val formattedText: TdApi.FormattedText = content.text
                // Entities contained in the text.
                // Entities can be nested, but must not mutually intersect with each other.
                // Pre, Code and PreCode entities can't contain other entities.
                // Bold, Italic, Underline and Strikethrough entities can contain
                // and to be contained in all other entities.
                // All other entities can't contain each other.
                // val entities: Array<TdApi.TextEntity> = formattedText.entities
                val plainText: String = formattedText.text
                logger.debug("Received a plain text message: `$plainText` in chat with id: $chatId")
            }
            else -> {
                // TODO: https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageContent.html
                logger.warn("TODO: Handle all remaining subclasses of TdApi.MessageContent")
            }
        }

    }

}
