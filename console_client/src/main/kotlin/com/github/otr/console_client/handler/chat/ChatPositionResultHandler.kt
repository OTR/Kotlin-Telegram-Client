package com.github.otr.console_client.handler.chat

import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 *
 */
object ChatPositionResultHandler : ResultHandlerBase<TdApi.ChatPosition>(
    loggerName = "ChatPositionHa"
) {

    override fun onResult(result: Result<TdApi.ChatPosition>) {
        val chatPosition: TdApi.ChatPosition = result.get()
        val unknownSomething: TdApi.ChatList = chatPosition.list // Describes a Type of List. Pattern matching required
        val lineAboutChatListType: String = when (unknownSomething) {
            is TdApi.ChatListMain -> "ChatList type is `TdApi.ChatListMain`"
                is TdApi.ChatListArchive -> "ChatList type is `TdApi.ChatListFilter`"
                    is TdApi.ChatListFilter -> "ChatList type is `TdApi.ChatListFilter`"
                        else -> "Unknown ChatList type"
        }
        val isChatPinned: Boolean = chatPosition.isPinned
        val chatOrder: Long = chatPosition.order
        val chatSource: TdApi.ChatSource? = chatPosition.source
        when (chatSource) {
            null -> {
                logger.trace("ChatSource == null")
            }
            is TdApi.ChatSourceMtprotoProxy -> {
                // This is an empty class, no public properties
                logger.debug("Received from chat source: `TdApi.ChatSourceMtprotoProxy`")
            }
            is TdApi.ChatSourcePublicServiceAnnouncement -> {
                val chatSourceText: String = chatSource.text
                val chatSourceType: String = chatSource.type
                logger.debug(
                    "Received from chat source: `TdApi.ChatSourceMtprotoProxy`" +
                    " with text: $chatSourceText and type: $chatSourceType" +
                    " $lineAboutChatListType"
                )
            }
            else -> {
                val typeName: String = chatSource.javaClass.simpleName
                logger.warn("$typeName is unknown subclass of `TdApi.ChatSource`")
            }
        }
    }

}
