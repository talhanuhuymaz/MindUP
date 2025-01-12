package com.yeditepe.mindup.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.yeditepe.mindup.screens.HomePage
import com.yeditepe.mindup.screens.LoginPage
import com.yeditepe.mindup.screens.ProfilePage
import com.yeditepe.mindup.screens.SettingsPage
import com.yeditepe.mindup.screens.SignupPage
import com.yeditepe.mindup.screens.StatsPage
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel
import kotlinx.coroutines.launch

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("home")
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                    label = { Text("Statistics") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("stats")
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("profile")
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("settings")
                        }
                    }
                )
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
        }
    }
}