package com.github.otr.console_client.domain.entity.chat

/**
 * A chat entity. (Can be a private chat, basic group, supergroup, or secret chat.)
 *
 * @param id Chat unique identifier.
 * @param type Type of the chat.
 * @param title Chat title.
 * @param lastMessage Last message in the chat; **may be null**.
 * @param unreadCount Number of unread messages in the chat.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Chat
 */
data class Chat(
    val id: Long,
    var user: User?, // FIXME: get rid of type, combine user and type filed with name of user
    val type: ChatType, // also contains userId
    val title: String,
    var lastMessage: Message?,
    var messages: List<Message>?, // FIXME: Replace with `Map` collection
    var unreadCount: Int
)
