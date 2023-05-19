package com.github.otr.console_client.domain.entity.chat

/**
 * Identifier of the sender of the message. Base abstract class that have two subclasses
 * representing kind of the Sender: TdApi.MessageSenderUser and TdApi.MessageSenderChat
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Message-Sender
 */
sealed class MessageSender {

    /**
     * Means the message was sent by a known user.
     * @param userId Identifier of the user that sent the message.
     */
    data class User(
        val userId: Long
    ) : MessageSender()

    /**
     * Means the message was sent on behalf of a chat.
     * @param chatId Identifier of the chat that sent the message.
     */
    data class Chat(
        val chatId: Long
    ) : MessageSender()

}
