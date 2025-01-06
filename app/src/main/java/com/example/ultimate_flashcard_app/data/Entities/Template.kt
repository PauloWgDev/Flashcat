package com.example.ultimate_flashcard_app.data.Entities

import android.os.Parcelable
import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "Template")
@Parcelize
data class Template(
    @PrimaryKey(autoGenerate = true) val templateId: Int = 0,
    val name: String,
    val format: String,
    val lastUsed: Long
) : Parcelable
