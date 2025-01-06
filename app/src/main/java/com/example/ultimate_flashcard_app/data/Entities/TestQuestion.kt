package com.example.ultimate_flashcard_app.data.Entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestQuestion(
    val question: String,
    val correctAnswer: String,
    val markedAnswer: String
) : Parcelable
