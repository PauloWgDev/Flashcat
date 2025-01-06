package com.example.ultimate_flashcard_app.data.Entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "Flashcards",
    foreignKeys = [ForeignKey(entity = FlashcardGroup::class,
        parentColumns = ["groupId"], childColumns = ["groupId"],
        onDelete = ForeignKey.CASCADE)])
@Parcelize
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val flashcardId: Int = 0,
    val groupId: Int,
    val title: String,      // side 1
    val content: String,    // side 2
    val answer: String?,    // this will be displayed in the test
    val date: Long
) : Parcelable







