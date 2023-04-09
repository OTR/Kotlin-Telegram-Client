package com.github.otr.simple_client.handler.auth

import com.github.otr.simple_client.handler.MyDefaultResultHandler

import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.TDLibSettings
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

/**
 *
 */
internal class MyAuthorizationStateWaitTdlibParametersHandler(
    private val client: TelegramClient,
    private val settings: TDLibSettings,
    private val exceptionHandler: ExceptionHandler
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     *
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {
        if (update.authorizationState is TdApi.AuthorizationStateWaitTdlibParameters) {

            // Compose a new TdLibParameters object
            val params = TdApi.SetTdlibParameters().apply {
                useTestDc = settings.isUsingTestDatacenter
                databaseDirectory = settings.databaseDirectoryPath.toString()
                filesDirectory = settings.downloadedFilesDirectoryPath.toString()
                useFileDatabase = settings.isFileDatabaseEnabled
                useChatInfoDatabase = settings.isChatInfoDatabaseEnabled
                useMessageDatabase = settings.isMessageDatabaseEnabled
                useSecretChats = false
                apiId = settings.apiToken.apiID
                apiHash = settings.apiToken.apiHash
                systemLanguageCode = settings.systemLanguageCode
                deviceModel = settings.deviceModel
                systemVersion = settings.systemVersion
                applicationVersion = settings.applicationVersion
                enableStorageOptimizer = settings.isStorageOptimizerEnabled
                ignoreFileNames = settings.isIgnoreFileNames
                databaseEncryptionKey = null
            }

            client.send(params, MyDefaultResultHandler, exceptionHandler)
        }
    }

}
