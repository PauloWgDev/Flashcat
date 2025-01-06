package com.example.ultimate_flashcard_app.ui.flashcard_list

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.adapter.AdapterTestReview
import com.example.ultimate_flashcard_app.data.Entities.TestHistory
import com.example.ultimate_flashcard_app.databinding.FragmentTestReviewBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import com.example.ultimate_flashcard_app.ui.viewmodels.HistoryViewModel

class TestReviewFragment: Fragment(R.layout.fragment_test_review)
{
    private var _binding: FragmentTestReviewBinding? = null
    private val binding get() = _binding!!

    private val args: TestReviewFragmentArgs by navArgs()

    private val historyViewModel by activityViewModels<HistoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTestReviewBinding.bind(requireView())

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                println("go back")
                val action = TestReviewFragmentDirections.actionTestReviewFragmentToFlashcardListFragment(args.groupId)
                findNavController().navigate(action)
            }
        })

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)

            recyclerView.adapter = AdapterTestReview(args.testQuestions.toList())

            val score = ((args.correctAnswers.toFloat() / args.testQuestions.size) * 100).toInt()
            percentageText.text = "$score%"


            // Change image based on performance
            when (score) {
                100 -> michiImage.setImageResource(R.drawable.michi_love)
                in 90..100 -> michiImage.setImageResource(R.drawable.michi_amazed)
                in 50..89 -> michiImage.setImageResource(R.drawable.michi_hi)
                in 30..49 -> michiImage.setImageResource(R.drawable.michi_angry)
                else -> michiImage.setImageResource(R.drawable.michi_sad)
            }

            SaveHistory(score)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun SaveHistory(score: Int)
    {
        val newHistory = TestHistory(groupId = args.groupId, score = score, date = System.currentTimeMillis())
        historyViewModel.insertHistory(newHistory)
    }
}