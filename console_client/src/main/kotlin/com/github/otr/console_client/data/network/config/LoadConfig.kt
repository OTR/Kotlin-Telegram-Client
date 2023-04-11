package com.github.otr.console_client.data.network.config

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.Properties
import kotlin.io.path.Path

private const val RESOURCES_BASE_DIR: String = "console_client/src/main/resources/"
private const val DEV_CONFIG_FILE_NAME: String = "console_client_dev.properties"

/**
 * A helper function to pass some variable values stored under `resources` directory
 * to the program during development stage.
 */
fun loadDevelopmentConfig(): Properties {

    val properties: Properties = Properties()
    properties.load(FileInputStream(
        Path(RESOURCES_BASE_DIR).resolve(DEV_CONFIG_FILE_NAME).toFile()
    ))

    return properties
}

/**
 * Just for testing purposes.
 */
fun main() {
    val properties: Properties = loadDevelopmentConfig()
    val chatResultHandlerLogLevel: String = properties.getProperty("chat_result_handler_log_level")
    println(chatResultHandlerLogLevel)
    val testLogger : Logger = LoggerFactory.getLogger("TestLogger") as Logger
    testLogger.level = Level.valueOf(chatResultHandlerLogLevel)
    println(testLogger.level.levelStr)
}
