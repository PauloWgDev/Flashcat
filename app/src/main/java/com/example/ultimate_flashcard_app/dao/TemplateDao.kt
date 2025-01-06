package com.example.ultimate_flashcard_app.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ultimate_flashcard_app.data.Entities.Template

@Dao
interface TemplateDao {

    @Query("SELECT * FROM Template")
    fun getAllTemplates(): LiveData<List<Template>>

    @Query("SELECT * FROM Template WHERE templateId = :templateId")
    fun getTemplate(templateId: Int): LiveData<Template>

    @Insert
    suspend fun insertTemplate(template: Template)

    @Update
    suspend fun updateTemplate(template: Template)

    @Delete
    suspend fun deleteTemplate(template: Template)
}