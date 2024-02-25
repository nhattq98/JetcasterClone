package com.example.clonejetcaster.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Player : Screen("player/episodeUri") {
        fun createRoute(episodeUri: String) = "player/$episodeUri"
    }
}

@Composable
fun rememberJetcasterAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) = remember(navController, context) {
    JetcasterAppState(navController, context)
}

class JetcasterAppState(
    val navController: NavHostController,
    private val context: Context,
) {
    fun navigateBack() {
        navController.popBackStack()
    }
}