package com.example.ultimate_flashcard_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultimate_flashcard_app.dao.FlashcardGroupDao
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupDao: FlashcardGroupDao): ViewModel(){

    val groups: LiveData<List<FlashcardGroup>> = groupDao.getAllGroups()

    fun insertGroup(group: FlashcardGroup)
    {
        viewModelScope.launch {
            groupDao.insertGroup(group)
        }
    }

    fun updateGroup(group: FlashcardGroup)
    {
        viewModelScope.launch {
            groupDao.updateGroup(group)
        }
    }

    fun deleteGroup(group: FlashcardGroup)
    {
        viewModelScope.launch {
            groupDao.deleteGroup(group)
        }
    }

    fun getCardsInGroup(groupId: Int): Int
    {
        var cardsCount = 0
        viewModelScope.launch {
            cardsCount = groupDao.getCountAllFlashcardsInGroup(groupId)
        }
        return cardsCount
    }
}