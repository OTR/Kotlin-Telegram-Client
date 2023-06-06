package com.github.otr.console_client.data.model

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.ChatType
import com.github.otr.console_client.domain.entity.chat.FormattedText
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.MessageSender
import com.github.otr.console_client.domain.entity.chat.MessageText
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.entity.chat.User
import com.github.otr.console_client.domain.entity.chat.UserType

/**
 *
 */
internal class TestDataSource {

    /**
     *
     */
    companion object {

        private const val TELEGRAM_CHAT_ID: Long = 777000
        private const val TELEGRAM_CHAT_TITLE: String = "Telegram"
        private const val TELEGRAM_USER_FIRST_NAME: String = "Telegram"
        private const val TELEGRAM_USER_PHONE_NUMBER: String = "42777"

        private const val JOHN_CHAT_ID: Long = 2000000000
        private const val JOHN_CHAT_TITLE: String = "John"
        private const val JOHN_USER_FIRST_NAME: String = "John"
        private const val JOHN_USER_NICK_NAME: String = "JohnDeer"
        private const val JOHN_USER_PHONE_NUMBER: String = "9996624242"

        // Kotlin it's me
        private const val KOTLIN_USER_ID: Long = 6000000000
        private const val KOTLIN_USER_FIRST_NAME: String = "Kotlin"
        private const val KOTLIN_USER_NICK_NAME: String = "kotlin_lang"
        private const val KOTLIN_USER_PHONE_NUMBER: String = "9996611111"

        private const val SPAM_BOT_CHAT_ID: Long = 178220800
        private const val SPAM_BOT_CHAT_TITLE: String = "Spam Info Bot"
        private const val SPAM_BOT_USER_FIRST_NAME: String = "Spam Info Bot"
        private const val SPAM_BOT_USER_NICK_NAME: String = "SpamBot"

        private const val LAST_MESSAGE_FROM_JOHN_ID: Long = 121000000
        private const val LAST_MESSAGE_FROM_JOHN_DATE: Int = 1684744571
        private const val LAST_MESSAGE_FROM_JOHN_TEXT: String = "last"

        private const val NEW_MESSAGE_FROM_JOHN_ID: Long = 122000000
        private const val NEW_MESSAGE_FROM_JOHN_DATE: Int = 1684744685
        private const val NEW_MESSAGE_FROM_JOHN_TEXT: String = "get a new one"

        private const val LAST_MESSAGE_FROM_TELEGRAM_ID: Long = 119000000
        private const val LAST_MESSAGE_FROM_TELEGRAM_DATE: Int = 1684477276
        private const val LAST_MESSAGE_FROM_TELEGRAM_TEXT: String = "Code for Telegram: 11111"

        private const val LAST_MESSAGE_FROM_SPAM_BOT_ID: Long = 19900000
        private const val LAST_MESSAGE_FROM_SPAM_BOT_DATE: Int = 1679818259
        private const val LAST_MESSAGE_FROM_SPAM_BOT_TEXT: String = "Thanks. Your report has sent"

        private val NO_LAST_MESSAGE: Message? = null
        private const val NO_UNREAD_MESSAGES: Int = 0
        private val NO_MESSAGES: List<Message>? = null
        private const val NO_LAST_NAME: String = ""
        private const val NO_PHONE_NUMBER: String = ""
        private val NO_NICK_NAME: String? = null
        private val NO_USER_DEFINED: User? = null
    }

    /**
     *
     */
    private val chatWithTelegram: Chat = Chat(
        id = TELEGRAM_CHAT_ID,
        user = NO_USER_DEFINED,
        type = ChatType.Private(userId = TELEGRAM_CHAT_ID),
        title = TELEGRAM_CHAT_TITLE,
        lastMessage = NO_LAST_MESSAGE,
        unreadCount = NO_UNREAD_MESSAGES,
        messages = NO_MESSAGES
    )

    /**
     *
     */
    private val chatWithJohn: Chat = Chat(
        id = JOHN_CHAT_ID,
        user = NO_USER_DEFINED,
        type = ChatType.Private(userId = JOHN_CHAT_ID),
        title = JOHN_CHAT_TITLE,
        lastMessage = NO_LAST_MESSAGE,
        unreadCount = NO_UNREAD_MESSAGES,
        messages = NO_MESSAGES
    )

    /**
     *
     */
    private val chatWithSpamBot: Chat = Chat(
        id = SPAM_BOT_CHAT_ID,
        user = NO_USER_DEFINED,
        type = ChatType.Private(userId = SPAM_BOT_CHAT_ID),
        title = SPAM_BOT_CHAT_TITLE,
        lastMessage = NO_LAST_MESSAGE,
        unreadCount = NO_UNREAD_MESSAGES,
        messages = NO_MESSAGES
    )

    /**
     *
     */
    private val testChats: List<Chat> = listOf(
        chatWithJohn, chatWithTelegram, chatWithSpamBot
    )

    /**
     * User Kotlin it's me
     */
    private val userKotlin: User = User(
        id = KOTLIN_USER_ID,
        type = UserType.REGULAR,
        firstName = KOTLIN_USER_FIRST_NAME,
        lastName = NO_LAST_NAME,
        nickName = KOTLIN_USER_NICK_NAME,
        phoneNumber = KOTLIN_USER_PHONE_NUMBER
    )

    /**
     *
     */
    private val userTelegram: User = User(
        id = TELEGRAM_CHAT_ID,
        type = UserType.REGULAR,
        firstName = TELEGRAM_USER_FIRST_NAME,
        lastName = NO_LAST_NAME,
        nickName = NO_NICK_NAME,
        phoneNumber = TELEGRAM_USER_PHONE_NUMBER
    )

    /**
     *
     */
    private val userJohn: User = User(
        id = JOHN_CHAT_ID,
        type = UserType.REGULAR,
        firstName = JOHN_USER_FIRST_NAME,
        lastName = NO_LAST_NAME,
        nickName = JOHN_USER_NICK_NAME,
        phoneNumber = JOHN_USER_PHONE_NUMBER
    )

    /**
     *
     */
    private val userSpamBot: User = User(
        id = SPAM_BOT_CHAT_ID,
        type = UserType.BOT,
        firstName = SPAM_BOT_USER_FIRST_NAME,
        lastName = NO_LAST_NAME,
        nickName = SPAM_BOT_USER_NICK_NAME,
        phoneNumber = NO_PHONE_NUMBER
    )

    /**
     *
     */
    private val testUsers: List<User> = listOf(
        userKotlin, userTelegram, userJohn, userSpamBot
    )

    /**
     *
     */
    private val blankChatWithUserJohn: Map<Long, Chat> = mapOf(
        JOHN_CHAT_ID to chatWithJohn.copy(
            user = userJohn.copy()
        )
    )

    /**
     *
     */
    private val lastMessageFromJohn: Message = Message(
        id = LAST_MESSAGE_FROM_JOHN_ID,
        senderId = MessageSender.User(userId = JOHN_CHAT_ID),
        chatId = JOHN_CHAT_ID,
        date = LAST_MESSAGE_FROM_JOHN_DATE,
        content = MessageText(formattedText = FormattedText(text = LAST_MESSAGE_FROM_JOHN_TEXT))
    )

    /**
     *
     */
    private val lastMessageFromTelegram: Message = Message(
        id = LAST_MESSAGE_FROM_TELEGRAM_ID,
        senderId = MessageSender.User(userId = TELEGRAM_CHAT_ID),
        chatId = TELEGRAM_CHAT_ID,
        date = LAST_MESSAGE_FROM_TELEGRAM_DATE,
        content = MessageText(formattedText = FormattedText(text = LAST_MESSAGE_FROM_TELEGRAM_TEXT))
    )

    /**
     *
     */
    private val lastMessageFromSpamBot: Message = Message(
        id = LAST_MESSAGE_FROM_SPAM_BOT_ID,
        senderId = MessageSender.User(userId = SPAM_BOT_CHAT_ID),
        chatId = SPAM_BOT_CHAT_ID,
        date = LAST_MESSAGE_FROM_SPAM_BOT_DATE,
        content = MessageText(formattedText = FormattedText(text = LAST_MESSAGE_FROM_SPAM_BOT_TEXT))
    )

    /**
     *
     */
    private val newMessageFromJohn: Message = Message(
        id = NEW_MESSAGE_FROM_JOHN_ID,
        senderId = MessageSender.User(userId = JOHN_CHAT_ID),
        chatId = JOHN_CHAT_ID,
        date = NEW_MESSAGE_FROM_JOHN_DATE,
        content = MessageText(formattedText = FormattedText(text = NEW_MESSAGE_FROM_JOHN_TEXT))
    )

    /**
     *
     */
    private val testMessages: List<Message> = listOf(
        lastMessageFromJohn,
        lastMessageFromTelegram,
        lastMessageFromSpamBot,
        newMessageFromJohn
    )

    /**
     * TODO: Move common code into a separate file
     */
    private val expectedRoster: Roster = BlankRosterImpl().apply {
        for (chat in getTestChats()) {
            // TODO: BE AWARE of changing constant object
            //  Otherwise constants from companion object will be changed
            // val chat: Chat = _chat.copy()
            val currChatId: Long = chat.id
            chat.user = getTestUsers().find { it.id == currChatId }
            // We need last message, so suppose last message has greater date
            chat.lastMessage = getTestMessages().filter { it.chatId == currChatId }.maxBy { it.date }
            chat.messages = getTestMessages().filter { it.chatId == currChatId }

            chatListMain.put(currChatId, chat)
        }
    }

    fun getUserKotlin(): User = userKotlin.copy()

    fun getUserTelegram(): User = userTelegram.copy()

    fun getUserSpamBot(): User = userSpamBot.copy()

    fun getUserJohn(): User = userJohn.copy()

    fun getChatTelegram(): Chat = chatWithTelegram.copy()

    fun getChatSpamBot(): Chat = chatWithSpamBot.copy()

    fun getChatJohn(): Chat = chatWithJohn.copy()

    fun getLastMessageFromTelegram(): Message = lastMessageFromTelegram.copy()

    fun getLastMessageFromJohn(): Message = lastMessageFromJohn.copy()

    fun getLastMessageFromSpamBot(): Message = lastMessageFromSpamBot.copy()

    fun getNewMessageFromJohn(): Message = newMessageFromJohn.copy()

    /**
     *
     */
    fun getTestChats(): List<Chat> {
        return testChats.map { it.copy() }
    }

    /**
     *
     */
    fun getTestUsers(): List<User> {
        return testUsers.map { it.copy() }
    }

    /**
     *
     */
    fun getBlankChatWithUserJohn(): Map<Long, Chat> {
        return blankChatWithUserJohn.map { it.key to it.value.copy() }.toMap()
    }

    /**
     *
     */
    fun getTestMessages(): List<Message> {
        return testMessages.map { it.copy() }
    }

    /**
     *
     */
    fun getExpectedRoster(): Roster {
        return expectedRoster.copy()
    }

    /**
     *
     */
    fun getExpectedRosterWithNoMessages(): Roster {
        return expectedRoster.copy().apply {
            chatListMain.replaceAll { _, chatEntity ->
                chatEntity.copy(messages = null, lastMessage = null)
            }
        }
    }
}
