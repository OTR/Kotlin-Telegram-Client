package com.github.otr.console_client.domain.entity.chat

/**
 * Represents a user.
 *
 * @param id User identifier.
 * @param firstName First name of the user.
 * @param nickName The first value of the `activeUsernames` array if exists (nullable)
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-User
 */
data class User (
    val id: Long,
    val type: UserType,
    val firstName: String,
    val lastName: String,
    val nickName: String?,
    val phoneNumber: String
    // TODO: profilePhoto
)
