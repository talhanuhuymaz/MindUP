package com.yeditepe.mindup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeditepe.mindup.screens.HomePage
import com.yeditepe.mindup.screens.LoginPage
import com.yeditepe.mindup.screens.SignupPage
import com.yeditepe.mindup.screens.StatsPage
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    moodViewModel: MoodViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel, moodViewModel)
        }
        composable("stats") {
            StatsPage(modifier, moodViewModel, navController)
        }
    })
}