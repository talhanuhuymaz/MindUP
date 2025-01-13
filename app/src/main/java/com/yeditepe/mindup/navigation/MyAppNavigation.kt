package com.yeditepe.mindup.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        NavigationItem("Motivation", "motivation", Icons.Default.Star),
        NavigationItem("Settings", "settings", Icons.Default.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "MindUP",
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { 
                            Icon(
                                item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ) 
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(item.route)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = Color.Red.copy(alpha = 0.8f)
                        ) 
                    },
                    label = { 
                        Text(
                            "Log Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red.copy(alpha = 0.8f)
                        ) 
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            authViewModel.signOut()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginPage(navController, authViewModel)
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
                StatsPage(
                    modifier = modifier,
                    moodViewModel = moodViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable("profile") {
                ProfilePage(
                    modifier = modifier,
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