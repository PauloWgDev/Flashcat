package com.example.ultimate_flashcard_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.TestQuestion

class AdapterTestReview(private val testQuestions: List<TestQuestion>):
    RecyclerView.Adapter<AdapterTestReview.TestReviewViewHolder>() {

    inner class TestReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val correctAnswerText: TextView = itemView.findViewById(R.id.correctAnswerText)
        val markedAnswerText: TextView = itemView.findViewById(R.id.chosenAnswerText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return TestReviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return testQuestions.size
    }

    override fun onBindViewHolder(holder: TestReviewViewHolder, position: Int) {
        val currentItem = testQuestions[position]
        holder.questionText.text = currentItem.question

        if (currentItem.correctAnswer != currentItem.markedAnswer)
        {
            holder.correctAnswerText.text = "✅\n ${currentItem.correctAnswer}"
            holder.markedAnswerText.text = "‼️\n ${currentItem.markedAnswer}"
        }
        else
        {
            holder.markedAnswerText.text = "✅"
            holder.correctAnswerText.text = "${currentItem.correctAnswer}"
        }
    }
}