package com.github.otr.telegram.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.github.otr.telegram.domain.entity.Chat
import com.github.otr.telegram.presentation.component.ChatCard
import com.github.otr.telegram.presentation.viewmodel.MainViewModel

private val defaultPadding: Dp = 16.dp

@Composable
fun ChatListScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {

    val chatListState: State<List<Chat>> = viewModel.chatListLiveData.observeAsState(listOf())
    val onFavoriteStatusClickListener: (Chat) -> Unit = viewModel::changeFavoriteStatus

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(defaultPadding),
            modifier = Modifier
                .padding(defaultPadding)
                .fillMaxSize()
        ) {
            items(items = chatListState.value, key = { it.id }) { chatItem ->

                ChatCard(chatItem = chatItem, onFavoriteStatusClickListener)

            }
        }
    }
}
