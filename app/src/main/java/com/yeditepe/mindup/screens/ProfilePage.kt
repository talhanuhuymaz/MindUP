package com.yeditepe.mindup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocalFireDepartment
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
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodViewModel
import com.yeditepe.mindup.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.yeditepe.mindup.model.MoodEntry
import androidx.compose.ui.draw.shadow
import com.yeditepe.mindup.components.PageHeader
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip

private val avatarList = listOf(
    R.drawable.fox,
    R.drawable.cat,
    R.drawable.batman,
    R.drawable.joker,

)

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    moodViewModel: MoodViewModel,
    onMenuClick: () -> Unit
) {
    val moodEntries = moodViewModel.moodEntries.observeAsState(initial = emptyList())
    val currentUser = authViewModel.getCurrentUser()
    val scrollState = rememberScrollState()
    var showAvatarDialog by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableStateOf(R.drawable.fox) }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = selectedAvatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { showAvatarDialog = true }
                    )
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
                    .padding(vertical = 4.dp),
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


                    val moodStats = moodEntries.value
                        .groupBy { it.mood }
                        .mapValues { it.value.size }

                    val totalEntries = moodEntries.value.size.toFloat()

                    moodStats.forEach { (mood, count) ->
                        val percentage = if (totalEntries > 0) {
                            (count / totalEntries * 100).toInt()
                        } else {
                            0
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(getMoodIconResource(mood)),
                                    contentDescription = mood,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = " $mood",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Text(
                                text = "$count ($percentage%)",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Recent Activity
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
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

            // Streak Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color(0xFF000000).copy(alpha = 0.1f)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Daily Streak
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Daily Streak",
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "${calculateDailyStreak(moodEntries.value)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Daily",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        )
                        Text(
                            text = "Streak",
                            fontSize = 16.sp,
                            color = Color(0xFF757575)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Mood Streak
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color(0xFFF3E5F5),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        val currentMoodStreak = calculateMoodStreakWithMood(moodEntries.value)
                        Image(
                            painter = painterResource(getMoodIconResource(currentMoodStreak.first)),
                            contentDescription = "Mood Streak",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "${currentMoodStreak.second}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Same Mood",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        )
                        Text(
                            text = "Streak",
                            fontSize = 16.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }
            }
        }

        if (showAvatarDialog) {
            AlertDialog(
                onDismissRequest = { showAvatarDialog = false },
                title = { Text("Choose Avatar") },
                text = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(avatarList) { avatar ->
                            Image(
                                painter = painterResource(id = avatar),
                                contentDescription = "Avatar Option",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        selectedAvatar = avatar
                                        showAvatarDialog = false
                                    }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAvatarDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

private fun getMoodIconResource(mood: String): Int {
    return when (mood.lowercase()) {
        "happy" -> R.drawable.happy
        "sad" -> R.drawable.sad
        "calm" -> R.drawable.relaxed
        "anxious" -> R.drawable.anxious
        "angry" -> R.drawable.angry
        else -> R.drawable.happy
    }
}

private fun calculateDailyStreak(entries: List<MoodEntry>): Int {
    if (entries.isEmpty()) return 0

    var streak = 1
    var currentDate = LocalDate.now()
    val sortedEntries = entries.sortedByDescending { it.timestamp }

    // Bugün giriş var mı kontrol et
    if (sortedEntries.none { it.timestamp.toLocalDate() == currentDate }) {
        currentDate = currentDate.minusDays(1)
        // Dün de giriş yoksa streak 0
        if (sortedEntries.none { it.timestamp.toLocalDate() == currentDate }) {
            return 0
        }
    }

    for (i in 1 until sortedEntries.size) {
        val entryDate = sortedEntries[i].timestamp.toLocalDate()
        if (entryDate == currentDate.minusDays(1)) {
            streak++
            currentDate = entryDate
        } else {
            break
        }
    }
    return streak
}

private fun calculateMoodStreakWithMood(entries: List<MoodEntry>): Pair<String, Int> {
    if (entries.isEmpty()) return Pair("happy", 0)

    var streak = 1
    val sortedEntries = entries.sortedByDescending { it.timestamp }
    val currentMood = sortedEntries.first().mood

    for (i in 1 until sortedEntries.size) {
        if (sortedEntries[i].mood == currentMood) {
            streak++
        } else {
            break
        }
    }
    return Pair(currentMood, streak)
}