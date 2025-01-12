import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoodIntensitySlider(
    intensity: Int,
    onIntensityChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "Mood Intensity",
            style = MaterialTheme.typography.titleSmall
        )
        Slider(
            value = intensity.toFloat(),
            onValueChange = { onIntensityChange(it.toInt()) },
            valueRange = 1f..10f,
            steps = 9
        )
        Text(
            text = "$intensity/10",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ActivityTagsSelector(
    selectedActivities: List<String>,
    onActivitySelected: (String) -> Unit
) {
    val activities = listOf(
        "Exercise", "Work", "Study", "Social", "Family",
        "Entertainment", "Rest", "Travel", "Shopping", "Other"
    )
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(activities) { activity ->
            FilterChip(
                selected = activity in selectedActivities,
                onClick = { onActivitySelected(activity) },
                label = { Text(activity) }
            )
        }
    }
} 