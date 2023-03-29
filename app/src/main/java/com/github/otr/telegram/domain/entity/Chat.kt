package com.github.otr.telegram.domain.entity

/**
 * An Entity that represents a chat with a certain person or a group
 */
data class Chat(
    val id: Long,
    val name: String,
    val favorite: Boolean,
)
