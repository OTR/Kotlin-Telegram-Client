package com.github.otr.simple_client.handler.auth

import com.github.otr.simple_client.handler.MyDefaultResultHandler

import it.tdlight.client.ClientInteraction
import it.tdlight.client.EmptyParameterInfo
import it.tdlight.client.GenericUpdateHandler
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfoTermsOfService
import it.tdlight.common.ExceptionHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi

/**
 * When TD Lib sends you `Wait Registration` State we handle this state:
 * block Main Thread, show Terms of Service to the user, ask them for the first name
 * and the last name, and then call `TdApi.RegisterUser` Function
 */
internal class MyAuthorizationStateWaitRegistrationHandler(
    private val client: TelegramClient,
    private val clientInteraction: ClientInteraction,
    private val exceptionHandler: ExceptionHandler
) : GenericUpdateHandler<TdApi.UpdateAuthorizationState> {

    /**
     * We subscribe to all updates about Authorization State
     */
    override fun onUpdate(update: TdApi.UpdateAuthorizationState) {

        // And then check if this State is Wait Registration State
        val authorizationState = update.authorizationState
        if (authorizationState is TdApi.AuthorizationStateWaitRegistration) {

            // Get `Terms Of Service` and show them to the User
            val tos = ParameterInfoTermsOfService(authorizationState.termsOfService)
            clientInteraction.onParameterRequest(
                InputParameter.TERMS_OF_SERVICE, tos
            ) { ignored: String? ->

                // Ask the user for the first name
                clientInteraction.onParameterRequest(
                    InputParameter.ASK_FIRST_NAME,
                    EmptyParameterInfo()
                ) { firstName: String? ->

                    // Ask the user for the last name
                    clientInteraction.onParameterRequest(
                        InputParameter.ASK_LAST_NAME,
                        EmptyParameterInfo()
                    ) { lastName: String? ->

                        // Check user input
                        if (firstName == null || firstName.isEmpty()) {
                            exceptionHandler.onException(
                                IllegalArgumentException("First name must not be null or empty")
                            )
                            return@onParameterRequest
                        }
                        if (firstName.length > 64) {
                            exceptionHandler.onException(
                                IllegalArgumentException("First name must be under 64 characters")
                            )
                            return@onParameterRequest
                        }
                        if (lastName == null) {
                            exceptionHandler.onException(
                                IllegalArgumentException("Last name must not be null")
                            )
                            return@onParameterRequest
                        }
                        if (lastName.length > 64) {
                            exceptionHandler.onException(
                                IllegalArgumentException("Last name must be under 64 characters")
                            )
                            return@onParameterRequest
                        }

                        // Send `TdApi.RegisterUser` Function
                        val response = TdApi.RegisterUser(firstName, lastName)
                        client.send(response, MyDefaultResultHandler, exceptionHandler)
                    }
                }
            }
        }
    }

}
