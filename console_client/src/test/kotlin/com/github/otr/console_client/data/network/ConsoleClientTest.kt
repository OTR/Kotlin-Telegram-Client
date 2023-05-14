package com.github.otr.console_client.data.network

import com.github.otr.console_client.domain.entity.AuthState

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
    fun testCreateBrandNewClient() {
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

}
