package com.github.otr.console_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handle all Update Types inherited from TdApi.Update that our client receives
 * after being successfully logged in
 *
 * @property uninterestedUpdateTypes update types we are not interested to handle for now
 *
 * <b>Sample Usage</b>
 * ```
 * client.addUpdatesHandler { onCommonUpdates(client) }
 * ```
 *
 * @see it.tdlight.client.GenericUpdateHandler
 */
fun onCommonUpdates(client: SimpleTelegramClient) = GenericUpdateHandler<TdApi.Update> { update ->

    val logger: Logger = LoggerFactory.getLogger("CommonUpdates")
    val causePrefix: String = "Received"

    val uninterestedUpdateTypes: List<TdApi.Update> = listOf(
        /* TODO */
    )
    when (update) {
        is TdApi.UpdateOption -> {
            val causeName: String = "TdApi.UpdateOption"
            val message: String = "${update.name} : ${update.value}"
            logger.debug("$causePrefix $causeName, $message")
        }
        else -> {
            val causeName: String = update.javaClass.simpleName
            val message: String = "Update type TdApi.$causeName is not supported"
            logger.debug("$causePrefix $causeName, $message")
        }
    }

}
