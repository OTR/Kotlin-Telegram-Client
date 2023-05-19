package com.github.otr.console_client.domain.entity.chat

/**
 * Contains the content of a message. Abstract base class that have the following subclasses:
 * TdApi.MessageText and many others
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Message-Content
 */
sealed class MessageContent

/**
 * Represents a text message.
 * @param formattedText : TdApi.FormattedText - A text of the message with some entities.
 * @param webPage : TdApi.WebPage - A preview of the web page that's mentioned in the text;
 * may be null.
 */
data class MessageText(
    val formattedText: FormattedText,
    // TODO: does webPage is really needed?
) : MessageContent()
