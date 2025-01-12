package com.yeditepe.mindup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel
import com.yeditepe.mindup.R
import java.time.format.DateTimeFormatter
import com.yeditepe.mindup.components.PageHeader

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
        PageHeader(
            title = "Profile",
            onMenuClick = onMenuClick
        )

        // Profil Fotoğrafı ve Bilgiler
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(60.dp),
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentUser?.email ?: "Not signed in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Total Entries: ${moodEntries.value.size}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        // Mood Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Mood Summary",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mood istatistikleri
                val moodStats = moodEntries.value
                    .groupBy { it.mood }
                    .mapValues { it.value.size }
                    .toList()
                    .sortedByDescending { it.second }

                moodStats.forEach { (mood, count) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = mood)
                        Text(
                            text = count.toString(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Recent Activity
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Recent Activity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                moodEntries.value.take(3).forEach { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = entry.mood)
                        Text(
                            text = entry.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}