package com.example.ultimate_flashcard_app.ui.flashcard_list

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.adapter.Adapter
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.import_export.Export
import com.example.ultimate_flashcard_app.data.import_export.Import
import com.example.ultimate_flashcard_app.databinding.FragmentHomeBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home), Adapter.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by activityViewModels<FlashcardViewModel>()
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val flashcardViewHolder = ViewModelProvider(this).get(FlashcardViewHolder::class.java)
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(requireView())
        val root: View = binding.root

        viewModel.setCurrentGroupId(args.groupId)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val action = HomeFragmentDirections.actionNavHomeToHomeFragmentGroups()
                findNavController().navigate(action)
            }
        })

        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 1)
            recyclerView.setHasFixedSize(true)

            newFlashcardButton.setOnClickListener{
                val action = HomeFragmentDirections.actionNavHomeToFlashcardEditFragment(null, args.groupId)
                findNavController().navigate(action)
            }

            testButton.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner){flashcard ->
                        if (flashcard.size > 4)
                        {
                            val action = HomeFragmentDirections.actionFlashcardListFragmentToTestFragment(args.groupId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }

            importButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val group = viewModel.getGroup(groupId = args.groupId)
                    viewModel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner, Observer { flashcards ->
                        if (flashcards.isNotEmpty()) {
                            // Export the group with its flashcards
                            val export = Export()
                            export.exportGroup(requireContext(), group, flashcards)
                            Snackbar.make(view, "Group and flashcards should be exported", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(view, "No flashcards available to export", Snackbar.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            playButton.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner, Observer { flashcards ->
                        if (flashcards.isNotEmpty()) {
                            // Navigate to FlashcardSide1Fragment with the first flashcard as argument
                            val firstFlashcard = flashcards[0]
                            val action = HomeFragmentDirections.actionFlashcardListFragmentToCardsViewPagerFragment(args.groupId, firstFlashcard.flashcardId)
                            findNavController().navigate(action)
                        } else {
                            Snackbar.make(view, "No flashcards available", Snackbar.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner, Observer { flashcards ->
                    val adapter = Adapter(flashcards, this@HomeFragment)
                    recyclerView.adapter = adapter
                })
            }
        }
    }



    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFlashcardClick(flashcard: Flashcard) {
        println("Flashcard Clicked\n\n")
        val action = HomeFragmentDirections.actionFlashcardListFragmentToCardsViewPagerFragment(flashcard.groupId, flashcard.flashcardId)
        findNavController().navigate(action)
    }

    override fun onFlashcardLongClick(flashcard: Flashcard) {
        val action = HomeFragmentDirections.actionNavHomeToFlashcardEditFragment(flashcard, args.groupId)
        findNavController().navigate(action)
    }
}
