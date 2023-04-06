package com.github.otr.console_client.handler

import it.tdlight.client.GenericUpdateHandler
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
            TdApi.UpdateConnectionState::class.java, //suspicious
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
            //
            TdApi.UpdateAuthorizationState::class.java -> {
                logger.debug(buildLogMessage(update, "TODO: Redirect to Auth Handler"))
            }
            //
            TdApi.UpdateChatLastMessage::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateChatPosition::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateNewChat::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateUnreadChatCount::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateUnreadMessageCount::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateUser::class.java -> {
                logger.debug(buildLogMessage(update, "TODO handling"))
            }
            //
            TdApi.UpdateOption::class.java -> {
                update as TdApi.UpdateOption
                val description: String = "${update.name} : ${update.value}"
                logger.trace(buildLogMessage(update, description))
            }
            //
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
