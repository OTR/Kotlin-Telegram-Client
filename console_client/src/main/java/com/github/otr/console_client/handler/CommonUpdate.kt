package com.github.otr.console_client.handler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

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
 */
fun onCommonUpdates(client: SimpleTelegramClient) = GenericUpdateHandler<TdApi.Update> { update ->
    val uninterestedUpdateTypes: List<TdApi.Update> = listOf(
        /* TODO */
    )
    when (update) {
        is TdApi.UpdateOption -> {
//            println("${update.name} : ${update.value}") // TODO: Log to file
        }
        else -> {
            val updateName: String = update.javaClass.simpleName
//                println("Update type $updateName not supported") // TODO: Log to file
        }
    }

}
