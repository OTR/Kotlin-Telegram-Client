package com.github.otr.console_client.data.handler

import it.tdlight.client.Result
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.UpdateNewMessage.html">
 *     Documentation on returned type TdApi.UpdateNewMessage
 *     </a>
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Chat.html">
 *     Documentation on Chat entity
 *     </a>
 *
 * @see <a href="https://github.com/tdlight-team/tdlight-java/blob/master/src/main/java/it/tdlight/common/TelegramClient.java#L45">
 *     Documentation on TelegramClient.send() method
 *     </a.
 *
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
        val logger: Logger = LoggerFactory.getLogger("NewMessageUpd")
        val causePrefix: String = "Received"
        val causeName: String = "TdApi.Chat"
        val message: String = "Received new message from chat $chatName: $text"
        logger.debug("$causePrefix $causeName, $message")
    }

}
