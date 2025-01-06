package com.example.ultimate_flashcard_app.ui.viewpagertest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.adapter.VPAdapter
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.databinding.FragmentCardsviewpagerBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import kotlinx.coroutines.launch

class Cards_ViewPager_Fragment : Fragment(R.layout.fragment_cardsviewpager) {
    private var _binding: FragmentCardsviewpagerBinding? = null
    private val args: Cards_ViewPager_FragmentArgs by navArgs()
    private val viewModel by activityViewModels<FlashcardViewModel>()
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCardsviewpagerBinding.bind(requireView())

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner, Observer{ flashcards ->

                    val adapter = VPAdapter(flashcards, context)
                    viewPager.adapter = adapter

                    // search for the selected flashcard in flashcards
                    val flashcardIndex : Int = getFlashcardIndexById(args.flashcardId, flashcards)

                    viewPager.currentItem = flashcardIndex
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getFlashcardIndexById(id: Int, flashcards: List<Flashcard>): Int
    {
        for (i in flashcards.indices) {
            if (flashcards[i].flashcardId == id) {
                return i
            }
        }
        return 0
    }
}
