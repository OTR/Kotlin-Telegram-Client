package com.github.otr.telegram.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.otr.telegram.domain.entity.Chat

private val defaultPadding: Dp = 16.dp

@Preview
@Composable
fun ChatListScreen(
    chatRoster: List<Chat> = listOf(
        Chat(1L, "Andrew"),
        Chat(2L, "Bob"),
        Chat(3L, "Charly"),
        Chat(4L, "Danny"),
    )
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(defaultPadding),
            modifier = Modifier
                .padding(defaultPadding)
                .fillMaxSize()
        ) {
            items(items = chatRoster, key = { it.id }) { chatItem ->
                Card(
                    elevation = 8.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colors.onBackground
                    ),
                    modifier = Modifier.fillMaxSize()
                )
                {
                    Text(text = "Chat with ${chatItem.name}")
                }
            }
        }
    }
}
