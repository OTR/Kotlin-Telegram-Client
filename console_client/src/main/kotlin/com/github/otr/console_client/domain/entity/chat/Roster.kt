package com.github.otr.console_client.domain.entity.chat


/**
 * https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.ChatList.html
 */
abstract class Roster: Cloneable {

    // A main list of chats.
    abstract val chatListMain: MutableMap<Long, Chat>

    // A list of chats usually located at the top of the main chat list.
    // Unmuted chats are automatically moved from the Archive to the Main chat list
    // when a new message arrives.
    abstract val chatListArchive: MutableMap<Long, Chat>

    // TODO: chatListFolder

    //
    abstract fun addOrUpdateUser(newUserEntity: User)

    //
    abstract fun addOrUpdateChat(newChatEntity: Chat)

    //
    abstract fun addOrUpdateMessage(newMessageEntity: Message)

    /**
     * Try to implement `copy` method by calling `clone` method from `Clonable` interface
     * https://www.baeldung.com/kotlin/deep-copy-data-class
     */
    fun copy(): Roster {
        return super.clone() as Roster
    }

}
