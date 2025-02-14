package com.example.locale.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.locale.ui.theme.LocaleTheme

@Composable
fun LocaleApp() {
    LocaleTheme {
        LocaleNavHolder(
            modifier = Modifier.fillMaxSize()
        )
    }
}