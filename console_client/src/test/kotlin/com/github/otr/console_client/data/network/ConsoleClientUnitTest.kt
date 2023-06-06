package com.github.otr.console_client.data.network

import com.github.otr.console_client.data.network.handler.HandlerType
import com.github.otr.console_client.domain.entity.AuthState

import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * A suit of test cases that cover an instance of Console Client and its methods
 *
 * @see com.github.otr.console_client.data.network.ConsoleClient
 */
class ConsoleClientUnitTest {

    companion object {

        private const val DONT_USE_DC_AT_ALL: Boolean = false
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
        val consoleCLI: ConsoleClient = ConsoleClient(
            resourcesPath = resourcesPath,
            useTestDc = DONT_USE_DC_AT_ALL
        )

        // THEN
        Assert.assertEquals(resourcesPath, consoleCLI.resourcesPath)

        val actualState: AuthState = consoleCLI.stateFlow.value
        val expectedState: AuthState = AuthState.INITIAL

        Assert.assertEquals(expectedState, actualState)
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
        val consoleCLI: ConsoleClient = ConsoleClient(
            resourcesPath = resourcesPath,
            useTestDc = DONT_USE_DC_AT_ALL
        )

        // WHEN
        consoleCLI.addHandlers(HandlerType.COMMON)

        // THEN
        val actualState: AuthState = consoleCLI.stateFlow.value
        val expectedState: AuthState = AuthState.INITIAL

        Assert.assertEquals(expectedState, actualState)
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


}