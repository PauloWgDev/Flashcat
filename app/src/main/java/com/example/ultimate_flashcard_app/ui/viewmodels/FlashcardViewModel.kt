package com.example.ultimate_flashcard_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultimate_flashcard_app.dao.FlashcardDao
import com.example.ultimate_flashcard_app.dao.FlashcardGroupDao
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(private val flashcardDao: FlashcardDao, private val groupDao: FlashcardGroupDao) : ViewModel(){

    val flashcardChannel = Channel<FlashcardEvent>()
    val flashcardEvent = flashcardChannel.receiveAsFlow()


    private var currentGroupId = 0


    fun getGroupDao(): FlashcardGroupDao{
        return groupDao
    }

    fun getFlashcardDao(): FlashcardDao{
        return flashcardDao
    }

    fun getCurrentGroupId(): Int {
        Log.d("FlashcardViewModel", "getCurrentGroupId() called, currentGroupId: $currentGroupId")
        return currentGroupId
    }

    fun setCurrentGroupId(groupId: Int) {
        Log.d("FlashcardViewModel", "setCurrentGroupId() called with groupId: $groupId")
        currentGroupId = groupId
    }

    fun getAllGroups(): LiveData<List<FlashcardGroup>> {
        return groupDao.getAllGroups()
    }

    fun getFlashcardsByGroupId(groupId: Int): LiveData<List<Flashcard>> {
        currentGroupId = groupId
        return flashcardDao.getAllFlashcardsInGroup(groupId)
    }

    fun insertFlashcard(flashcard: Flashcard) = viewModelScope.launch{
        flashcardDao.insertFlashcard(flashcard)
    }

    fun updateFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        flashcardDao.updateFlashcard(flashcard)
    }

    fun deleteFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        flashcardDao.deleteFlashcard(flashcard)
        flashcardChannel.send(
            FlashcardEvent.ShowUndoSnackBar(
                "Flashcard deleted succesfully",
                flashcard
            )
        )
    }

    sealed class FlashcardEvent{
        data class ShowUndoSnackBar(val msg: String, val flashcard: Flashcard): FlashcardEvent()
        object NavigateToFlashcardFragment: FlashcardEvent()
    }



    // Group Methods
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
        var cardsCount: Int = 0
        viewModelScope.launch {
            cardsCount = groupDao.getCountAllFlashcardsInGroup(groupId)
        }
        return cardsCount
    }

    suspend fun getGroup(groupId: Int): FlashcardGroup {
        return groupDao.getGroup(groupId)
    }
}
