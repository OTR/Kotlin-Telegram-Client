package com.github.otr.console_client.domain.entity.chat

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * A class represents a Chat message.
 *
 * @param id Message identifier; unique for the chat to which the message belongs.
 * @param senderId Identifier of the sender of the message.
 * @param chatId Chat identifier. The same as the senderId?
 * @param date Point in time (Unix timestamp) when the message was sent.
 * @param content Contains the content of a message (abstract base class).
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Message
 */
data class Message(
    val id: Long,
    val senderId: MessageSender,
    val chatId: Long,
    val date: Int,
    val content: MessageContent,
) {
    companion object {
        val datetimeFormatter: DateTimeFormatter = DateTimeFormatter
            .ofPattern("dd.MM.yy HH:mm:ss")
        val zoneOffset: ZoneOffset = OffsetDateTime.now().offset
        val nanoOfSecond: Int = 0
    }
}

/**
 * An extension function for translating `data` of type Int into human-readable format
 */
fun Message.formattedDate(): String {
    val dateTime: LocalDateTime = LocalDateTime.ofEpochSecond(
        date.toLong(),
        Message.nanoOfSecond,
        Message.zoneOffset
    )
    return dateTime.format(Message.datetimeFormatter)
}

/**
 * If MessageContent is an instance of MessageText and has `text` field
 */
fun Message.asPlainText(): String {
    val content: MessageContent = this.content
    return if (content is MessageText) {
        return content.formattedText.text
    } else {
        "No text content?"
    }
}
