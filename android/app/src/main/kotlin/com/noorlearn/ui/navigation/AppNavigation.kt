package com.noorlearn.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.noorlearn.ui.screens.*
import com.noorlearn.ui.theme.BeigeBackground
import com.noorlearn.ui.theme.PrimaryGreen
import com.noorlearn.ui.theme.GrayText

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("dashboard", "Home", Icons.Filled.Home)
    object Quran : Screen("surah_list", "Qur'an", Icons.AutoMirrored.Filled.MenuBook)
    object Journey : Screen("daily_journey", "Journey", Icons.Filled.Explore)
    object Tools : Screen("tools", "Tools", Icons.Filled.Handyman)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

val items = listOf(
    Screen.Home,
    Screen.Quran,
    Screen.Journey,
    Screen.Tools,
    Screen.Profile
)

// Routes that should show the bottom bar
private val bottomBarRoutes = items.map { it.route }.toSet()

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val user by authViewModel.user.collectAsState()

    val isBottomBarRoute = currentRoute != null && (
        currentRoute == "dashboard" ||
        currentRoute == "surah_list" ||
        currentRoute == "daily_journey" ||
        currentRoute.startsWith("tools") ||
        currentRoute == "profile"
    )

    Scaffold(
        containerColor = BeigeBackground,
        bottomBar = {
            // Only show bottom bar on main tabs
            if (isBottomBarRoute) {
                NavigationBar(
                    containerColor = BeigeBackground,
                    contentColor = PrimaryGreen
                ) {
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { 
                                val baseRoute = it.route?.substringBefore("?") ?: ""
                                val screenBaseRoute = screen.route.substringBefore("?")
                                baseRoute == screenBaseRoute 
                            } == true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                indicatorColor = PrimaryGreen.copy(alpha = 0.2f),
                                unselectedIconColor = GrayText,
                                unselectedTextColor = GrayText
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        
        // Show loading indicator until session check completes
        if (user == null && authViewModel.isLoading.collectAsState().value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
            return@Scaffold
        }

        NavHost(
            navController = navController,
            startDestination = if (user != null) Screen.Home.route else "auth",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("auth") {
                AuthScreen(navController = navController)
            }
            composable("onboarding") {
                OnboardingScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                DashboardScreen(navController = navController)
            }
            composable(Screen.Quran.route) {
                SurahListScreen(navController = navController)
            }
            composable(Screen.Journey.route) {
                DailyJourneyScreen(navController = navController)
            }
            composable("chatbot") {
                ChatbotScreen(navController = navController)
            }
            composable(
                route = "tools?tab={tab}",
                arguments = listOf(
                    navArgument("tab") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val tab = backStackEntry.arguments?.getInt("tab") ?: 0
                ToolsScreen(navController = navController, initialTab = tab)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable("hadith_hub") {
                HadithHubScreen(navController = navController)
            }
            composable("prophet_stories") {
                ProphetStoriesScreen(navController = navController)
            }
            composable(
                route = "ayah_reader/{surahId}/{surahName}",
                arguments = listOf(
                    navArgument("surahId") { type = NavType.IntType },
                    navArgument("surahName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val surahId = backStackEntry.arguments?.getInt("surahId") ?: 1
                val surahName = backStackEntry.arguments?.getString("surahName") ?: ""
                AyahReaderScreen(
                    navController = navController,
                    surahId = surahId,
                    surahName = surahName
                )
            }
            composable("qaida") {
                QaidaScreen(navController = navController)
            }
            composable("bookmarks") {
                BookmarksScreen(navController = navController)
            }
            composable("reflection_journal") {
                ReflectionJournalScreen(navController = navController)
            }
            composable("vocabulary_builder") {
                VocabularyBuilderScreen(navController = navController)
            }
            composable("recitation_history") {
                RecitationHistoryScreen(navController = navController)
            }
        }
    }
}
