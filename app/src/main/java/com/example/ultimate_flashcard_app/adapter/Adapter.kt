package com.example.ultimate_flashcard_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.databinding.ItemFlashcardBinding
import java.text.SimpleDateFormat

class Adapter(private val mFlashcards: List<Flashcard>, private val listener: OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnClickListener{
        fun onFlashcardClick(flashcard: Flashcard)
        fun onFlashcardLongClick(flashcard: Flashcard)
    }

    inner class FlashcardViewHolder(private val flashcardBinding: ItemFlashcardBinding): RecyclerView.ViewHolder(flashcardBinding.root)
    {
        init {
            flashcardBinding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = mFlashcards[position]
                    listener.onFlashcardClick(note)
                }
            }

            flashcardBinding.root.setOnLongClickListener(){
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = mFlashcards[position]
                    listener.onFlashcardLongClick(note)
                }
                true
            }
        }

        fun bind(flashcard: Flashcard)
        {
            flashcardBinding.apply {
                textTitle.text = flashcard.title
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                textDate.text = formatter.format(flashcard.date)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val flashcardBinding = ItemFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashcardViewHolder(flashcardBinding)
    }

    override fun getItemCount(): Int {
        return mFlashcards.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FlashcardViewHolder).bind(mFlashcards[position])
    }
}