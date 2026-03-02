package com.noorlearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noorlearn.ui.screens.DashboardScreen
import com.noorlearn.ui.screens.ChatbotScreen
import com.noorlearn.ui.screens.SurahListScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        composable("chatbot") {
            ChatbotScreen(navController = navController)
        }
        composable("surah_list") {
            SurahListScreen(navController = navController)
        }
        composable("hadith_hub") {
            com.noorlearn.ui.screens.HadithHubScreen(navController = navController)
        }
        composable("prophet_stories") {
            com.noorlearn.ui.screens.ProphetStoriesScreen(navController = navController)
        }
    }
}
