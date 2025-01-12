package com.yeditepe.mindup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yeditepe.mindup.viewmodel.MoodViewModel
import com.yeditepe.mindup.model.MoodEntry
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.yeditepe.mindup.R
import com.yeditepe.mindup.components.PageHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPage(
    modifier: Modifier = Modifier,
    moodViewModel: MoodViewModel,
    navController: NavController,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PageHeader(
            title = "Statistics",
            onMenuClick = onMenuClick
        )
        
        val moodEntries = moodViewModel.moodEntries.observeAsState(initial = emptyList())
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .verticalScroll(scrollState)
        ) {
            // Top App Bar
            TopAppBar(
                title = { Text("Mood Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Dominant Mood Card
                val dominantMood = moodEntries.value
                    .groupBy { it.mood }
                    .maxByOrNull { it.value.size }
                    ?.key ?: "No data"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dominant Mood",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = dominantMood,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Image(
                            painter = painterResource(getMoodIconResource(dominantMood)),
                            contentDescription = dominantMood,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Weekly Patterns Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Weekly Patterns",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        WeeklyMoodDistribution(moodEntries.value)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val stats = moodViewModel.getMoodStats()
                        stats.forEach { (mood, count) ->
                            val percentage = (count.toFloat() / moodEntries.value.size * 100).toInt()
                            MoodDistributionItem(
                                mood = mood,
                                count = count,
                                percentage = percentage
                            )
                        }
                    }
                }

                // Insights Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val insights = generateInsights(moodEntries.value)
                        insights.forEach { insight ->
                            Text(
                                text = "• $insight",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoodDistributionItem(
    mood: String,
    count: Int,
    percentage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = mood,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DayMoodColumn(
    dayOfWeek: Int,
    moods: List<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = getDayAbbreviation(dayOfWeek),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        
        moods.take(3).forEach { mood ->
            Image(
                painter = painterResource(getMoodIconResource(mood)),
                contentDescription = mood,
                modifier = Modifier.size(24.dp)
            )
        }
        
        if (moods.size > 3) {
            Text(
                text = "+${moods.size - 3}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getDayAbbreviation(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        1 -> "Mon"
        2 -> "Tue"
        3 -> "Wed"
        4 -> "Thu"
        5 -> "Fri"
        6 -> "Sat"
        7 -> "Sun"
        else -> ""
    }
}

private fun getMoodIconResource(mood: String): Int {
    return when (mood.lowercase()) {
        "happy" -> R.drawable.happy
        "sad" -> R.drawable.sad
        "angry" -> R.drawable.angry
        "anxious" -> R.drawable.relaxed
        else -> R.drawable.happy
    }
}

private fun getWeeklyStats(entries: List<MoodEntry>): Map<Int, List<String>> {
    val now = LocalDateTime.now()
    return entries
        .filter { entry -> ChronoUnit.DAYS.between(entry.timestamp, now) < 7 }
        .groupBy { entry -> entry.timestamp.dayOfWeek.value }
        .mapValues { entry -> entry.value.map { it.mood } }
}

private fun generateInsights(entries: List<MoodEntry>): List<String> {
    val insights = mutableListOf<String>()
    
    if (entries.isEmpty()) return listOf("Not enough data to generate insights")

    // Weekly trend
    val thisWeek = entries.filter { entry -> 
        ChronoUnit.DAYS.between(entry.timestamp, LocalDateTime.now()) < 7 
    }
    if (thisWeek.isNotEmpty()) {
        val avgMoodsPerDay = thisWeek.size / 7f
        insights.add("You've recorded an average of %.1f moods per day this week".format(avgMoodsPerDay))
    }
    
    // Recent trend
    val recentMoods = entries.takeLast(3).map { entry -> entry.mood }
    if (recentMoods.toSet().size == 1) {
        insights.add("Your last 3 recorded moods have been consistently '${recentMoods.first()}'")
    }
    
    // Time of day analysis
    val morningMoods = entries.filter { it.timestamp.hour in 6..11 }
    val afternoonMoods = entries.filter { it.timestamp.hour in 12..17 }
    val eveningMoods = entries.filter { it.timestamp.hour in 18..23 }
    
    val bestTimeOfDay = listOf(
        "morning" to morningMoods,
        "afternoon" to afternoonMoods,
        "evening" to eveningMoods
    ).maxByOrNull { it.second.size }?.first
    
    if (bestTimeOfDay != null) {
        insights.add("You tend to record your moods most often during the $bestTimeOfDay")
    }
    
    return insights
}

@Composable
private fun MoodPatternsCard(entries: List<MoodEntry>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Mood Patterns",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weekly mood distribution
            WeeklyMoodDistribution(entries)
        }
    }
}

@Composable
private fun WeeklyMoodDistribution(entries: List<MoodEntry>) {
    val weeklyStats = getWeeklyStats(entries)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (dayOfWeek in 1..7) {
            DayMoodColumn(
                dayOfWeek = dayOfWeek,
                moods = weeklyStats[dayOfWeek] ?: emptyList()
            )
        }
    }
} 