package com.example.ultimate_flashcard_app.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup

@Dao
interface FlashcardGroupDao {
    @Query("SELECT * FROM flashcard_group ORDER BY date DESC")
    fun getAllGroups(): LiveData<List<FlashcardGroup>>

    @Query("SELECT * FROM flashcard_group WHERE groupId = :groupId")
    suspend fun getGroup(groupId: Int): FlashcardGroup

    @Query("SELECT COUNT(*) FROM flashcards WHERE groupId = :groupId")
    suspend fun getCountAllFlashcardsInGroup(groupId: Int): Int

    @Insert
    suspend fun insertGroup(group: FlashcardGroup)

    @Update
    suspend fun updateGroup(group: FlashcardGroup)

    @Delete
    suspend fun deleteGroup(group: FlashcardGroup)
}