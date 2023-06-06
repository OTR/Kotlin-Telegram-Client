package com.github.otr.console_client.data.network.handler.chat

import com.github.otr.console_client.data.mapper.RosterMapper
import com.github.otr.console_client.data.mapper.asLogMsg
import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.chat.Chat
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
class ChatResultHandler(
    val consoleCLI: ConsoleClient
) : ResultHandlerBase<TdApi.Chat>(
    loggerName = "ChatResultHand"
) {

    private val mapper: RosterMapper = RosterMapper

    /**
     * @see
     * <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Chat.html">
     *      Documentation on returned type `TdApi.Chat`
     * </a>
     *
     */
    override fun onResult(result: Result<TdApi.Chat>) {
        val chatResult: TdApi.Chat = result.get()
        val chatModel: Chat = mapper.mapChatResultToModel(chatResult)
        //
        logger.debug(chatModel.asLogMsg())
        //
        consoleCLI.roster.addOrUpdateChat(chatModel)
    }

}
