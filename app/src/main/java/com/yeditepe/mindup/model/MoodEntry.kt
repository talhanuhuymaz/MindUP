package com.yeditepe.mindup.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val mood: String = "",
    val note: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now()
) 