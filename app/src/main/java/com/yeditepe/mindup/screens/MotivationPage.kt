package com.yeditepe.mindup.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeditepe.mindup.components.PageHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotivationPage(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PageHeader(
            title = "Motivation",
            onMenuClick = onMenuClick
        )

        // Geri kalan içerik
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            val currentDate = LocalDate.now()
            val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

            val dailyActivities = listOf(
                DailyActivity("Monday", "Start Fresh", "🌅 Morning meditation or light exercise"),
                DailyActivity("Tuesday", "Build Momentum", "📝 Set your top 3 priorities for the week"),
                DailyActivity("Wednesday", "Mid-Week Boost", "🎯 Review and celebrate your progress"),
                DailyActivity("Thursday", "Stay Strong", "🌿 Take a mindful break in nature"),
                DailyActivity("Friday", "Reflect & Plan", "✨ Plan something fun for the weekend"),
                DailyActivity("Saturday", "Recharge", "🎨 Try a creative activity"),
                DailyActivity("Sunday", "Prepare & Rest", "🌙 Early bedtime for a fresh start")
            )

            val todayActivity = dailyActivities[currentDate.dayOfWeek.value - 1]

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Activity",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = todayActivity.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = todayActivity.suggestion,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Haftanın diğer günleri başlığı
            Text(
                text = "Weekly Wellness Guide",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Haftalık liste
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dailyActivities) { activity ->
                    val isToday = activity.day == todayActivity.day
                    WeekDayActivityCard(
                        activity = activity,
                        isToday = isToday
                    )
                }
            }
        }
    }
}

@Composable
fun WeekDayActivityCard(
    activity: DailyActivity,
    isToday: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) Color(0xFFE8F5E9) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = activity.day,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isToday) Color(0xFF2E7D32) else Color.Black
                    )
                    Text(
                        text = activity.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isToday) Color(0xFF2E7D32) else Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activity.suggestion,
                    fontSize = 14.sp,
                    color = if (isToday) Color(0xFF2E7D32).copy(alpha = 0.7f) else Color.Gray
                )
            }
        }
    }
}

data class DailyActivity(
    val day: String,
    val title: String,
    val suggestion: String
) 