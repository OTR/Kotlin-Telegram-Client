package com.github.otr.telegram.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.otr.telegram.R

private val defaultPadding: Dp = 16.dp

/**
 * Remember that content of the Composable below already renders inside a Column Scope
 */
@Composable
fun SideDrawer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.settings_label))
        }

        TextButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.about_label))
        }

        TextButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.log_out_label))
        }
    }

}


