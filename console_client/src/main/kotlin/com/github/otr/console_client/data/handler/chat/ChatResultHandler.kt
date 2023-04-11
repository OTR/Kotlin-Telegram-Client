package com.github.otr.console_client.data.handler.chat

import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 * Handle incoming Updates about a Chat entity
 *
 * @see
 * <a href="https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Chat">
 *     Documentation on returned type `TdApi.Chat`
 * </a>
 */
object ChatResultHandler : ResultHandlerBase<TdApi.Chat>(
    loggerName = "ChatResultHand"
) {

    /**
     * @see
     * <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Chat.html">
     *      Documentation on returned type `TdApi.Chat`
     * </a>
     *
     */
    override fun onResult(result: Result<TdApi.Chat>) {
        val chat: TdApi.Chat = result.get()
        val userId: Long = chat.id
        val chatType: TdApi.ChatType = chat.type
        val chatTypeName: String = chatType.javaClass.simpleName.removePrefix("ChatType")
        val chatTitle: String = chat.title
        val unreadCount: Int = chat.unreadCount

        logger.debug(
            "There is a ${chatTypeName} chat with $chatTitle." +
            " You have $unreadCount unread messages from them."
        )
    }

}
