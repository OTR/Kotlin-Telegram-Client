package com.github.otr.telegram.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.github.otr.telegram.navigation.NavigationItem

private val defaultPadding: Dp = 16.dp

/**
 * Remember that content of the Composable below already renders inside a Column Scope
 */
@Preview
@Composable
fun SideDrawer() {

    NavigationRail(
        modifier = Modifier.fillMaxWidth()
    ){

        val selectedItemPosition = remember { mutableStateOf(0) }

        val items = listOf<NavigationItem>(
            NavigationItem.ChatList,
            NavigationItem.Settings,
            NavigationItem.About,
            NavigationItem.LogOut
        )

        items.forEachIndexed { index, item ->
            NavigationRailItem(
                selected = selectedItemPosition.value == index,
                onClick = { selectedItemPosition.value = index },
                icon = { Icon(imageVector = item.icon, contentDescription = item.route) },
                label = { Text(text = stringResource(id = item.stringResourceId)) }
            )
        }
    }

}
