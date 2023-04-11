package com.github.otr.simple_client.helper

/**
 *
 */
class AskLoadingUnreadMessages(
    private val msgAboutUnreadMessages: String
): UserInputScanner(
    parameter = "yes/no",
    question = "$msgAboutUnreadMessages Do you want to load them?"
)
