package com.yeditepe.mindup.screens


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel


@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    moodViewModel: MoodViewModel,
    onMenuClick: () -> Unit
) {
    val moodEntries = moodViewModel.moodEntries.observeAsState(initial = emptyList())
    val currentUser = authViewModel.getCurrentUser()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Content will be added here
    }
} 