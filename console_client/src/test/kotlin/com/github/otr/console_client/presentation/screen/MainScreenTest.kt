package com.github.otr.console_client.presentation.screen

import com.github.otr.console_client.data.model.TestDataSource
import com.github.otr.console_client.domain.entity.chat.Roster
import com.github.otr.console_client.presentation.viewmodel.RosterOwner

import org.junit.Assert.assertEquals
import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 *
 */
class MainScreenTest {

    private val fakeViewModel: RosterOwner = object : RosterOwner {

        override fun getRoster(): Roster {
            return TestDataSource().getExpectedRoster()
        }
    }

    /**
     * A positive test case, Roster output looks as wished
     */
    @Test
    fun testDisplayRoster() {
        // GIVEN
        // Set up fake Output Stream
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        System.setOut(printStream)
        val expectedResult: String = """
            ________________________________________________________________________________
            [Telegram]
            [19.05.23 12:21:16] Code for Telegram: 11111
            ________________________________________________________________________________
            ________________________________________________________________________________
            [John]
            [22.05.23 14:36:11] last
            [22.05.23 14:38:05] get a new one
            ________________________________________________________________________________
            ________________________________________________________________________________
            [Spam Info Bot]
            [26.03.23 14:10:59] Thanks. Your report has sent
            ________________________________________________________________________________
        """.trimIndent()


        // WHEN
        val screen: MainScreen = MainScreen(fakeViewModel)
        val menuItems = screen.menuItems
        val getRosterAsStringCallBack: () -> Unit = menuItems
            .find { it.second.contains("roster") }?.third ?: {}
        getRosterAsStringCallBack.invoke()

        // THEN
        val actualResult: String = outputStream.toString().trimIndent()
        assertEquals(expectedResult, actualResult)
    }

}
