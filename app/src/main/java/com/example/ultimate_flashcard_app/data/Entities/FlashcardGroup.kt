package com.example.ultimate_flashcard_app.data.Entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "Flashcard_Group",
    foreignKeys = [ForeignKey(entity = Template::class,
        parentColumns = ["templateId"],
        childColumns = ["templateId"])])
@Parcelize
data class FlashcardGroup(
    @PrimaryKey(autoGenerate = true) val groupId: Int = 0,
    val templateId: Int?,
    val title: String,
    val description: String?,
    var cards: Int? = 0,
    val date: Long
): Parcelable





