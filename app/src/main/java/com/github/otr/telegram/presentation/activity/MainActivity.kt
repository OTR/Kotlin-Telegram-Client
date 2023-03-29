package com.github.otr.telegram.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

import com.github.otr.telegram.presentation.screen.MainScreen
import com.github.otr.telegram.presentation.viewmodel.MainViewModel
import com.github.otr.telegram.ui.theme.KotlinTelegramClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        val viewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //
        setContent {
            KotlinTelegramClientTheme {
                MainScreen(viewModel)
            }
        }
    }
}
