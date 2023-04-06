package com.github.otr.console_client.handler

import it.tdlight.client.CommandHandler
import it.tdlight.client.SimpleTelegramClient
import it.tdlight.jni.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
 *
 * @see it.tdlight.client.CommandHandler
 */
fun onStopCommand(client: SimpleTelegramClient) = CommandHandler { chat, commandSender, arguments ->
    if (isAdmin(commandSender)) {
        val logger: Logger = LoggerFactory.getLogger("StopCommand")
        val causePrefix: String = "Received"
        val causeName: String = "TdApi.UpdateNewMessage"
        val message: String = "Received `/stop` command. closing..."
        logger.debug("$causePrefix $causeName, $message")
        // Stop the client
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
 *
 * @see it.tdlight.client.CommandHandler
 */
@Deprecated("Just to show another way of satisfying CommandHandler interface")
fun finishCommandHandler(
    chat: TdApi.Chat,
    commandSender: TdApi.MessageSender,
    arguments: String,
    client: SimpleTelegramClient
){
    if (isAdmin(commandSender)) {
        // Stop the client
        println("Received `/finish` command. closing...")
        client.sendClose()
    }
}

private fun isAdmin(sender: TdApi.MessageSender): Boolean = sender == ADMIN_USER
