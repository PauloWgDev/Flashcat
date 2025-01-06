package com.example.ultimate_flashcard_app.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcards ORDER BY DATE DESC")
    fun getALlFlashcards(): LiveData<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE groupId = :groupId ORDER BY DATE DESC")
    fun getAllFlashcardsInGroup(groupId: Int): LiveData<List<Flashcard>>

    @Insert
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)
}


