package com.creator_surge.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.creator_surge.ui.components.PromptScreen

@Composable
fun CreatorSurgeApp() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        PromptScreen()
    }
}
