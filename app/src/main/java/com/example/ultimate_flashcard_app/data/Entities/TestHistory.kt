package com.example.ultimate_flashcard_app.data.Entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "TestHistory",
    foreignKeys = [ForeignKey(entity = FlashcardGroup::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"])])
@Parcelize
data class TestHistory(
    @PrimaryKey(autoGenerate = true) val historyId: Int = 0,
    val groupId: Int,
    val score: Int,
    val date: Long
) : Parcelable
