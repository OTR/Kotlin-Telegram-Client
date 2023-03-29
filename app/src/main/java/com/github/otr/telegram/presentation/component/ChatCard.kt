package com.github.otr.telegram.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.github.otr.telegram.domain.entity.Chat

private val defaultPadding: Dp = 16.dp

@Composable
fun ChatCard(
    chatItem: Chat,
    onFavoriteButtonClickListener: (Chat) -> Unit
) {

    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground),
        modifier = Modifier.fillMaxSize()
    )
    {
        Column {

            CardBody(chatItem = chatItem, onFavoriteButtonClickListener )

        }
    }

}

@Composable
private fun CardBody(
    chatItem: Chat,
    onFavoriteButtonClickListener: (Chat) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = defaultPadding / 2)
    ) {
        Text(
            text = "Chat with ${chatItem.name}",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(1f)
        )
        FavoriteButton(isFavorite = chatItem.favorite) {
            onFavoriteButtonClickListener(chatItem)
        }
    }

}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClickListener: () -> Unit
) {

    TextButton(
        onClick = onClickListener::invoke,
        modifier = Modifier.height(30.dp)
    ) {
        val text = if (isFavorite) "is Favorite" else "Make Favorite"
        Text(text = text, fontSize = 10.sp)
    }

}
