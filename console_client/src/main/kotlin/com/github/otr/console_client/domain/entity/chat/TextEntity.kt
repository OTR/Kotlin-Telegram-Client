package com.github.otr.console_client.domain.entity.chat

/**
 * Represents a part of the text that needs to be formatted in some unusual way.
 * Entities are contained in the text. Entities can be nested,
 * but must not mutually intersect with each other.
 * `Pre`, `Code` and `PreCode` entities can't contain other entities.
 * `Bold`, `Italic`, `Underline` and `Strikethrough` entities can contain
 * and to be contained in all other entities. All other entities can't contain each other.
 *
 * @param length Length of the entity, in UTF-16 code units.
 * @param offset Offset of the entity in UTF-16 code units.
 * @param type Type of the entity.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Text-Entity
 */
data class TextEntity(
    private val length: Int,
    private val offset: Int,
    private val type: TextEntityType
)
