package com.yeditepe.mindup.data

import androidx.room.*
import com.yeditepe.mindup.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getMoodsByUserId(userId: String): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntry)

    @Delete
    suspend fun deleteMood(mood: MoodEntry)

    @Query("DELETE FROM mood_entries WHERE id = :moodId AND userId = :userId")
    suspend fun deleteMoodByIdAndUserId(moodId: String, userId: String)
} 