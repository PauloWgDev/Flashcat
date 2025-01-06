package com.example.ultimate_flashcard_app.data.Database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ultimate_flashcard_app.dao.FlashcardDao
import com.example.ultimate_flashcard_app.dao.FlashcardGroupDao
import com.example.ultimate_flashcard_app.dao.TemplateDao
import com.example.ultimate_flashcard_app.dao.TestHistoryDao
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import com.example.ultimate_flashcard_app.data.Entities.Template
import com.example.ultimate_flashcard_app.data.Entities.TestHistory
import javax.xml.transform.Templates

@Database(entities = [Flashcard::class, FlashcardGroup::class,
    Template::class, TestHistory::class],
    version = 8,
    exportSchema = true,
    autoMigrations = [ AutoMigration (from = 7, to = 8)]
)
abstract class database: RoomDatabase()
{
    abstract fun folderDao(): FlashcardDao

    abstract fun groupDao(): FlashcardGroupDao

    abstract fun templateDao(): TemplateDao

    abstract fun historyDao(): TestHistoryDao
}





