package com.example.ultimate_flashcard_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.dao.FlashcardGroupDao
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import com.example.ultimate_flashcard_app.databinding.ItemGroupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AdapterGroup(private val mGroups: List<FlashcardGroup>,
                   private val listener: OnClickListener,
                   private val groupDao: FlashcardGroupDao,
                   private val coroutineScope: CoroutineScope): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener{
        fun onGroupClick(group: FlashcardGroup)
        fun onGroupLongClick(group: FlashcardGroup)
    }

    inner class  GroupViewHolder(private val groupBinding: ItemGroupBinding): RecyclerView.ViewHolder(groupBinding.root){

        init {
            groupBinding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                {
                    val group = mGroups[position]
                    listener.onGroupClick(group)
                }
            }

            groupBinding.root.setOnLongClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                {
                    val group = mGroups[position]
                    listener.onGroupLongClick(group)
                }
                true
            }
        }

        fun bind(group: FlashcardGroup)
        {
            groupBinding.apply {
                textTitle.text = group.title
                // Launch coroutine to fetch count of flashcards asynchronously
                coroutineScope.launch {
                    val cardCount = groupDao.getCountAllFlashcardsInGroup(group.groupId)
                    textCardCount.text = "cards: $cardCount"
                }
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                textDate.text = formatter.format(group.date)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val groupBinding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(groupBinding)
    }

    override fun getItemCount(): Int {
        return mGroups.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GroupViewHolder).bind(mGroups[position])
    }
}