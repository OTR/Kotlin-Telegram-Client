package com.github.otr.telegram.presentation.screen

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

import com.github.otr.telegram.presentation.component.SideDrawer
import com.github.otr.telegram.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    Scaffold(
        drawerContent = { SideDrawer() }
    ) { paddingValues ->

        ChatListScreen(viewModel = viewModel, paddingValues = paddingValues)

    }

}
