package com.example.ultimate_flashcard_app.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `TestHistory` (
                `historyId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `groupId` INTEGER NOT NULL,
                `score` INTEGER NOT NULL,
                `date` INTEGER NOT NULL,
                FOREIGN KEY(`groupId`) REFERENCES `Flashcard_Group`(`groupId`)
            )
            """.trimIndent()
        )
    }
}
