package com.github.otr.console_client.integration.test_dc

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.data.network.ConsoleLogin
import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.data.network.login_handler.TestDcLogin
import com.github.otr.console_client.domain.entity.AuthState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Test console client against Telegram's test environment (test data center)
 */
class ConsoleClientTestDCTest {

    companion object {

        private const val USE_TEST_DC: Boolean = true
        private const val EMPTY_LIST_SIZE: Int = 0
    }

    @Rule
    @JvmField
    val tempFolder: TemporaryFolder = TemporaryFolder()

    /**
     * A positive test case where it's the very first login of a Client
     * And database directory is not created and no cached settings
     * The Client uses a test number `9996611111`
     * And the right verification code for that number is `1111`
     * FIXME: Fickle, not stable test, when run a whole suit tests in parallel
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPositiveTheVeryFirstLoginToTestDC() {
        // GIVEN
        val rightSizeAndOrderOfStates: MutableList<AuthState> = mutableListOf(
            AuthState.INITIAL,
            AuthState.WAIT_TD_LIB_PARAMETERS,
            AuthState.WAIT_PHONE_NUMBER,
            AuthState.WAIT_CODE,
            AuthState.READY
        )
        val resourcesPath: String = tempFolder.root.path
        val consoleCLI: ConsoleClient = ConsoleClient(
            resourcesPath = resourcesPath,
            useTestDc = USE_TEST_DC
        )
        consoleCLI.addHandlers(HandlerType.COMMON)
        val scope = CoroutineScope(Dispatchers.Default)

        // WHEN
        val job1: Job = scope.launch {
            consoleCLI.main(
                customAuthMethod = TestDcLogin,
                useCustomClientInteraction = ConsoleLogin.TEST_CLIENT_INTERACTION
            )
        }

        // THEN
        val job2: Job = scope.launch {
            consoleCLI.stateFlow.collect {
                println("${rightSizeAndOrderOfStates.size} $it")
                val expected: AuthState = rightSizeAndOrderOfStates.removeAt(0)
                val actual: AuthState = it
                Assert.assertEquals(expected, actual)
            }
        }

        runBlocking {
            delay(3000) // FIXME: When it's ran in a test suit even delay not helping much
            job1.cancel()
            job2.cancel()
            val expected: Int = EMPTY_LIST_SIZE
            val actual: Int = rightSizeAndOrderOfStates.size
            Assert.assertEquals(expected, actual)
        }

    }

    /**
     * A negative test case where it's the very first login of a Client
     * And database directory is not created and no cached settings
     * The Client uses a test number `9996611111`
     * And the right verification code for that number is `1111`
     * But we send `1112` which is the wrong one to ensure Error handling
     */
    @Ignore("Not implemented yet")
    @Test
    fun testNegativeTheVeryFirstLoginToTestDC() {
        // GIVEN

        // WHEN

        // THEN

    }

    /**
     * TODO:
     * A positive test case
     * Ensure there is a Chat `Telegram Notifications` with `42777` number
     * in the Client's roster
     */
    @Ignore("Not implemented yet")
    @Test
    fun testPositiveTelegramNotificationsInTheRoster() {

    }

}
