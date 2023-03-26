package com.github.otr.telegram.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.otr.telegram.R

sealed class NavigationItem(
    val icon: ImageVector,
    val stringResourceId: Int,
    val route: String
) {

    object ChatList: NavigationItem(
        icon = Icons.Outlined.MailOutline,
        stringResourceId = R.string.chat_list_label,
        route = "chat_list"
    )

    object Settings: NavigationItem(
        icon = Icons.Outlined.Settings,
        stringResourceId = R.string.settings_label,
        route = "settings"
    )

    object About: NavigationItem(
        icon = Icons.Outlined.Build,
        stringResourceId = R.string.about_label,
        route = "about"
    )

    object LogOut: NavigationItem(
        icon = Icons.Outlined.ExitToApp,
        stringResourceId = R.string.log_out_label,
        route = "logout"
    )

}
