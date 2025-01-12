package com.yeditepe.mindup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yeditepe.mindup.data.AppDatabase
import com.yeditepe.mindup.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val moodDao = database.moodDao()
    
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading
    
    val moodEntries: LiveData<List<MoodEntry>> = moodDao.getAllMoods().asLiveData()
    
    fun addMoodEntry(mood: String, note: String) {
        _loading.value = true
        viewModelScope.launch {
            val entry = MoodEntry(
                mood = mood,
                note = note,
                timestamp = LocalDateTime.now()
            )
            moodDao.insertMood(entry)
            _loading.value = false
        }
    }
    
    fun deleteMoodEntry(entryId: String) {
        viewModelScope.launch {
            moodDao.deleteMoodById(entryId)
        }
    }
    
    fun getMoodStats(): Map<String, Int> {
        return moodEntries.value?.groupBy { it.mood }?.mapValues { it.value.size } ?: emptyMap()
    }
} 