package com.github.otr.simple_client.handler

import it.tdlight.client.CommandHandler
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.TelegramError
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

import org.slf4j.LoggerFactory
import java.util.Optional
import java.util.function.Supplier

/**
 *
 */
internal class MyCommandsHandler(
    private val client: TelegramClient,
    private val commandHandlers: Map<String, Set<CommandHandler>>,
    private val me: Supplier<TdApi.User>
) : GenericUpdateHandler<TdApi.UpdateNewMessage> {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     *
     */
    private val myUsername: Optional<String>
        get() {
            val user: TdApi.User = me.get()
            val username: String? = user.usernames.activeUsernames.firstOrNull()
            return if (username == null || username.isEmpty()) {
                Optional.empty()
            } else {
                Optional.of(username)
            }
        }

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateNewMessage) {
        val message = update.message
        if (message.forwardInfo == null
            && !message.isChannelPost
            && (message.authorSignature == null || message.authorSignature.isEmpty())
            && message.content is TdApi.MessageText
        ) {
            val messageText = message.content as TdApi.MessageText
            val text = messageText.text.text
            if (text.startsWith("/")) {
                var parts = text.split(" ".toRegex(), limit = 2).toTypedArray()
                if (parts.size == 1) {
                    parts = arrayOf(parts[0], "")
                }
                if (parts.size == 2) {
                    val currentUnsplittedCommandName = parts[0].substring(1)
                    val arguments = parts[1].trim { it <= ' ' }
                    val commandParts =
                        currentUnsplittedCommandName.split("@".toRegex(), limit = 2)
                            .toTypedArray()
                    var currentCommandName: String? = null
                    var correctTarget = false
                    if (commandParts.size == 2) {
                        val myUsername = myUsername.orElse(null)
                        if (myUsername != null) {
                            currentCommandName = commandParts[0].trim { it <= ' ' }
                            val currentTargetUsername = commandParts[1]
                            if (myUsername.equals(currentTargetUsername, ignoreCase = true)) {
                                correctTarget = true
                            }
                        }
                    } else if (commandParts.size == 1) {
                        currentCommandName = commandParts[0].trim { it <= ' ' }
                        correctTarget = true
                    }
                    if (correctTarget) {
                        val commandName = currentCommandName
                        val handlers =
                            commandHandlers.getOrDefault(currentCommandName, emptySet())
                        for (handler in handlers) {
                            client.send(TdApi.GetChat(message.chatId),
                                { response: TdApi.Object ->
                                    if (response is TdApi.Error) {
                                        throw TelegramError(response)
                                    }
                                    handler.onCommand(
                                        response as TdApi.Chat,
                                        message.senderId,
                                        arguments
                                    )
                                }
                            ) { error: Throwable? ->
                                logger.warn(
                                    "Error when handling the command {}",
                                    commandName,
                                    error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
