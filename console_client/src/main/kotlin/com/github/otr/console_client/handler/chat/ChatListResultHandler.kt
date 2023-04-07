package com.github.otr.console_client.handler.chat

import it.tdlight.client.Result
import it.tdlight.jni.TdApi
import it.tdlight.jni.TdApi.ChatListFilter

/**
 * Takes undetermined abstract base class `TdApi.ChatList`
 * and handles it depend on concrete subclass type.
 *
 * @see
 * <a href="https://tdlibx.github.io/td/docs/org/drinkless/td/libcore/telegram/TdApi.ChatListArchive.html">
 *      Documentation on concrete class `TdApi.ChatListArchive`
 * </a>
 * @see
 * <a href="https://tdlibx.github.io/td/docs/org/drinkless/td/libcore/telegram/TdApi.ChatListMain.html">
 *      Documentation on concrete class `TdApi.ChatListMain`
 * </a>
 * @see it.tdlight.client.SimpleTelegramClient.archivedChatsLoader
 * @see it.tdlight.client.SimpleTelegramClient.mainChatsLoader
 *
 */
object ChatListResultHandler : ResultHandlerBase<TdApi.ChatList>(
    loggerName = "ChatListResult"
) {

    override fun onResult(result: Result<TdApi.ChatList>) {

        val chatList: TdApi.ChatList = result.get()
        when (chatList) {
            // A list of chats usually located at the top of the main chat list.
            // Unmuted chats are automatically moved from the Archive to the Main chat list
            // when a new message arrives.
            is TdApi.ChatListArchive -> {
                logger.trace("Received `TdApi.ChatListArchive`")
            }
            // A main list of chats.
            // Used in the getChats method of the TelegramClient to return the main chat list.
            is TdApi.ChatListMain -> {
                logger.trace("Received `TdApi.ChatListMain`")
            }
            // https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.ChatListFilter.html
            // This class represents a filter for chats to be used in chat list
            is TdApi.ChatListFilter -> {
                val chatFilterId: Int = chatList.chatFilterId
                logger.trace("Received `TdApi.ChatListFilter`")
            }
            else -> {
                logger.warn("Got unknown subclass of `TdApi.ChatList`")
            }
        }

    }

}
