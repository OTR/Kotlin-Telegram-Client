package com.github.otr.console_client.data.network.handler.chat

import com.github.otr.console_client.data.mapper.RosterMapper
import com.github.otr.console_client.data.mapper.asLogMsg
import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.chat.Message

import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 *
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Message.html">
 *     Documentation on returned type of `TdApi.Message`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageSender.html">
 *     Documentation on returned type of `TdApi.MessageSender`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageContent.html">
 *     Documentation on returned type of `TdApi.MessageContent`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.MessageText.html">
 *     Documentation on returned type of `TdApi.MessageText`
 *     </a>
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.FormattedText.html">
 *     Documentation on returned type of `TdApi.FormattedText`
 *     </a>
 *
 */
class MessageResultHandler(
    loggerName: String,
    val consoleCLI: ConsoleClient
) : ResultHandlerBase<TdApi.Message>(loggerName) {

    private val mapper: RosterMapper = RosterMapper

    override fun onResult(result: Result<TdApi.Message>) {
        val messageResult: TdApi.Message = result.get()
        val messageModel: Message = mapper.mapMessageResultToModel(messageResult)
        //
        logger.debug(messageModel.asLogMsg(loggerName))
        //
        // TODO: when(loggerName) "LastMessage" or "NewMessage" ?
        consoleCLI.roster.addOrUpdateMessage(messageModel)

    }

}
