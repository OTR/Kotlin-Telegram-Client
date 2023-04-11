package com.github.otr.simple_client.handler.message

import com.github.otr.simple_client.helper.AskLoadingUnreadMessages
import it.tdlight.client.Result
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

/**
 *
 */
class ChatResultHandler(
    private val client: SimpleTelegramClient
) : GenericUpdateHandler<TdApi.UpdateNewChat> {

    companion object {

        private const val DONT_USE_OFFSET: Int = 0
        private const val NOT_FROM_LOCAL: Boolean = false
    }

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateNewChat) {
        val chat: TdApi.Chat = update.chat
        val chatId: Long = chat.id
        val lastMessageId: Long = chat.lastMessage.id
        val lastReadInboxId: Long = chat.lastReadInboxMessageId
        val lastReadOutboxId: Long = chat.lastReadOutboxMessageId
        println("lastMsg: $lastMessageId, lastReadIn: $lastReadInboxId, lastReadOut: $lastReadOutboxId")
        val chatType: TdApi.ChatType = chat.type
        val chatTypeName: String = chatType.javaClass.simpleName.removePrefix("ChatType")
        val chatTitle: String = chat.title
        val unreadCount: Int = chat.unreadCount

        if (unreadCount > 0) {
            val msgAboutUnreadMessages: String = "There is a $chatTypeName chat with $chatTitle." +
                " You have $unreadCount unread messages from them."

            // Call our blocking interactor (user input scanner) which will ask a user
            // about loading new messages and pass user response to a trailing lambda function
//            AskLoadingUnreadMessages(msgAboutUnreadMessages).passUserInputTo { userResponse ->
//                if (userResponse == "yes") {
                    // If user responded with `yes` call TdApi.Function
                    // that loads messages from a Chat with the given chatId
                    // https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.GetChatHistory.html
                    client.send(
                        TdApi.GetChatHistory(
                            chatId, // chatId
                            lastMessageId, // fromMessageId
                            DONT_USE_OFFSET, // offset
                            unreadCount, // limit
                            NOT_FROM_LOCAL // onlyLocal
                        )
                    ) {result: Result<TdApi.Messages> ->
                        val _messages: TdApi.Messages = result.get()
                        println(_messages.totalCount)
                        val messages: Array<TdApi.Message> = _messages.messages
                        messages.forEach {message ->
                            val content = message.content
                            println(message.id)
                            if (content is TdApi.MessageText) {
                                println(content.text.text)
                            }
                        }
                    }
//                }
//            }
        }
    }

}
