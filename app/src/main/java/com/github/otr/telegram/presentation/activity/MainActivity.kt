package com.github.otr.telegram.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.otr.telegram.presentation.screen.MainScreen
import com.github.otr.telegram.ui.theme.KotlinTelegramClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinTelegramClientTheme {
                MainScreen()
            }
        }
    }
}
