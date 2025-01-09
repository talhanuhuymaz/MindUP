package com.yeditepe.mindup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yeditepe.mindup.viewmodel.AuthState
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.yeditepe.mindup.viewmodel.MoodCarousel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

data class MoodEntry(
    val mood: String,
    val note: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val id: String = java.util.UUID.randomUUID().toString()
)



@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    var mood by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var savedEntries by remember { mutableStateOf<List<MoodEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Page",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // MoodCarousel kullanılıyor
        MoodCarousel { selectedMood ->
            mood = selectedMood
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (mood.isNotEmpty()) {
            Text(
                text = "You selected: $mood",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Arama çubuğu
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add a note") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (mood.isNotEmpty() && note.isNotEmpty()) {
                    savedEntries = savedEntries + MoodEntry(mood, note)
                    mood = ""
                    note = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Entry")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Filtrelenmiş ve gruplanmış entry'leri göster
        if (savedEntries.isNotEmpty()) {
            Text(
                text = "Your Entries",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            val filteredEntries = savedEntries.filter { entry ->
                entry.mood.contains(searchQuery, ignoreCase = true) ||
                        entry.note.contains(searchQuery, ignoreCase = true)
            }

            AnimatedVisibility(
                visible = filteredEntries.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    filteredEntries
                        .groupBy { it.timestamp.toLocalDate() }
                        .toSortedMap(compareByDescending { it }) // Sort dates newest first
                        .forEach { (date, entries) ->
                            item {
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(entries.sortedByDescending { it.timestamp }) { entry -> // Sort entries newest first
                                EntryCard(
                                    entry = entry,
                                    onDelete = {
                                        savedEntries = savedEntries.filter { it.id != entry.id }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            authViewModel.signOut()
        }) {
            Text(text = "Sign out", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun EntryCard(entry: MoodEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
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
                    text = "Mood: ${entry.mood}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete entry",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Note: ${entry.note}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = entry.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
