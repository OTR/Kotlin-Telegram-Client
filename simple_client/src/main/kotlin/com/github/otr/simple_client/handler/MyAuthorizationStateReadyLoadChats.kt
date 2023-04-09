package com.github.otr.simple_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.TelegramError
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

import org.slf4j.LoggerFactory

/**
 *
 */
internal class MyAuthorizationStateReadyLoadChats(
    private val client: TelegramClient,
    private val chatList: TdApi.ChatList
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    companion object {
        private val logger = LoggerFactory.getLogger("LoadChats")
    }

    /**
     *
     */
    private val isLoaded: Boolean? = null

    /**
     *
     */
    fun isLoaded(): Boolean = isLoaded ?: false

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateReady) {
            client.send(TdApi.LoadChats(chatList, 2000),
                { ok: TdApi.Object ->
                    // my addition
                    val chatListName = if (chatList is TdApi.ChatListMain) "Main" else "Archive"
                    //
                    if (ok is TdApi.Error) {
                        val error = ok
                        if (error.code != 404) {
                            throw TelegramError(error)
                        }
                        logger.debug("All $chatListName chats have already been loaded")
                    } else {
                        logger.debug("All $chatListName chats have been loaded")
                    }
                }
            ) { error: Throwable? -> logger.warn("Failed to execute TdApi.LoadChats()") }
        }
    }

}
