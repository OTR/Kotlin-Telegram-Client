package com.github.otr.telegram.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.github.otr.telegram.R

private val defaultPadding: Dp = 16.dp

@Preview
@Composable
fun VerificationCodeScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(defaultPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.verification_code),
                contentDescription = "Application Logo",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(defaultPadding))
            Text(
                text = stringResource(R.string.verification_code_screen_help_text),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(defaultPadding))
            TextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(text = stringResource(id = R.string.verification_code_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.verification_code_screen_placeholder))
                }
            )
            Spacer(modifier = Modifier.height(defaultPadding))
            Button(onClick = { }) {
                Text(text = stringResource(R.string.submit_button))
            }
        }
    }

}
