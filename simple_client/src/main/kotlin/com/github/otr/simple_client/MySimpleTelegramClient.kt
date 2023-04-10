package com.github.otr.simple_client

import com.github.otr.simple_client.handler.MyAuthorizationStateReadyGetMe
import com.github.otr.simple_client.handler.MyAuthorizationStateReadyLoadChats
import com.github.otr.simple_client.handler.MyAuthorizationStateWaitForExit
import com.github.otr.simple_client.helper.MyScannerClientInteraction
import com.github.otr.simple_client.handler.MyDefaultResultHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitAuthenticationDataHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitCodeHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitOtherDeviceConfirmationHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitPasswordHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitRegistrationHandler
import com.github.otr.simple_client.handler.auth.MyAuthorizationStateWaitTdlibParametersHandler
import com.github.otr.simple_client.handler.MyCommandsHandler
import com.github.otr.simple_client.helper.InteractionWrapper

import it.tdlight.client.Authenticable
import it.tdlight.client.AuthenticationData
import it.tdlight.client.ClientInteraction
import it.tdlight.client.ConsoleInteractiveAuthenticationData
import it.tdlight.client.CommandHandler
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.GenericResultHandler
import it.tdlight.client.Result
import it.tdlight.client.TDLibSettings
import it.tdlight.common.Init
import it.tdlight.common.ConstructorDetector
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.ResultHandler
import it.tdlight.common.TelegramClient
import it.tdlight.common.internal.CommonClientManager
import it.tdlight.common.utils.CantLoadLibrary
import it.tdlight.common.utils.LibraryVersion
import it.tdlight.jni.TdApi

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.function.Consumer
import java.util.function.Supplier

/**
 *
 */
class MySimpleTelegramClient(settings: TDLibSettings) : Authenticable {

    companion object {
        // a static field that holds an instance of a Logger
        val LOG: Logger = LoggerFactory.getLogger(this::class.java)
        //
        var blockingExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        // Check whether TD Lib Native libraries are accessible
        init {
            try {
                Init.start()
            } catch (e: CantLoadLibrary) {
                throw RuntimeException("Can't load native libraries", e)
            }
        }
    }

// ___________________________________________________________________________

    /**
     * Request a new instance of `TelegramClient` from Client Manager
     * keep that `inner` client as a private field of `SimpleTelegramClient`
     * implementing Composite design pattern
     */
    private val client: TelegramClient =
        CommonClientManager.create(LibraryVersion.IMPLEMENTATION_NAME)

    /**
     * A field to hold TD Lib settings such as:
     * API token, file-based database directory path, etc.
     */
    private val settings: TDLibSettings

// ___________________________________________________________________________

    /**
     * Set up ScannerClientInteraction as default Client Interactor which will be called
     * whenever SimpleTelegramClient needs to block Main Thread to ask a user to input data
     */
    private var clientInteraction: ClientInteraction = MyScannerClientInteraction(
        blockingExecutor, this
    )

    /**
     * They Allow you to set your own `ClientInteraction` but this is useless as long
     * as you need to implement their interface `ClientInteraction` which allows only
     * predefined `InputParameter`s as enum class
     */
    fun setClientInteraction(clientInteraction: ClientInteraction) {
        this.clientInteraction = clientInteraction
    }

// ___________________________________________________________________________

    /**
     *
     */
    private var authenticationData: AuthenticationData? = null

    /**
     * Method of implemented `Authenticable` interface
     * When you explicitly call this method as `client.getAuthenticationData { ... }`
     * Main thread will be blocked, A console dialog will be popped up where th user
     * will be asked to type their phone number (or another authentication method)
     * that phone number will be saved in `AuthenticationData` object and passed into
     * consumer function
     */
    override fun getAuthenticationData(result: Consumer<AuthenticationData>) {
        if (authenticationData is ConsoleInteractiveAuthenticationData) {
            val consoleInteractiveAuthenticationData =
                authenticationData as ConsoleInteractiveAuthenticationData
            try {
                blockingExecutor.execute(Runnable {
                    consoleInteractiveAuthenticationData.askData()
                    result.accept(consoleInteractiveAuthenticationData)
                })
            } catch (ex: RejectedExecutionException) {
                LOG.error("Failed to execute askData. Returning an empty string", ex)
                result.accept(consoleInteractiveAuthenticationData)
            } catch (ex: NullPointerException) {
                LOG.error("Failed to execute askData. Returning an empty string", ex)
                result.accept(consoleInteractiveAuthenticationData)
            }
        } else {
            authenticationData?.let { result.accept(it) }
        }
    }

//______________ `TdApi.Close` related methods and fields ____________________

    /**
     *
     */
    private val closed = CountDownLatch(1)

//_____________________________ SET UP CONTAINERS FOR HANDLERS _______________

    /**
     *
     */
    private val commandHandlers: MutableMap<String, MutableSet<CommandHandler>> =
        ConcurrentHashMap()

    /**
     *
     */
    private val updateHandlers: MutableSet<ResultHandler<TdApi.Update>> =
        ConcurrentHashMap<ResultHandler<TdApi.Update>, Any>().keySet(Any())

    /**
     *
     */
    private val updateExceptionHandlers: MutableSet<ExceptionHandler> =
        ConcurrentHashMap<ExceptionHandler, Any>().keySet(Any())

    /**
     *
     */
    private val defaultExceptionHandlers: MutableSet<ExceptionHandler> =
        ConcurrentHashMap<ExceptionHandler, Any>().keySet(Any())

//_____________________________ END OF SET UP CONTAINERS FOR HANDLERS ________

//______________________ TdApi.GetMe related methods and fields ______________

    /**
     *
     */
    private var meGetter: MyAuthorizationStateReadyGetMe

    /**
     *
     */
    val me: TdApi.User
        get() = meGetter.getMe()

//______________ END OF TdApi.GetMe related methods and fields _______________

//______________ TdApi.GetChatList related methods and fields ________________

    /**
     *
     */
    private val mainChatsLoader: MyAuthorizationStateReadyLoadChats

    /**
     *
     */
    private val archivedChatsLoader: MyAuthorizationStateReadyLoadChats

    /**
     *
     */
    val isMainChatsListLoaded: Boolean
        get() = mainChatsLoader.isLoaded()

    /**
     *
     */
    val isArchivedChatsListLoaded: Boolean
        get() = archivedChatsLoader.isLoaded()

//______________ END OF TdApi.GetChatList related methods and fields _________

//_____________________________ START OF INIT BLOCK __________________________

    init {
        this.settings = settings

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitTdlibParametersHandler(
                client,
                settings,
                ::handleDefaultException
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitAuthenticationDataHandler(
                client,
                this,
                ::handleDefaultException
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitRegistrationHandler(
                client,
                InteractionWrapper(blockingExecutor, clientInteraction),
                ::handleDefaultException
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitPasswordHandler(
                client,
                InteractionWrapper(blockingExecutor, clientInteraction),
                ::handleDefaultException
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitOtherDeviceConfirmationHandler(
                InteractionWrapper(blockingExecutor, clientInteraction)
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateWaitCodeHandler(
                client,
                InteractionWrapper(blockingExecutor, clientInteraction),
                ::handleDefaultException
            )
        )

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java, MyAuthorizationStateWaitForExit(closed)
        )

        /**
         *
         */
        mainChatsLoader = MyAuthorizationStateReadyLoadChats(client, TdApi.ChatListMain())

        /**
         *
         */
        archivedChatsLoader = MyAuthorizationStateReadyLoadChats(client, TdApi.ChatListArchive())
        addUpdateHandler(
            TdApi.UpdateAuthorizationState::class.java,
            MyAuthorizationStateReadyGetMe(client, mainChatsLoader, archivedChatsLoader).also {
                meGetter = it
            })

        /**
         *
         */
        addUpdateHandler(
            TdApi.UpdateNewMessage::class.java,
            MyCommandsHandler(client, commandHandlers, Supplier { me })
        )
    }

//_____________________________ END OF INIT BLOCK ____________________________

// _________ Inner handlers passed on Client initialization __________________

    /**
     *
     */
    private fun handleUpdate(update: TdApi.Object) {
        var handled = false
        for (updateHandler in updateHandlers) {
            updateHandler.onResult(update)
            handled = true
        }
        if (!handled) {
            LOG.warn(
                "An update was not handled," +
                        " please use addUpdateHandler(handler) before starting the client!"
            )
        }
    }

    /**
     *
     */
    private fun handleUpdateException(ex: Throwable) {
        var handled = false
        for (updateExceptionHandler in updateExceptionHandlers) {
            updateExceptionHandler.onException(ex)
            handled = true
        }
        if (!handled) {
            LOG.warn("Error received from Telegram!", ex)
        }
    }

    /**
     *
     */
    private fun handleDefaultException(ex: Throwable) {
        var handled = false
        for (exceptionHandler in defaultExceptionHandlers) {
            exceptionHandler.onException(ex)
            handled = true
        }
        if (!handled) {
            LOG.warn("Unhandled exception!", ex)
        }
    }

    /**
     *
     */
    private fun handleResultHandlingException(ex: Throwable) {
        LOG.error("Failed to handle the request result", ex)
    }

// ______________ Methods for adding Handlers to Handler containers __________

    /**
     *
     */
    fun <T : TdApi.Update> addCommandHandler(commandName: String, handler: CommandHandler) {
        val handlers = commandHandlers.computeIfAbsent(commandName) { k: String? ->
            ConcurrentHashMap<CommandHandler, Any>().keySet(Any())
        }
        handlers.add(handler)
    }

    /**
     *
     */
    fun <T : TdApi.Update> addUpdateHandler(
        updateType: Class<T>,
        handler: GenericUpdateHandler<T>
    ) {
        val updateConstructor = ConstructorDetector.getConstructor(updateType)
        updateHandlers.add(ResultHandler { update: TdApi.Object ->
            if (update.constructor == updateConstructor) {
                handler.onUpdate(update as T)
            }
        })
    }

    /**
     *
     */
    fun addUpdatesHandler(handler: GenericUpdateHandler<TdApi.Update>) {
        updateHandlers.add(ResultHandler<TdApi.Update> { update: TdApi.Object? ->
            if (update is TdApi.Update) {
                handler.onUpdate(update)
            } else {
                LOG.warn("Unknown update type: {}", update)
            }
        })
    }

    /**
     * Optional handler to handle errors received from TDLib
     */
    fun addUpdateExceptionHandler(updateExceptionHandler: ExceptionHandler) {
        updateExceptionHandlers.add(updateExceptionHandler)
    }

    /**
     * Optional handler to handle uncaught errors
     * (when using send without an appropriate error handler)
     */
    fun addDefaultExceptionHandler(defaultExceptionHandlers: ExceptionHandler) {
        this.defaultExceptionHandlers.add(defaultExceptionHandlers)
    }

// ___________________________________________________________________________

    /**
     * Start the client
     */
    fun start(authenticationData: AuthenticationData) {
        this.authenticationData = authenticationData
        createDirectories()
        client.initialize(
            { update: TdApi.Object -> handleUpdate(update) },
            { ex: Throwable -> handleUpdateException(ex) },
            { ex: Throwable -> handleDefaultException(ex) }
        )
    }

    /**
     * Send a function and get the result
     */
    fun <R : TdApi.Object?> send(
        function: TdApi.Function<R>,
        resultHandler: GenericResultHandler<R>
    ) {
        client.send(function, { result: TdApi.Object? ->
            resultHandler.onResult(Result.of(result))
        }, ::handleResultHandlingException
        )
    }

    /**
     * Execute a synchronous function.
     * **Please note that only some functions can be executed using this method.**
     * If you want to execute a function please use [.send]!
     */
    fun <R : TdApi.Object?> execute(function: TdApi.Function<R>): Result<R> {
        return Result.of(client.execute(function))
    }

//______________ `TdApi.Close` related methods and fields ____________________

    /**
     * Send the close signal but don't wait
     */
    fun sendClose() {
        client.send(TdApi.Close(), MyDefaultResultHandler)
    }

    /**
     * Send the close signal and wait for exit
     */
    fun closeAndWait() {
        client.send(TdApi.Close(), MyDefaultResultHandler)
        waitForExit()
    }

    /**
     * Wait until TDLight is closed
     */
    fun waitForExit() {
        closed.await()
    }

//______________ Helper methods ______________________________________________

    /**
     *
     */
    private fun createDirectories() {
        try {
            if (Files.notExists(settings.databaseDirectoryPath)) {
                Files.createDirectories(settings.databaseDirectoryPath)
            }
            if (Files.notExists(settings.downloadedFilesDirectoryPath)) {
                Files.createDirectories(settings.downloadedFilesDirectoryPath)
            }
        } catch (ex: IOException) {
            throw UncheckedIOException("Can't create TDLight directories", ex)
        }
    }

}
