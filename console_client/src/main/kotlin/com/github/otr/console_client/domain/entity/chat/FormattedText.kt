package com.github.otr.console_client.domain.entity.chat

/**
 * Represents a formatted text. It can contain plain and formatted text.
 * @param text : String - a plain text without formatting.
 * @param entities : Array<TdApi.TextEntity> - a list of special entities that appear in the text.
 * For example, usernames, URLs, bot commands, etc.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Formatted-Text
 */
data class FormattedText(
    val text: String,
    //  val entities: List<TextEntity> // TODO: Uncomment on production version
)
