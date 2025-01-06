package com.example.ultimate_flashcard_app.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import io.noties.markwon.Markwon

class VPAdapter(
    private val mFlashcards: List<Flashcard>, context: Context?
) : RecyclerView.Adapter<VPAdapter.ViewPagerViewHolder>() {

    private val markwon: Markwon? = context?.let { Markwon.create(it) }

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val side1Title: TextView = itemView.findViewById(R.id.side1_title)
        val side2Title: TextView = itemView.findViewById(R.id.side2_title)
        val side2Content: TextView = itemView.findViewById(R.id.side2_content)
        val cardView: View = itemView.findViewById(R.id.flashcard_card)
        val side1Layout: View = itemView.findViewById(R.id.relativeLayout_side1)
        val side2Layout: View = itemView.findViewById(R.id.relativeLayout_side2)

        private var isFront = true
        private val flipping = false

        @SuppressLint("ResourceType")
        private  val frontAnim: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.anim.fade_front_animator) as AnimatorSet
        @SuppressLint("ResourceType")
        private  val backAnim: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.anim.fade_back_animator) as AnimatorSet
        @SuppressLint("ResourceType")
        private  val frontflipAnim: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.anim.flip_front_animator) as AnimatorSet
        @SuppressLint("ResourceType")
        private  val backflipAnim: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.anim.flip_back_animator) as AnimatorSet

        init {
            val scale:Float = itemView.context.resources.displayMetrics.density
            cardView.cameraDistance = 15000 * scale

            cardView.setOnClickListener {
                flipCard()
            }
        }


        private fun flipCard() {

            // return if any flip animation is playing
            if(frontflipAnim.isRunning || backflipAnim.isRunning)
            {
                return
            }

            if (isFront) {
                frontflipAnim.setTarget(cardView)
                frontAnim.setTarget(side1Layout)
                backAnim.setTarget(side2Layout)
                frontflipAnim.start()
                frontAnim.start()
                backAnim.start()
                isFront = false
            } else {
                backflipAnim.setTarget(cardView)
                frontAnim.setTarget(side2Layout)
                backAnim.setTarget(side1Layout)
                backflipAnim.start()
                frontAnim.start()
                backAnim.start()
                isFront = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curCard = mFlashcards[position]
        holder.side1Title.text = curCard.title
        holder.side2Title.text = curCard.title

        if(markwon == null)
        {
            println("Markwon is null")
            holder.side2Content.text = curCard.content
        }
        else{
            markwon.setMarkdown(holder.side2Content, curCard.content)
        }
    }

    override fun getItemCount(): Int {
        return mFlashcards.size
    }
}
