package com.github.otr.console_client.data.model

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.ChatType
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.entity.chat.User

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Hierarchy of domain layer entities is the following:
 *  1. You have 3 Types of Chat Lists (one Main List, one Archive List, zero or several Folders)
 *  2. Each Chat List contains Chat entities
 *  3. If it is a Private Chat with a Regular User, then ChatId is a primary key
 *      and equals to the UserId you have a chat with
 *  4. A User entity contains detail information on a specific user, their id (primary key),
 *      their first and last names, nicknames, phone number
 *  5. Each Chat entity has a field called `lastMessage` which is null or a Long number
 *      referring to a MessageId on the Telegram network
 */
data class BlankRosterImpl(
    override val chatListMain: MutableMap<Long, Chat> = mutableMapOf(),
    override val chatListArchive: MutableMap<Long, Chat> = mutableMapOf()
) : Roster() {

    //  private val myUserId: Long = -1 // TODO: Not implemented yet
    private val myNickname: String = "Kotlin"

    /**
     * 1. Check if there is a `Chat` entity with a specific chat id which is a primary key
     *      in `chatListMain` list (TODO: Check other lists too)
     * 2. If there is none then put the given argument into that list
     * 3. If there is one then check their fields, if equals - do nothing
     * 4. If fields differ then update fields of the previous Entity with fields from the new one
     * But keep the `user` fields, because it is custom, and not send by Telegram
     */
    override fun addOrUpdateChat(newChatEntity: Chat) {
        val chatId: Long = newChatEntity.id
        if (checkIfThereIsChatWithId(chatId)) {
            val prevChatEntity: Chat = getChatById(chatId)
            if (checkIfChatEntitiesAreEqual(prevChatEntity, newChatEntity)) {
                // Do nothing
            } else {
                val updatedChat: Chat = updateChatEntityFields(prevChatEntity, newChatEntity)
                chatListMain.put(chatId, updatedChat)
            }
        } else {
            chatListMain.put(chatId, newChatEntity)
        }
    }

    /**
     * Use User.id field as a primary key; check if a Map entity with such key exist
     * If not - put the given `user` into the Map,
     * otherwise TODO: compare two entities field by field and update an existing entity if needed
     */
    override fun addOrUpdateUser(newUserEntity: User) {
        val userId: Long = newUserEntity.id // TODO: Check if user.id is always equals chat.id
        // If such Chat exists
        if (checkIfThereIsChatWithId(userId)) {
            // Get that Chat entity
            val chatEntity: Chat = getChatById(userId)
            // If its field `user` is null
            val prevUser: User? = chatEntity.user
            if (prevUser == null) {
                // Create a copy of that Chat but with `user` field filled up
                val updatedChatEntity: Chat = chatEntity.copy(user = newUserEntity)
                // Put some
                addOrUpdateChat(updatedChatEntity)
            // Compare fields
            } else {
                // If they are the same do nothing
                if (checkIfUserEntitiesAreEqual(prevUser, newUserEntity)) {
                    // Do nothing
                // Otherwise replace previous user with new User
                } else {
                    val updatedChatEntity: Chat = chatEntity.copy(user = newUserEntity)
                    // Put some
                    addOrUpdateChat(updatedChatEntity)
                }
            }
        // Otherwise create a blank Chat Entity for that new User
        } else {
            // Check if `newUserEntity` it's me
            if (isMe(newUserEntity)) return
            val newBlankChatEntity: Chat = createNewBlankChat(newUserEntity)
            addOrUpdateChat(newBlankChatEntity)
        }
    }

    /**
     * TODO: Split into two functions the first that process `Last Message` event
     *  and the second that process `New Message` event
     */
    override fun addOrUpdateMessage(newMessageEntity: Message) {
        val chatId: Long = newMessageEntity.chatId
        if (checkIfThereIsChatWithId(chatId)) {
            val chatEntity: Chat = getChatById(chatId)
            chatEntity.lastMessage = newMessageEntity
            val prevMessages: MutableList<Message> = chatEntity.messages?.toMutableList() ?: mutableListOf()
            val logger: Logger = LoggerFactory.getLogger("TstBlankRoster")
            logger.debug("called once") // TODO: DELETE ME, for debugging purpose only
            // Check if there is already a message with such ID in the List
            if (checkIfThereIsMessageWithIdInList(prevMessages, newMessageEntity)) {
                // If there is some - do nothing
            } else {
                // otherwise add the new message to the list
                val updatedMessages: List<Message> = buildList {
                    addAll(prevMessages)
                    add(newMessageEntity)
                }
                val updatedChatEntity: Chat = chatEntity.copy(
                    lastMessage = newMessageEntity,
                    messages = updatedMessages
                )
                addOrUpdateChat(updatedChatEntity)
            }
        } else {
            TODO("Handling that case not implemented yet")
        }
    }

    /**
     * Check if there is a `Chat` entity with a specific chat id which is a primary key
     * in `chatListMain` list (TODO: Check other lists too)
     * If there is none return `false` otherwise return `true`
     */
    private fun checkIfThereIsChatWithId(chatId: Long): Boolean {
        return chatListMain.containsKey(chatId)
    }

    /**
     * As long as `Chat` entity is an instance of data class
     * `equals` method should do the job well, but in the future things could change
     * that's why it is a separate method for checking equality
     */
    private fun checkIfChatEntitiesAreEqual(prevChat: Chat, currChat: Chat): Boolean {
        return prevChat.equals(currChat)
    }

    /**
     * Get `Chat` entity with a specific chat id which is a primary key
     * from `chatListMain` list (TODO: Get from other lists too)
     */
    private fun getChatById(chatId: Long): Chat {
        return chatListMain.getValue(chatId)
    }

    /**
     * `chatListMain` list (TODO: Set to other lists too)
     */
    private fun updateChatEntityFields(prevChat: Chat, currChat: Chat): Chat {
        val chatId: Long = prevChat.id
        val userField: User? = prevChat.user
        val updatedChat: Chat = currChat.copy(user = userField)
        return updatedChat
    }

    /**
     * Create a Blank Chat entity specially for that new user
     * Add fill up as much fields of that Chat entity as far as User entity allows to reveal
     * We suppose they are always the same
     */
    private fun createNewBlankChat(newUserEntity: User): Chat {
        val userId: Long = newUserEntity.id
        val userFirstName: String = newUserEntity.firstName
        val newBlankChatEntity: Chat = Chat(
            id = userId,
            user = newUserEntity,
            type = ChatType.Private(userId = userId),
            title = userFirstName,
            lastMessage = null,
            messages = null,
            unreadCount = 0
        )
        return newBlankChatEntity
    }

    /**
     * `User` entity is an instance of data class, but check just to ensure
     */
    private fun checkIfUserEntitiesAreEqual(prevUser: User, currUser: User): Boolean {
        return prevUser.equals(currUser)
    }

    /**
     * The problem is that after successful login (after receiving Authorization State READY)
     * at first Telegram send us Update States with Users in our Roster (including ourself)
     * but we don't know who of them ourself,
     * after that Simple Telegram Loads forcefully loads All the Chats and then prints out
     * `[LoadChats     ] - All chats have been loaded`
     * And only after that SimpleTelegramClient sends `GetMe` Function and receives the reference
     * to ourself, after that step we for sure know who we are
     * But we don't actually need ourself in our Roster, TODO: Find out how to exclude ourself
     * So for know we just hardcode our nickname and exclude a User with such nickname from Roster
     */
    private fun isMe(newUserEntity: User): Boolean {
        return newUserEntity.firstName == myNickname
    }

    /**
     *
     */
    private fun checkIfThereIsMessageWithIdInList(
        listToSearch: List<Message>, givenMessage: Message
    ): Boolean {
        val foundMessage: Message? = listToSearch.find { it.id == givenMessage.id }
        return foundMessage != null
    }

}
