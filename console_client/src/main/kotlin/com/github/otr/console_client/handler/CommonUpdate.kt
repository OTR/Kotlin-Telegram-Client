package com.github.otr.console_client.handler

import com.github.otr.console_client.handler.chat.ChatPositionResultHandler
import com.github.otr.console_client.handler.chat.ChatResultHandler
import com.github.otr.console_client.handler.chat.MessageResultHandler
import com.github.otr.console_client.handler.chat.UserResultHandler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.Result
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handle all Update Types inherited from `TdApi.Update` that our client receives
 * after being successfully logged in
 *
 * @property uninterestedUpdateTypes update types we are not interested to handle for now
 *
 * <b>Sample Usage</b>
 * ```
 * client.addUpdatesHandler(CommonUpdatesHandler(client))
 * ```
 *
 * @see it.tdlight.client.GenericUpdateHandler
 */
class CommonUpdatesHandler(
    client: SimpleTelegramClient
) : GenericUpdateHandler<TdApi.Update> {

    private companion object {

        val logger: Logger = LoggerFactory.getLogger("CommonUpdates")
        const val CAUSE_PREFIX: String = "Received"

        /**
         * @see <a href="https://github.com/OTR/Kotlin-Telegram-Client/wiki/List-of-General-Updates">
         *      Short explanation for each direct subclass of `TdApi.Update` Type
         *     </a>
         */
        val uninterestedUpdateTypes: List<Class<out TdApi.Update>> = listOf(
            TdApi.UpdateActiveEmojiReactions::class.java,
            TdApi.UpdateAnimationSearchParameters::class.java,
            TdApi.UpdateAttachmentMenuBots::class.java,
            TdApi.UpdateChatFilters::class.java, // suspicious
            TdApi.UpdateChatThemes::class.java,
            TdApi.UpdateConnectionState::class.java, // suspicious
            TdApi.UpdateDiceEmojis::class.java,
            TdApi.UpdateDefaultReactionType::class.java,
            TdApi.UpdateFileDownloads::class.java,
            TdApi.UpdateHavePendingNotifications::class.java,
            TdApi.UpdateScopeNotificationSettings::class.java,
            TdApi.UpdateSelectedBackground::class.java
        )
    }

    override fun onUpdate(update: TdApi.Update) {
        val updateType = update::class.java
        when (updateType) {
            // Represents an incoming update about the authorization state of the user.
            // https://github.com/OTR/Kotlin-Telegram-Client/wiki/Td-Api-Update-Authorization-State
            TdApi.UpdateAuthorizationState::class.java -> {
                logger.trace(buildLogMessage(update, "TODO: Redirect to Auth Handler"))
            }
            // Note that in this branch we catch **ONLY THE LAST** message of a chat
            TdApi.UpdateChatLastMessage::class.java -> {
                update as TdApi.UpdateChatLastMessage
                val chatLastMessage: TdApi.Message = update.lastMessage
                // val chatId: Long = update.chatId
                // val chatPositions: Array<TdApi.ChatPosition> = update.positions
                MessageResultHandler(
                    loggerName = "LastMessageHan"
                ).onResult(Result.of(chatLastMessage))
            }
            // Represents an update about the new position of a chat in a chat list.
            TdApi.UpdateChatPosition::class.java -> {
                update as TdApi.UpdateChatPosition
                //  val chatId: Long = update.chatId
                val chatPosition: TdApi.ChatPosition = update.position
                ChatPositionResultHandler.onResult(Result.of(chatPosition))
            }
            // Represents a chat Entity from your Chat List that has been loaded/created.
            // TdApi.Chat entity contains information about:
            // user Id - unique numerical identifier of a Telegram user
            // chat Type - represents a kind of Chat entity: Private, BasicGroup, SuperGroup
            // title (in case of Private chat - first name of a person you have a chat with)
            // unread count - a number of unread messages from that Person, Group ...
            TdApi.UpdateNewChat::class.java -> {
                update as TdApi.UpdateNewChat
                val newChat: TdApi.Chat = update.chat
                ChatResultHandler.onResult(Result.of(newChat))
            }
            // TODO: Rewrite Handler. Looks like you need to pass the whole update
            //       and implement `GenericUpdateHandler`
            // Number of unread chats, i.e. with unread messages or marked as unread, has changed.
            // This update is sent only if the message database is used.
            TdApi.UpdateUnreadChatCount::class.java -> {
                update as TdApi.UpdateUnreadChatCount
                val chatList: TdApi.ChatList = update.chatList
                 val unreadCount: Int = update.unreadCount
                // val unreadUnmutedCount: Int = update.unreadUnmutedCount
                // val markedAsUnreadCount: Int = update.markedAsUnreadCount
                // val markedAsUnreadUnmutedCount: Int = update.markedAsUnreadUnmutedCount
                // val totalCount: Int = update.totalCount
                logger.debug("You have $unreadCount unread chat of type ${chatList.javaClass.simpleName}")
//                ChatListResultHandler.onResult(Result.of(chatList))
            }
            // Number of unread messages in a chat list has changed
            // This update is sent only if the message database is used.
            // https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.UpdateUnreadMessageCount.html
            TdApi.UpdateUnreadMessageCount::class.java -> {
                update as TdApi.UpdateUnreadMessageCount
                val unreadCount: Int = update.unreadCount
                val chatListType: TdApi.ChatList = update.chatList
                val chatListTypeName: String = chatListType.javaClass.simpleName
                    .removePrefix("ChatList")
                val unreadUnmutedCount: Int = update.unreadUnmutedCount
                logger.debug(
                    "The total number of unread messages in a Chat List of type $chatListTypeName" +
                    " now equals to $unreadCount"
                )
            }
            // Triggers on a real regular user, Spam Bot,
            // `Telegram` account and on itself in chat list
            TdApi.UpdateUser::class.java -> {
                update as TdApi.UpdateUser
                val user: TdApi.User = update.user
                UserResultHandler.onResult(Result.of(user))
            }
            // https://github.com/OTR/Kotlin-Telegram-Client/wiki/List-of-Update-Options
            // Skip UpdateOptions
            TdApi.UpdateOption::class.java -> {
                update as TdApi.UpdateOption
                val description: String = "${update.name} : ${update.value}"
                logger.trace(buildLogMessage(update, description))
            }
            // Skip uninterested Update Types
            in uninterestedUpdateTypes -> {
                logger.trace(buildLogMessage(update, "Update type is not interested"))
            }
            //
            else -> {
                logger.warn(buildLogMessage(update, "Update type is not supported"))
            }
        }
    }

    private fun buildLogMessage(update: TdApi.Update, description: String): String {
        val causeName: String = update.javaClass.simpleName
        return "$CAUSE_PREFIX TdApi.$causeName, $description"
    }

}
