package com.yeditepe.mindup.data

import androidx.room.*
import com.yeditepe.mindup.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntry)

    @Delete
    suspend fun deleteMood(mood: MoodEntry)

    @Query("DELETE FROM mood_entries WHERE id = :moodId")
    suspend fun deleteMoodById(moodId: String)
} 