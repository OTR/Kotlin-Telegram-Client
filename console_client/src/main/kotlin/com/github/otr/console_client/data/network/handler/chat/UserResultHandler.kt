package com.github.otr.console_client.data.network.handler.chat

import com.github.otr.console_client.data.mapper.RosterMapper
import com.github.otr.console_client.data.mapper.asLogMsg
import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.domain.entity.chat.User
import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 * A handler to process incoming update of type `TdApi.UpdateUser`
 *
 * @see it.tdlight.client.GenericResultHandler
 */
class UserResultHandler(
    private val consoleCLI: ConsoleClient
) : ResultHandlerBase<TdApi.User>(
    loggerName = "UserResultHand"
) {

    private val mapper: RosterMapper = RosterMapper

    /**
     * @see
     * <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.User.html">
     *     Documentation on result type `TdApi.User`
     * </a>
     * @see
     * <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.Usernames.html">
     *     Documentation on result type `TdApi.Usernames`
     * </a>
     */
    override fun onResult(result: Result<TdApi.User>) {
        val userResult: TdApi.User = result.get()
        val userModel: User = mapper.mapUserResultToModel(userResult)
        //
        logger.debug(userModel.asLogMsg())
        // Update roster
        consoleCLI.roster.addOrUpdateUser(userModel)
    }

}
