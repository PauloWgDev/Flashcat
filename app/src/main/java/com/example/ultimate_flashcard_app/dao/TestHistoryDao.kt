package com.example.ultimate_flashcard_app.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ultimate_flashcard_app.data.Entities.TestHistory

@Dao
interface TestHistoryDao {

    @Query("SELECT * FROM TestHistory")
    fun getWholeHistory(): LiveData<List<TestHistory>>

    @Query("SELECT * FROM TestHistory WHERE groupId = :groupId")
    fun getHistoryFromGroup(groupId: Int): LiveData<List<TestHistory>>

    @Insert
    suspend fun insertHistory(history: TestHistory)

    @Delete
    suspend fun deleteHistory(history: TestHistory)
}