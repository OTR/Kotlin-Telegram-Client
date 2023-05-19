package com.github.otr.console_client.domain.entity.chat

/**
 * An abstract base class represents a part of the text which must be formatted differently.
 * Represents entities contained in the text. Entities can be nested,
 * but must not mutually intersect with each other.
 * **Pre**, **Code** and **PreCode** entities can't contain other entities.
 * **Bold**, **Italic**, **Underline** and **Strikethrough** entities can contain
 * and to be contained in all other entities. All other entities can't contain each other.
 * Only **MentionName**, **PreCode** and **TextUrl** has public properties,
 * other subclasses are just empty classes.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Text-Entity-Type
 */
sealed class TextEntityType {
    //TODO: Maybe use enum class?

    /**
     * A text shows instead of a raw mention of the user (e.g., when the user has no username).
     * @param userId Identifier of the mentioned user.
     */
    data class MentionName(private val userId: Long): TextEntityType()

    /**
     * Text that must be formatted as if inside pre, and code HTML tags.
     * @param language Programming language of the code; as defined by the sender.
     */
    data class PreCode(private val language: String): TextEntityType()

    /**
     * A text description shown instead of a raw URL.
     * @param url HTTP or tg:// URL to be opened when the link is clicked.
     */
    data class TextUrl(private val url: String): TextEntityType()

    object Bold: TextEntityType()
    object BotCommand: TextEntityType()
    object Cashtag: TextEntityType()
    object Code: TextEntityType()
    object EmailAddress: TextEntityType()
    object Hashtag: TextEntityType()
    object Italic: TextEntityType()
    object Mention: TextEntityType()
    object PhoneNumber: TextEntityType()
    object Pre: TextEntityType()
    object Strikethrough: TextEntityType()
    object Underline: TextEntityType()
    object Url: TextEntityType()

}
