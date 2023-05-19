package com.github.otr.console_client.domain.entity.chat

/**
 * This class is an abstract base class. Represents the type of a user.
 * The following types are possible: regular users, deleted users and bots.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-User-Type
 */
enum class UserType {
    BOT,
    DELETED,
    REGULAR,
    UNKNOWN
}
