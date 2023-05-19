package com.github.otr.console_client.data.mapper

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.ChatType
import com.github.otr.console_client.domain.entity.chat.FormattedText
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.MessageContent
import com.github.otr.console_client.domain.entity.chat.MessageSender
import com.github.otr.console_client.domain.entity.chat.MessageText
import com.github.otr.console_client.domain.entity.chat.TextEntity
import com.github.otr.console_client.domain.entity.chat.TextEntityType
import com.github.otr.console_client.domain.entity.chat.User
import com.github.otr.console_client.domain.entity.chat.UserType

import it.tdlight.jni.TdApi
import org.slf4j.LoggerFactory

/**
 *
 */
object RosterMapper {

    /**
     *
     */
    fun mapUserResultToModel(userResult: TdApi.User): User {
        val id: Long = userResult.id

        val typeResult: TdApi.UserType = userResult.type
        val type: UserType = mapUserTypeResultToModel(typeResult)

        val firstName: String = userResult.firstName
        val lastName: String = userResult.lastName

        val usernames: TdApi.Usernames? = userResult.usernames // Usernames of user; may be null.
        val activeUsernames: Array<String>? = usernames?.activeUsernames
        val nickName: String? = activeUsernames?.first()

        val phoneNumber: String = userResult.phoneNumber

        return User(
            id = id,
            type = type,
            firstName = firstName,
            lastName = lastName,
            nickName = nickName,
            phoneNumber = phoneNumber,
        )
    }

    /**
     * @see
     *
     */
    fun mapChatResultToModel(chatResult: TdApi.Chat): Chat {
        val userId: Long = chatResult.id
        val chatTypeResult: TdApi.ChatType = chatResult.type
        val chatType: ChatType = mapChatTypeResultToModel(chatTypeResult)
        val chatTitle: String = chatResult.title
        val unreadCount: Int = chatResult.unreadCount
        val lastMessageResult: TdApi.Message? = chatResult.lastMessage
        val lastMessage: Message? = lastMessageResult?.let {
            mapMessageResultToModel(it)
        }

        // TODO: Debug code; remove on production version
        if (lastMessage == null) LoggerFactory.getLogger("ChatResultHand").trace(
            "last message is null"
        )

        return Chat(
            id = userId,
            user = null,
            type = chatType,
            title = chatTitle,
            lastMessage = lastMessage,
            unreadCount = unreadCount,
            messages = null
        )
    }

    /**
     *
     * Unhandled fields are: message.canBeForwarder, message.canBeSaved, message.isChannelPost
     */
    fun mapMessageResultToModel(messageResult: TdApi.Message): Message {

        // Message identifier; unique for the chat to which the message belongs.
        val id: Long = messageResult.id

        // Identifier of the sender of the message.
        val messageSenderResult: TdApi.MessageSender = messageResult.senderId
        val senderId: MessageSender = mapMessageSenderResultToModel(messageSenderResult)

        // Chat identifier. TODO: Same as senderId?
        val chatId: Long = messageResult.chatId

        // Point in time (Unix timestamp) when the message was sent.
        val date: Int = messageResult.date

        // This class is an abstract base class. Contains the content of a message.
        val messageContentResult: TdApi.MessageContent = messageResult.content
        val messageContent: MessageContent = mapMessageContentResultToModel(messageContentResult)

        return Message(
            id = id,
            senderId = senderId,
            chatId = chatId,
            date = date,
            content = messageContent
        )
    }

    /**
     *
     */
    fun mapUserTypeResultToModel(userTypeResult: TdApi.UserType): UserType {
        return when(userTypeResult) {
            is TdApi.UserTypeBot -> UserType.BOT
            is TdApi.UserTypeDeleted -> UserType.DELETED
            is TdApi.UserTypeRegular -> UserType.REGULAR
            is TdApi.UserTypeUnknown -> UserType.UNKNOWN
            else -> { throw RuntimeException("Unknown TdApi.UserType")}
        }
    }

    /**
     *
     */
    fun mapChatTypeResultToModel(chatTypeResult: TdApi.ChatType): ChatType {
        // chatTypeResult.javaClass.simpleName.removePrefix("ChatType")
        return when(val it = chatTypeResult) {
            is TdApi.ChatTypeBasicGroup -> {
                ChatType.BasicGroup(basicGroupId = it.basicGroupId)
            }
            is TdApi.ChatTypePrivate -> {
                ChatType.Private(userId = it.userId)
            }
            is TdApi.ChatTypeSecret -> {
                ChatType.Secret(secretChatId = it.secretChatId, userId = it.userId)
            }
            is TdApi.ChatTypeSupergroup -> {
                ChatType.Supergroup(
                    isChannel = it.isChannel,
                    supergroupId = it.supergroupId
                )
            }
            else -> { throw RuntimeException("There is no such Chat Type") }
        }
    }

    /**
     * Normally there should be only two sub classes of MessageSender,
     * but throw an Exception just in case
     */
    private fun mapMessageSenderResultToModel(messageSender: TdApi.MessageSender): MessageSender {
        return when (messageSender) {
            is TdApi.MessageSenderUser -> {
                MessageSender.User(userId = messageSender.userId)
            }
            is TdApi.MessageSenderChat -> {
                MessageSender.Chat(chatId = messageSender.chatId)
            }
            else -> {
                val typeName: String = messageSender.javaClass.simpleName
                val reason: String = "Unknown message sender type: $typeName"
                throw IllegalStateException(reason)
            }
        }
    }

    /**
     *
     * @see
     * https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Message-Content
     * //
     */
    private fun mapMessageContentResultToModel(msgContent: TdApi.MessageContent): MessageContent {
        return when (msgContent) {
            is TdApi.MessageText -> {
                val messageText: MessageText = mapMessageTextResultToModel(msgContent)
                messageText
            }
            //
            is TdApi.MessageAnimation -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageAudio -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageBasicGroupChatCreate -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageCall -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatAddMembers -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatChangePhoto -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatChangeTitle -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatDeleteMember -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatDeletePhoto -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatJoinByLink -> { TODO("Not implemented Msg Content") }
//            is TdApi.MessageChatSetTtl -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatUpgradeFrom -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageChatUpgradeTo -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageContact -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageContactRegistered -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageCustomServiceAction -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageDocument -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageExpiredPhoto -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageExpiredVideo -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageGame -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageGameScore -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageInvoice -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageLocation -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePassportDataReceived -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePassportDataSent -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePaymentSuccessful -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePaymentSuccessfulBot -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePhoto -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePinMessage -> { TODO("Not implemented Msg Content") }
            is TdApi.MessagePoll -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageScreenshotTaken -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageSticker -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageSupergroupChatCreate -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageUnsupported -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageVenue -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageVideo -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageVideoNote -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageVoiceNote -> { TODO("Not implemented Msg Content") }
            is TdApi.MessageWebsiteConnected -> { TODO("Not implemented Msg Content") }
            else -> {
                throw RuntimeException("sub class of TdApi.MessageContent is unknown")
            }
        }

    }

    /**
     * A preview of the web page that's mentioned in the text; may be null.
     * val webPage: TdApi.WebPage = content.webPage
     *
     * Entities can be nested, but must not mutually intersect with each other.
     * Pre, Code and PreCode entities can't contain other entities.
     * Bold, Italic, Underline and Strikethrough entities can contain
     * and to be contained in all other entities.
     * All other entities can't contain each other.
     *
     */
    private fun mapMessageTextResultToModel(messageText: TdApi.MessageText): MessageText {
        // A text with some entities.
        val formattedTextResult: TdApi.FormattedText = messageText.text
        val formattedText: FormattedText = mapFormattedTextResultToModel(formattedTextResult)

        return MessageText(
            formattedText = formattedText
        )
    }

    /**
     *
     */
    private fun mapFormattedTextResultToModel(formattedText: TdApi.FormattedText): FormattedText {
        val plainText: String = formattedText.text
        // Entities contained in the text
        val entitiesResult: Array<TdApi.TextEntity> = formattedText.entities
        val entities: List<TextEntity> = buildList {
            for(entityResult in entitiesResult) {
                val textEntity: TextEntity = mapTextEntityResultToModel(entityResult)
                add(textEntity)
            }
        }

        return FormattedText(
            text = plainText,
//            entities = entities // TODO: uncomment on production version
        )
    }

    /**
     *
     */
    private fun mapTextEntityResultToModel(entityResult: TdApi.TextEntity): TextEntity {
        // FIXME: Parse correctly each Text Entity Type
        //  val type = entityResult.type
        return TextEntity(
            length = entityResult.length,
            offset = entityResult.offset,
            type = TextEntityType.Bold
        )
    }

}
