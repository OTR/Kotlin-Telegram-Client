package com.github.otr.console_client.data.model

import com.github.otr.console_client.domain.entity.chat.Chat
import com.github.otr.console_client.domain.entity.chat.Message
import com.github.otr.console_client.domain.entity.chat.MessageText
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.domain.entity.chat.User

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A suit of tests for testing methods of `BlankRosterImpl`
 */
class BlankRosterImplTest {

    private lateinit var roster: Roster

    private val testDataSource = TestDataSource()

    private val logger: Logger = LoggerFactory.getLogger("TstBlankRoster")

    /**
     * Set up before each test. Create a new Blank Roster object.
     */
    @Before
    fun setUp() {
        roster = BlankRosterImpl()
    }

    /**
     * A positive test case, Imitate the order of receiving Update events.
     * TODO: Update expectedRoster in case of lastMessage and newMessage fields
     */
    @Test
    fun testPositiveImitateReceivingChatAndUserEvents() {
        // GIVEN
        assertTrue(roster.chatListMain.isEmpty())
        // prepare expectedRoster, TODO: Rewrite data preparation, because it looks awful
        // set `lastMessage` and `messages` fields for all Chat Entities to `null`
        val expectedRoster: Roster = testDataSource.getExpectedRosterWithNoMessages()

        // WHEN
        with(roster) {
            with(testDataSource) {
                addOrUpdateUser(getUserKotlin())
                addOrUpdateUser(getUserTelegram())
                addOrUpdateChat(getChatTelegram())
                addOrUpdateUser(getUserJohn())
                addOrUpdateChat(getChatJohn())
                addOrUpdateUser(getUserSpamBot())
                addOrUpdateChat(getChatSpamBot())
            }
        }

        // THEN
        assertEquals(expectedRoster, roster)
    }

    /**
     * A positive test case, Roster object is blank and we receive a User event,
     * But there is no appropriate Chat object to put in so we create a blank Chat object
     * and put received User to that Blank Object
     */
    @Test
    fun testPositiveCreateABlankChatAndPutUser() {
        // GIVEN
        val receivedUserEvent: User = testDataSource.getUserJohn()
        val receivedChatEvent: Chat = testDataSource.getChatJohn()
        val blankChatWithUserJohn: Map<Long, Chat> = testDataSource.getBlankChatWithUserJohn()
        val rosterWithBlankChatAndUserJohn: Roster = BlankRosterImpl().apply {
            chatListMain.clear()
            chatListMain.putAll(blankChatWithUserJohn)
        }

        // WHEN
        roster.addOrUpdateUser(receivedUserEvent)
        roster.addOrUpdateChat(receivedChatEvent)

        // THEN
        assertEquals(rosterWithBlankChatAndUserJohn, roster)
    }

    /**
     * A positive test case, Check method `equals` of two Roster objects with the same fields
     */
    @Test
    fun testPositiveCheckMethodEquals() {
        // GIVEN
        val firstRoster: Roster = BlankRosterImpl().apply {
            chatListMain.clear()
            chatListMain.putAll(testDataSource.getBlankChatWithUserJohn())
        }
        val secondRoster: Roster = BlankRosterImpl().apply {
            chatListMain.clear()
            chatListMain.putAll(testDataSource.getBlankChatWithUserJohn())
        }

        // WHEN
        val areEquals: Boolean = firstRoster.equals(secondRoster)

        // THEN
        assertTrue(areEquals)
    }

    /**
     * A positive test case, Imitate receiving User events, then Chat events,
     * then Last Message events, then New Message events
     */
    @Test
    fun testPositiveImitateReceivingLastMessageThenNewMessage() {
        // GIVEN
        // Expected Roster
        val expectedRoster: Roster = testDataSource.getExpectedRoster()
        // WHEN
        // Process incoming events
        with(roster) {
            with(testDataSource) {
                addOrUpdateUser(getUserKotlin())
                //
                addOrUpdateUser(getUserTelegram())
                addOrUpdateChat(getChatTelegram())
                addOrUpdateLastMessage(getLastMessageFromTelegram())
                //
                addOrUpdateUser(getUserJohn())
                addOrUpdateChat(getChatJohn())
                addOrUpdateLastMessage(getLastMessageFromJohn())
                //
                addOrUpdateUser(getUserSpamBot())
                addOrUpdateChat(getChatSpamBot())
                addOrUpdateLastMessage(getLastMessageFromSpamBot())
                //
                addOrUpdateLastMessage(getNewMessageFromJohn())
            }

        }
        println()

        // THEN
        assertEquals(expectedRoster, roster)
    }

    /**
     * A positive test case, for testing persistence of a test sample data
     */
    @Test
    fun testLastMessageHasGreaterDateField() {

        // GIVEN
        val johnChatId: Long = testDataSource.getChatJohn().id

        // WHEN
        val previousMessageFromJohn: Message = testDataSource.getTestMessages()
            .filter { it.chatId == johnChatId }.minBy { it.date }
        val lastMessageFromJohn: Message = testDataSource.getTestMessages()
            .filter { it.chatId == johnChatId }.maxBy { it.date }

        // THEN
        val prevContent: MessageText = previousMessageFromJohn.content as MessageText
        val lastContent: MessageText = lastMessageFromJohn.content as MessageText
        assertTrue(prevContent.formattedText.text.contains("last"))
        assertTrue(lastContent.formattedText.text.contains("new"))
    }

}

/**
 * TODO: Is there a reason for creating extension function? Apart from naming convenience?
 */
private fun Roster.addOrUpdateLastMessage(lastMessageFromTelegram: Message) {
    addOrUpdateMessage(newMessageEntity = lastMessageFromTelegram)
}
