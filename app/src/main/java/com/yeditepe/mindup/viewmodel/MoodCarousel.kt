package com.yeditepe.mindup.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeditepe.mindup.R

@Composable
fun MoodCarousel(onMoodSelected: (String) -> Unit) {
    val moodList = listOf(
        R.drawable.happy to "Happy",
        R.drawable.sad to "Sad",
        R.drawable.angry to "Angry",
        R.drawable.relaxed to "Relaxed"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        moodList.forEach { (imageRes, mood) ->
            CircularMoodCard(imageRes = imageRes, mood = mood, onClick = { onMoodSelected(mood) })
        }
    }
}

@Composable
fun CircularMoodCard(imageRes: Int, mood: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = mood,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mood, fontSize = 12.sp)
        }
    }
}
