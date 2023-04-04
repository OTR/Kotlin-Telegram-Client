package com.github.otr.console_client.handler

import it.tdlight.client.Result
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

/**
 * A Handler that will print out a message and Chat name from which that message was received
 *
 * <b>Sample Usage</b>
 * ```
 * client.addUpdateHandler(TdApi.UpdateNewMessage::class.java) {update ->
 *     onUpdateNewMessage(update, client)
 * }
 * ```
 * @param client a reference to itself, an instance of SimpleTelegramClient
 * @param update an update sent by Telegram and will be handled if types match
 */
fun onUpdateNewMessage(update: TdApi.UpdateNewMessage, client: SimpleTelegramClient) {
    val messageContent: TdApi.MessageContent = update.message.content
    val text: String = if (messageContent is TdApi.MessageText ) {
        messageContent.text.text
    } else {
        messageContent.javaClass.simpleName
    }

    client.send(TdApi.GetChat(update.message.chatId)) { chatIdResult: Result<TdApi.Chat> ->
        val chat = chatIdResult.get()
        val chatName = chat.title
        println("Received new message from chat $chatName: $text")
    }
}
