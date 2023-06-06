package com.github.otr.console_client.domain.entity.chat

/**
 * This class is an abstract base class. Describes the type of a chat.
 *
 * @see
 * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Chat-Type
 */
sealed class ChatType {

    /**
     * A class representing a basic group
     * @param basicGroupId : Long - Basic group identifier.
     */
    data class BasicGroup(
        private val basicGroupId: Long
    ) : ChatType()

    /**
     * An ordinary chat with a user.
     * @param userId User identifier
     */
    data class Private(
        private val userId: Long
    ) : ChatType()

    /**
     * A secret chat with a user.
     * @param secretChatId Secret chat identifier.
     * @param userId User identifier of the secret chat peer.
     */
    data class Secret(
        private val secretChatId: Int,
        private val userId: Long
    ) : ChatType()

    /**
     * A supergroup (i.e. a chat with up to GetOption("supergroup_max_size") other users),
     * or channel (with unlimited members).
     * @param isChannel True, if the supergroup is a channel.
     * @param supergroupId Supergroup or channel identifier.
     */
    data class Supergroup(
        private val isChannel : Boolean,
        private  val supergroupId : Long
    ) : ChatType()

}
