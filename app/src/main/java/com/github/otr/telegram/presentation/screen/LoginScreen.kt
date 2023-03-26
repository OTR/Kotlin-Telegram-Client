package com.github.otr.telegram.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.github.otr.telegram.R

@Preview
@Composable
fun LoginScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_application_logo),
                contentDescription = "Application Logo",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = stringResource(R.string.login_screen_input_number_label),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.padding(16.dp))
            TextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(text = stringResource(R.string.login_screen_phone_placeholder))
                }
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = { }) {
                Text(text = stringResource(R.string.login_screen_submit_button))
            }
        }
    }
}
