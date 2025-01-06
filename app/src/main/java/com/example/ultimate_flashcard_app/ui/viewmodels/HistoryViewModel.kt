package com.example.ultimate_flashcard_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultimate_flashcard_app.dao.TestHistoryDao
import com.example.ultimate_flashcard_app.data.Entities.TestHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyDao: TestHistoryDao): ViewModel()
{
    fun getWholeHistory(): LiveData<List<TestHistory>>
    {
        return historyDao.getWholeHistory()
    }

    fun getHistoryFromGroup(groupId: Int): LiveData<List<TestHistory>>
    {
        return historyDao.getHistoryFromGroup(groupId)
    }

    fun insertHistory(history: TestHistory)
    {
        viewModelScope.launch {
            historyDao.insertHistory(history)
        }
    }

    fun deleteHistory(history: TestHistory)
    {
        viewModelScope.launch {
            historyDao.deleteHistory(history)
        }
    }
}