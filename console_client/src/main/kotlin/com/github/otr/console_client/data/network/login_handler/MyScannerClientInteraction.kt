package com.github.otr.console_client.data.network.login_handler

import it.tdlight.client.Authenticable
import it.tdlight.client.AuthenticationData
import it.tdlight.client.ClientInteraction
import it.tdlight.client.ConsoleInteractiveAuthenticationData
import it.tdlight.client.InputParameter
import it.tdlight.client.ParameterInfo
import it.tdlight.client.ParameterInfoCode
import it.tdlight.client.ParameterInfoNotifyLink
import it.tdlight.client.ParameterInfoPasswordHint
import it.tdlight.client.ParameterInfoTermsOfService
import it.tdlight.client.QrCodeTerminal
import it.tdlight.common.utils.ScannerUtils

import java.util.concurrent.ExecutorService
import java.util.function.Consumer

/**
 * @see it.tdlight.client.ScannerClientInteraction
 */
internal class MyScannerClientInteraction(
    private val blockingExecutor: ExecutorService,
    private val authenticable: Authenticable
) : ClientInteraction {

    /**
     *
     */
    override fun onParameterRequest(
        parameter: InputParameter,
        parameterInfo: ParameterInfo,
        resultCons: Consumer<String>
    ) {
        authenticable.getAuthenticationData { authenticationData: AuthenticationData ->
            blockingExecutor.execute {

                //
                val useRealWho: Boolean = when (authenticationData) {
                    is ConsoleInteractiveAuthenticationData -> authenticationData.isInitialized
                    else -> true
                }

                //
                val who: String = when {
                    !useRealWho -> "login"
                    authenticationData.isQrCode -> "QR login"
                    authenticationData.isBot -> authenticationData.botToken
                        .split(":".toRegex(), limit = 2).toTypedArray()[0]
                    else -> "+" + authenticationData.userPhoneNumber
                }

                //
                var question: String
                var trim = false

                // Biggest When Block you've ever seen
                when (parameter) {
                    InputParameter.ASK_FIRST_NAME -> {
                        question = "Enter first name"
                        trim = true
                    }
                    InputParameter.ASK_LAST_NAME -> {
                        question = "Enter last name"
                        trim = true
                    }
                    InputParameter.ASK_CODE -> {
                        question = buildQuestionToAskCode(parameterInfo)
                        trim = true
                    }
                    InputParameter.ASK_PASSWORD -> {
                        question = "Enter your password"
                        val passwordMessage = buildPasswordMessage(parameterInfo)
                        println(passwordMessage)
                    }
                    InputParameter.NOTIFY_LINK -> {
                        val link = (parameterInfo as ParameterInfoNotifyLink).link
                        println(
                            "Please confirm this login link on another device: $link\n" +
                                    QrCodeTerminal.getQr(link) + "\n"
                        )
                        resultCons.accept("")
                        return@execute
                    }
                    InputParameter.TERMS_OF_SERVICE -> {
                        val tos = (parameterInfo as ParameterInfoTermsOfService).termsOfService
                        question = "Terms of service: \n${tos.text.text}"
                        if (tos.minUserAge > 0) {
                            question += "\nMinimum user age: ${tos.minUserAge}"
                        }
                        if (tos.showPopup) {
                            question += "\nPlease press enter."
                            trim = true
                        } else {
                            println(question)
                            resultCons.accept("")
                            return@execute
                        }
                    }
                    else -> question = parameter.toString()
                }

                // Actual user input is been taken here
                val result = ScannerUtils.askParameter(who, question)

                // Provide the taken user input to result Consumer
                if (trim) {
                    resultCons.accept(result.trim { it <= ' ' })
                } else {
                    resultCons.accept(result)
                }
            }
        }
    }

    /**
     *
     */
    private fun buildPasswordMessage(parameterInfo: ParameterInfo): String {

        var passwordMessage = "Password authorization:"
        val hint = (parameterInfo as ParameterInfoPasswordHint).hint
        if (hint != null && hint.isNotEmpty()) {
            passwordMessage += "\n\tHint: $hint"
        }
        val hasRecoveryEmailAddress = parameterInfo.hasRecoveryEmailAddress()
        passwordMessage += "\n\tHas recovery email: $hasRecoveryEmailAddress"
        val recoveryEmailAddressPattern = parameterInfo.recoveryEmailAddressPattern
        if (recoveryEmailAddressPattern != null && recoveryEmailAddressPattern.isNotEmpty()) {
            passwordMessage += "\n\tRecovery email address pattern: $recoveryEmailAddressPattern"
        }

        return passwordMessage
    }

    /**
     *
     */
    private fun buildQuestionToAskCode(parameterInfo: ParameterInfo): String {

        var _question = "\nEnter authentication code, PLEASE:"
        val codeInfo = parameterInfo as ParameterInfoCode
        val codeTypeName: String = codeInfo.type.javaClass.simpleName
            .replace("AuthenticationCodeType", "")
        _question += "\nPhone number: ${codeInfo.phoneNumber}"
        _question += "\nTimeout: ${codeInfo.timeout} seconds"
        _question += "\n\tCode type: $codeTypeName"
        if (codeInfo.nextType != null) {
            val nextCodeTypeName: String = codeInfo.nextType.javaClass.simpleName
                .replace("AuthenticationCodeType", "")
            _question += "\n\tNext code type: $nextCodeTypeName"
        }
        return _question
    }

}
