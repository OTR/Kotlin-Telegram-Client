package com.github.otr.console_client.presentation.viewmodel

import com.github.otr.console_client.domain.entity.chat.Roster

/**
 * Each class that returns a `Roster` object should implement this interface
 */
interface RosterOwner {

    fun getRoster(): Roster

}
