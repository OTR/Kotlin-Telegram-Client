package com.github.otr.telegram.presentation.component

import android.widget.ToggleButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.otr.telegram.R
import com.github.otr.telegram.navigation.NavigationItem
import kotlin.random.Random

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
        val items = listOf<NavigationItem>(
            NavigationItem.ChatList,
            NavigationItem.Settings,
            NavigationItem.About,
            NavigationItem.LogOut
        )

        items.forEach {
            NavigationRailItem(
                selected = Random.nextBoolean(),
                onClick = { /*TODO*/ },
                icon = { Icon(imageVector = it.icon, contentDescription = it.route) },
                label = { Text(text = stringResource(id = it.stringResourceId)) }
            )
        }
    }
}
