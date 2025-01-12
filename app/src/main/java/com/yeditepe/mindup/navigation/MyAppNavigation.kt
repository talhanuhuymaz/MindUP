package com.yeditepe.mindup.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.yeditepe.mindup.screens.HomePage
import com.yeditepe.mindup.screens.LoginPage
import com.yeditepe.mindup.screens.ProfilePage
import com.yeditepe.mindup.screens.SettingsPage
import com.yeditepe.mindup.screens.SignupPage
import com.yeditepe.mindup.screens.StatsPage
import com.yeditepe.mindup.screens.MotivationPage
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    moodViewModel: MoodViewModel,
    drawerState: DrawerState
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val items = listOf(
        NavigationItem("Home", "home", Icons.Default.Home),
        NavigationItem("Profile", "profile", Icons.Default.Person),
        NavigationItem("Stats", "stats", Icons.Default.Analytics),
        NavigationItem("Settings", "settings", Icons.Default.Settings),
        NavigationItem("Motivation", "motivation", Icons.Default.Star)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(item.route)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Log Out") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            authViewModel.signOut()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginPage(modifier, navController, authViewModel)
            }
            composable("signup") {
                SignupPage(modifier, navController, authViewModel)
            }
            composable("home") {
                HomePage(
                    modifier = modifier,
                    navController = navController,
                    authViewModel = authViewModel,
                    moodViewModel = moodViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable("stats") {
                StatsPage(modifier, moodViewModel, navController)
            }
            composable("profile") {
                ProfilePage(
                    modifier = modifier,
                    navController = navController,
                    authViewModel = authViewModel,
                    moodViewModel = moodViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable("settings") {
                SettingsPage(
                    modifier = modifier,
                    navController = navController,
                    authViewModel = authViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable("motivation") {
                MotivationPage(
                    modifier = modifier,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}