# Kotlin Telegram Client

## Project Description
Android application written in Kotlin making use of TDLight library to interact with Telegram API. Jetpack Compose is used for UI.

## Documentation

Check out our [GitHub Wiki Pages](https://github.com/OTR/Kotlin-Telegram-Client/wiki)

For documentation about `console_client` module check out its [README](console_client/README.md) file.

## Screenshots

:soon: _TODO_

## Project Structure

### `app` directory

Contains Android Application module

### `console_client` directory

Contains [Simple Telegram Console Client](console_client/README.md) with use of TD Light Java

## Requirements

Android device (or emulator) with API Level from API 26 (Android 8 Oreo) to API 33

## Development environment

[FIXME]: Update versions of dependencies

 * OS: Windows 10
 * Gradle JDK: Android Studio Default JDK 11 (version 11.0.15)
 * Android Gradle Plugin Version: 7.4.2
 * Gradle Version: 7.5
 * Kotlin version: 1.8.10
 * Source compatibility (for Android Application): Java version 1.8 (Java 8)
 * Source compatibility (for console client module): Java version 17 (Java 17)
 * Jetpack Compose version: 1.4.1
 * Jetpack Compose Compiler version: 1.4.4
 * TDLight-Java version 2.8.10.6
 * TDLib version 1.8.10

## Tested on devices (test device environment)

:soon: _TODO_

## References

### Code examples in Java

[TDLight Java](https://github.com/tdlight-team/tdlight-java/) - Complete Bot and Userbot Telegram library based on TDLib. [Documentation](https://tdlight-team.github.io/)

### Articles with code examples in Java

:soon: _TODO_

### Code examples in Kotlin

[Bare minimum working example](https://github.com/vchernetskyi993/messengers-playground/tree/master/tg-tdlight/src/main/kotlin/com/example) - TDLib Kotlin integration. Simple command-line application that sends `Hello World!` Telegram message from `APP_SENDER_PHONE` to `APP_RECEIVER_PHONE`.
On the first run will request code confirmation to login on the device as sender.

[Kotlin Coroutines extensions for Telegram API TDLib](https://github.com/tdlibx/td-ktx) - barely working and outdated example 

### Articles with code examples in Kotlin

:soon: _TODO_
