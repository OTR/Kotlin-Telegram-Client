package com.github.otr.console_client.data.mapper

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.MessageContent
import com.github.otr.console_client.domain.entity.chat.MessageText
import com.github.otr.console_client.domain.entity.chat.User

/**
 * String representation of a User Model
 */
fun User.asLogMsg(): String {
    val lineAboutNickname: String = if (nickName == null) {
        "without nickname"
    } else {
        "nickname: $nickName"
    }

    return  "In our chat list there is a user with first name: $firstName" +
            " and $lineAboutNickname and phone number: $phoneNumber"
}

/**
 *
 */
fun Chat.asLogMsg(): String {
    val chatTypeName: String = this.javaClass.simpleName.removePrefix("ChatType")
    return "There is a $chatTypeName chat with $title." +
            " You have $unreadCount unread messages from them."
}

/**
 *
 */
fun Message.asLogMsg(loggerName: String): String {
    val chatId: Long = this.chatId
    var plainText: String = when(val content: MessageContent = this.content) {
        is MessageText -> content.formattedText.text
        else -> "TODO: unknown content"
    }
    // Replace new line symbols for the same of readability
    plainText = plainText.replace("\n","; ")

    val messageId: Long = this.id
    return when(loggerName) {
        "LastMessageHan" -> {
            "The last message in a chat with id: $chatId has plain text: `$plainText`"
        }
        "NewMessageHand" -> {
            "Received a new plain text message with id $messageId: `$plainText`" +
                    " in a chat with id: $chatId"
        }
        else -> {
            "Received a plain text message: `$plainText` in a chat with id: $chatId"
        }
    }
}
