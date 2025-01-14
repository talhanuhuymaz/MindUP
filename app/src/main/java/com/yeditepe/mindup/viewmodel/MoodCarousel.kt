package com.yeditepe.mindup.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeditepe.mindup.R

data class MoodItem(
    val imageRes: Int,
    val name: String
)

@Composable
fun MoodCarousel(onMoodSelected: (String) -> Unit) {
    val moods = listOf(
        MoodItem(R.drawable.happy, "Happy"),
        MoodItem(R.drawable.sad, "Sad"),
        MoodItem(R.drawable.relaxed, "Calm"),
        MoodItem(R.drawable.angry, "Angry"),
        MoodItem(R.drawable.anxious, "Anxious"),

    )

    var selectedMoodIndex by remember { mutableStateOf(-1) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(moods) { mood ->
            MoodCard(
                mood = mood,
                isSelected = selectedMoodIndex == moods.indexOf(mood),
                onClick = {
                    selectedMoodIndex = moods.indexOf(mood)
                    onMoodSelected(mood.name)
                }
            )
        }
    }
}

@Composable
private fun MoodCard(
    mood: MoodItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isSelected) Color.Black else Color.White,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Black else Color.LightGray,
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = mood.imageRes),
                contentDescription = mood.name,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = mood.name,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
