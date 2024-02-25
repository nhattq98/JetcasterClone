package com.example.clonejetcaster.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clonejetcaster.R
import com.example.clonejetcaster.ui.home.Home

@Composable
fun JetcasterApp(
    appState: JetcasterAppState = rememberJetcasterAppState(),
) {
    if (true) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { backStackEntry ->
                Home(
                    navigateToPlayer = { episodeUri ->
                    }
                )
            }
        }
    } else {
        OfflineDialog { }
    }
}

@Composable
fun OfflineDialog(onRetry: (() -> Unit)? = null) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry ?: return@AlertDialog) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}