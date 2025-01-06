package com.example.ultimate_flashcard_app.ui.flashcard_edit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.databinding.FrashcardeditFragmentBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import com.example.ultimate_flashcard_app.ui.viewmodels.TemplatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardEditFragment: Fragment(R.layout.frashcardedit_fragment) {

    private val viewModel by viewModels<FlashcardViewModel>()
    private val templateViewModel by viewModels<TemplatesViewModel>()
    private val args: FlashcardEditFragmentArgs by navArgs()

    private var mdView = false
    private var currentGroupId = 0
    private var realTextContent: String = ""
    private lateinit var binding: FrashcardeditFragmentBinding
    private var markwon: Markwon? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentGroupId = args.groupId

        binding = FrashcardeditFragmentBinding.bind(view)
        markwon = context?.let { Markwon.create(it) }
        currentGroupId = args.groupId

        setupView()
        handleFlashcardData()
        observeFlashcardEvents()
    }

    private fun setupView() {
        binding.apply {
            // Handle the preview mode switch between markdown and raw text
            previewFab.setOnClickListener { toggleMarkdownView() }
            contentEdit.setOnClickListener { revertMarkdownViewIfNeeded() }
            deleteFlashcardFab.setOnClickListener { deleteFlashcardAndNavigate() }
        }
    }

    private fun handleFlashcardData() {
        val flashcard = args.flashcard
        if (flashcard != null) {
            populateFlashcard(flashcard)
        } else {
            loadTemplateAndPopulate()
        }
    }

    // flashcard != null
    private fun populateFlashcard(flashcard: Flashcard) {
        binding.apply {
            // Set initial values to the EditText fields
            titleEdit.setText(flashcard.title)
            contentEdit.setText(flashcard.content)
            answerEdit.setText(flashcard.answer)

            saveFlashcardFab.setOnClickListener {
                val updatedFlashcard = flashcard.copy(
                    title = titleEdit.text.toString(),
                    content = contentEdit.text.toString(),
                    date = System.currentTimeMillis(),
                    answer = answerEdit.text.toString(),
                    groupId = args.groupId
                )

                updateFlashcard(updatedFlashcard)
            }
        }
    }


    // flashcard = null
    private fun loadTemplateAndPopulate() {
        viewLifecycleOwner.lifecycleScope.launch {
            val templateId = viewModel.getGroup(args.groupId).templateId
            templateId?.let { it ->
                templateViewModel.getTemplate(it).observe(viewLifecycleOwner) { template ->
                    template?.let {
                        binding.contentEdit.setText(it.format)
                    }
                }
            }
            binding.apply {
                saveFlashcardFab.setOnClickListener{
                    saveFlashcard(
                        Flashcard(
                            title = titleEdit.text.toString(),
                            content = contentEdit.text.toString(),
                            date = System.currentTimeMillis(),
                            answer = answerEdit.text.toString(),
                            groupId = args.groupId
                        )
                    )
                }
            }
        }
    }

    private fun updateFlashcard(flashcard: Flashcard)
    {
        viewModel.updateFlashcard(flashcard)
        navigateToHome()
    }

    private fun saveFlashcard(flashcard: Flashcard) {
        viewModel.insertFlashcard(flashcard)
        navigateToHome()
    }

    private fun deleteFlashcardAndNavigate() {
        args.flashcard?.let {
            viewModel.deleteFlashcard(it)
        }
        navigateToHome()
    }

    private fun toggleMarkdownView() {
        binding.apply {
            if (mdView) {
                contentEdit.setText(realTextContent)
                mdView = false
            } else {
                realTextContent = contentEdit.text.toString()
                markwon?.setMarkdown(contentEdit, realTextContent)
                mdView = true
            }
        }
    }

    private fun revertMarkdownViewIfNeeded() {
        if (mdView) {
            binding.contentEdit.setText(realTextContent)
            mdView = false
        }
    }

    private fun observeFlashcardEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flashcardEvent.collect { event ->
                if (event is FlashcardViewModel.FlashcardEvent.NavigateToFlashcardFragment) {
                    navigateToHome()
                }
            }
        }
    }

    private fun navigateToHome() {
        val action = FlashcardEditFragmentDirections.actionFlashcardEditFragmentToNavHome(args.groupId)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHome()
            }
        })
    }
}