package com.github.otr.simple_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.TelegramError
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicReference

/**
 *
 */
internal class MyAuthorizationStateReadyGetMe(
    private val client: TelegramClient,
    private val mainChatsLoader: MyAuthorizationStateReadyLoadChats,
    private val archivedChatsLoader: MyAuthorizationStateReadyLoadChats
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     *
     */
    private val me = AtomicReference<TdApi.User>()

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateReady) {
            client.send(TdApi.GetMe(), { me: TdApi.Object ->
                if (me is TdApi.Error) {
                    val error = me
                    throw TelegramError(error)
                }
                this.me.set(me as TdApi.User)
                if (me.type is TdApi.UserTypeRegular) {
                    mainChatsLoader.onUpdate(update)
                    archivedChatsLoader.onUpdate(update)
                }
            }) { error: Throwable? -> logger.warn("Failed to execute TdApi.GetMe()") }
        }
    }

    /**
     *
     */
    fun getMe(): TdApi.User {
        return me.get()
    }

}
