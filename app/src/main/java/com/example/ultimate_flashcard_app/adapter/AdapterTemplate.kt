package com.example.ultimate_flashcard_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.data.Entities.Template
import com.example.ultimate_flashcard_app.databinding.ItemFlashcardBinding

class AdapterTemplate(private val mTemplates: List<Template>, private val listener: OnClickListener ):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    interface OnClickListener{
        fun onTemplateClick(template: Template)
        fun onTemplateLongClick(template: Template)
    }

    inner class TemplateViewHolder(private val flashcardBinding: ItemFlashcardBinding): RecyclerView.ViewHolder(flashcardBinding.root)
    {
        init {
            flashcardBinding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = mTemplates[position]
                    listener.onTemplateClick(note)
                }
            }

            flashcardBinding.root.setOnLongClickListener(){
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = mTemplates[position]
                    listener.onTemplateLongClick(note)
                }
                true
            }
        }

        fun bind(template: Template)
        {
            flashcardBinding.apply {
                textTitle.text = template.name
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TemplateViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return  mTemplates.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AdapterTemplate.TemplateViewHolder).bind(mTemplates[position])
    }


}