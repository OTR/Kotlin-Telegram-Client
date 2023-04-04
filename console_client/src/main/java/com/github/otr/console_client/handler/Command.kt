package com.github.otr.console_client.handler

import it.tdlight.client.CommandHandler
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi

private val ADMIN_ID: Long = System.getenv("TELEGRAM_RECIPIENT_CHAT_ID")?.toLong()
    ?: throw IllegalArgumentException(
        "TELEGRAM_RECIPIENT_CHAT_ID environment variable is not set\n" +
        "Should look like: 667900586"
    )
private val ADMIN_USER: TdApi.MessageSender = TdApi.MessageSenderUser(ADMIN_ID)

/**
 * One way of passing command handler. Will trigger on /stop message in private chat
 *
 * <b>Sample Usage</b>
 * ```
 * client.addCommandHandler<TdApi.Update>("stop", stopCommandHandler(client))
 * ```
 */
fun stopCommandHandler(client: SimpleTelegramClient) = CommandHandler { chat, commandSender, arguments ->
    if (isAdmin(commandSender)) {
        // Stop the client
        println("Received stop command. closing...")
        client.sendClose()
    }
}

/**
 * Another way of passing command handler. Will trigger on /finish message in private chat
 *
 * <b>Sample Usage</b>
 * ```
 * client.addCommandHandler<TdApi.Update>("finish") { chat, commandSender, arguments ->
 *     finishCommandHandler(chat, commandSender, arguments, client)
 * }
 * ```
 */
fun finishCommandHandler(
    chat: TdApi.Chat,
    commandSender: TdApi.MessageSender,
    arguments: String,
    client: SimpleTelegramClient
){
    if (isAdmin(commandSender)) {
        // Stop the client
        println("Received finish command. closing...")
        client.sendClose()
    }
}

private fun isAdmin(sender: TdApi.MessageSender): Boolean = sender == ADMIN_USER
