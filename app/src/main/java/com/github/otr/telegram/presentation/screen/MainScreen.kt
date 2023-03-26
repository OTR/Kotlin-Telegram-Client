package com.github.otr.telegram.presentation.screen

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.otr.telegram.presentation.component.SideDrawer

@Preview
@Composable
fun MainScreen() {

    Scaffold(
        drawerContent = { SideDrawer() }
    ) { paddingValues ->

        ChatListScreen(paddingValues = paddingValues)

    }
}
