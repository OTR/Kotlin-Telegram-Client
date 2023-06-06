package com.github.otr.console_client.integration.real_dc

import com.github.otr.console_client.data.network.ConsoleClient
import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.domain.entity.AuthState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Run console client against a real Telegram's data center
 *
 * A suit of test cases that cover an instance of Console Client and its methods
 *
 * @see com.github.otr.console_client.data.network.ConsoleClient
 */
class ConsoleClientRealDCTest {

    companion object {

        private const val USE_REAL_DC: Boolean = false
    }

    @Rule
    @JvmField
    val tempFolder: TemporaryFolder = TemporaryFolder()

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
        val consoleCLI: ConsoleClient = ConsoleClient(
            resourcesPath = resourcesPath,
            useTestDc = USE_REAL_DC
        )
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
                Assert.assertEquals(expectedState, actualState)
            }
        }

        Thread.sleep(1000) // FIXME
        Assert.assertEquals(0, stateSequence.size)
    }

}
