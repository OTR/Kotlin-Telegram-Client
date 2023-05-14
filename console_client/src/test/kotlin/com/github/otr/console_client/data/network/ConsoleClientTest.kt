package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.domain.entity.AuthState
import jdk.jfr.Description
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 *
 */
class ConsoleClientTest {

    @Rule
    @JvmField
    val tempFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun testCreateInstanceOfConsoleClient() {
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

    @Test
    fun testAddHandlerToConsoleClient() {
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

    @Test
    fun testMainMethodOfConsoleClient() {
        // GIVEN
        val resourcesPath: String = tempFolder.root.path
        val consoleCLI: ConsoleClient = ConsoleClient(resourcesPath)
        consoleCLI.addHandlers(HandlerType.COMMON)
        val stateSequence: MutableList<AuthState> = mutableListOf(
            AuthState.INITIAL,
            AuthState.WAIT_TD_LIB_PARAMETERS,
            AuthState.WAIT_PHONE_NUMBER
        )
        stateSequence.take(1)

        // WHEN
        val scope = CoroutineScope(Dispatchers.Default)
        // THEN
        scope.launch {
            consoleCLI.stateFlow.collect {
                val expectedState: AuthState = stateSequence.removeAt(0)
                val actualState: AuthState = it
                println(it)
                assertEquals(expectedState, actualState)
            }
        }
        scope.launch { consoleCLI.main() }
        Thread.sleep(5000)
    }

    // TODO: Test emitting States
//    consoleCLI.emitState(AuthState.LOGGING_OUT)
//    Thread.sleep(500)

}
