package com.yeditepe.mindup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.yeditepe.mindup.data.AppDatabase
import com.yeditepe.mindup.model.MoodEntry
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.google.firebase.auth.FirebaseAuth

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val moodDao = database.moodDao()
    private val auth = FirebaseAuth.getInstance()
    
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    
    private val _currentUserId = MutableLiveData<String?>()
    
    init {
        _currentUserId.value = auth.currentUser?.uid
        auth.addAuthStateListener { firebaseAuth ->
            _currentUserId.value = firebaseAuth.currentUser?.uid
        }
    }
    
    val moodEntries: LiveData<List<MoodEntry>> = _currentUserId.switchMap { userId ->
        userId?.let {
            moodDao.getMoodsByUserId(it).asLiveData()
        } ?: MutableLiveData(emptyList())
    }
    
    fun addMoodEntry(mood: String, note: String) {
        _loading.value = true
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val entry = MoodEntry(
                    userId = userId,
                    mood = mood,
                    note = note,
                    timestamp = LocalDateTime.now()
                )
                moodDao.insertMood(entry)
            }
            _loading.value = false
        }
    }
    
    fun deleteMoodEntry(entryId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                moodDao.deleteMoodByIdAndUserId(entryId, userId)
            }
        }
    }
    
    fun getMoodStats(): Map<String, Int> {
        return moodEntries.value?.groupBy { it.mood }?.mapValues { it.value.size } ?: emptyMap()
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener { }
    }
} 