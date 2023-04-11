package com.github.otr.console_client.data.network.handler.chat

import it.tdlight.client.Result
import it.tdlight.jni.TdApi

/**
 * A handler to process incoming update of type `TdApi.UpdateUser`
 *
 * @see it.tdlight.client.GenericResultHandler
 */
object UserResultHandler : ResultHandlerBase<TdApi.User>(
    loggerName = "UserResultHand"
) {

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
        val user: TdApi.User = result.get()
        val firstName: String = user.firstName
        val usernames: TdApi.Usernames? = user.usernames
        val activeUsernames: Array<String>? = usernames?.activeUsernames
        val nickName: String? = activeUsernames?.first()
        val lineAboutNickname: String = if (nickName == null) {
            "without nickname"
        } else {
            "nickname: $nickName"
        }
        val phoneNumber: String = user.phoneNumber

        logger.debug(
            "In our chat list there is a user with first name: $firstName" +
                    " and $lineAboutNickname and phone number: $phoneNumber"
        )
    }

}
