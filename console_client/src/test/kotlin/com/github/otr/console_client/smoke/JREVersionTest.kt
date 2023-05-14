package com.github.otr.console_client.smoke

import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

/**
 * Test that proper JRE version is used
 */
class JREVersionTest {

    @Ignore("Not implemented yet")
    @Test
    fun testJREVersion(){
        val JREVersion: String = System.getProperty("java.version")
        assertTrue(JREVersion.startsWith("17"))
    }

}
