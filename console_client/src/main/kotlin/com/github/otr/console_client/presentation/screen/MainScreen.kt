package com.github.otr.console_client.presentation.screen

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.entity.chat.asPlainText
import com.github.otr.console_client.domain.entity.chat.formattedDate
import com.github.otr.console_client.presentation.viewmodel.RosterOwner

/**
 * A class representing the Main Screen where is the Uses got after successful logging in
 */
class MainScreen(
    private val viewModel: RosterOwner
): Screen() {

    companion object {

        private const val MENU_TITLE: String = "[Main Menu]"
        private val BOTTOM_LINE: String = "_".repeat(80)
        private const val NEW_LINE: String = "\n"

    }

    private val roster: Roster = viewModel.getRoster()

    override val menuItems: List<Triple<String, String, () -> Unit>> = listOf(
        Triple("1", "Show roster") { println(getRosterAsString(roster)) },
        Triple("9", "Show help") { println("This is help") },
        Triple("0", "Exit") { println("Can't exit") },
    )

    /**
     * Return a predefined list of menu items with their indexes.
     */
    override fun getMenu(): String {
        val menu: String = super.getMenu()
        return listOf(MENU_TITLE, menu, BOTTOM_LINE).joinToString(NEW_LINE)
    }

    /**
     * Output each Chat from Main List of a Roster with their last messages
     * TODO: Output not only main list
     * TODO: Order by last message's date
     */
    private fun getRosterAsString(roster: Roster): String {
        var output: String = ""
        for (chat in roster.chatListMain) {
            val chatEntity: Chat = chat.value
            val chatTitle: String = chatEntity.title
            output += BOTTOM_LINE + NEW_LINE
            output += "[$chatTitle]$NEW_LINE"
            val chatMessages: List<Message>? = chatEntity.messages
            chatMessages?.let {
                for (message in chatMessages) {
                    val formattedDate: String = message.formattedDate()
                    val plainText: String = message.asPlainText()
                    output += "[$formattedDate] $plainText$NEW_LINE"
                }
            }
            output += BOTTOM_LINE + NEW_LINE
        }
        return output.trimEnd()
    }

}
