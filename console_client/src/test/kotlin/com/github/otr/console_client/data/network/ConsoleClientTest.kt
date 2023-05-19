package com.github.otr.console_client.data.network

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
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * A suit of test cases that cover an instance of Console Client and its methods
 *
 * @see com.github.otr.console_client.data.network.ConsoleClient
 */
class ConsoleClientTest {

    companion object {

        private const val USE_TEST_DC: Boolean = true
        private const val EMPTY_LIST_SIZE: Int = 0
    }

    @Rule
    @JvmField
    val tempFolder: TemporaryFolder = TemporaryFolder()

    /**
     * A positive test case for a Console Client's constructor
     * Create an instance of Console Client with a given resources Path
     * And subscribe to it's Flow
     */
    @Test
    fun testPositiveCreateInstanceOfConsoleClient() {
        // GIVEN
        // For Windows Temporary Folder would be like:
        // C:\Users\User\AppData\Local\Temp\junit12895079684515193141
        val resourcesPath: String = tempFolder.root.path

        // WHEN
        val consoleCLI: ConsoleClient = ConsoleClient(resourcesPath = resourcesPath)

        // THEN
        assertEquals(resourcesPath, consoleCLI.resourcesPath)

        val actualState: AuthState = consoleCLI.stateFlow.value
        val expectedState: AuthState = AuthState.INITIAL

        assertEquals(expectedState, actualState)
    }

    /**
     * A positive test case for the `addHandlers(...)` method of a Console Client
     * Create an instance of Console Client and set up `COMMON` handler for processing
     * received Updates
     */
    @Test
    fun testPositiveAddHandlerToConsoleClient() {
        // GIVEN
        val resourcesPath: String = tempFolder.root.path
        val consoleCLI: ConsoleClient = ConsoleClient(resourcesPath)

        // WHEN
        consoleCLI.addHandlers(HandlerType.COMMON)

        // THEN
        val actualState: AuthState = consoleCLI.stateFlow.value
        val expectedState: AuthState = AuthState.INITIAL

        assertEquals(expectedState, actualState)
    }

    /**
     * A positive test case for the `main()` method of a Console Client
     * Create an instance of a Console Client, add the `COMMON` handler for processing
     * received Updates, make it serve forever by invoking the `main()` method,
     * after some delay ensure that a sequence of Auth Updates has the exact size and order
     * of States and then cancel a coroutines with infinite loop and Flow collector
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPositiveMainMethodOfConsoleClient() = runTest {
        // GIVEN
        val resourcesPath: String = tempFolder.root.path
        val consoleCLI: ConsoleClient = ConsoleClient(resourcesPath)
        consoleCLI.addHandlers(HandlerType.COMMON)
        val stateSequence: MutableList<AuthState> = mutableListOf(
            AuthState.INITIAL,
            AuthState.WAIT_TD_LIB_PARAMETERS,
            AuthState.WAIT_PHONE_NUMBER
        )
        val scope = CoroutineScope(Dispatchers.Default)

        // WHEN
        scope.launch { consoleCLI.main() }

        // THEN
        scope.launch {
            consoleCLI.stateFlow.collect {
                val expectedState: AuthState = stateSequence.removeAt(0)
                val actualState: AuthState = it
                println(it)
                assertEquals(expectedState, actualState)
            }
        }

        Thread.sleep(1000) // FIXME
        assertEquals(0, stateSequence.size)
    }

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
                assertEquals(expected, actual)
            }
        }

        runBlocking {
            delay(7000)
            job1.cancel()
            job2.cancel()
            val expected: Int = EMPTY_LIST_SIZE
            val actual: Int = rightSizeAndOrderOfStates.size
            assertEquals(expected, actual)
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
     * A positive test case where we emit a State and collect the same State
     */
    @Ignore("Not implemented yet")
    @Test
    fun testPositiveEmittingStates() {
        // TODO: Test emitting States
        // consoleCLI.emitState(AuthState.LOGGING_OUT)
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
