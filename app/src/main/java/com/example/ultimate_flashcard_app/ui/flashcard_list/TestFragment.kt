package com.example.ultimate_flashcard_app.ui.flashcard_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.Entities.TestQuestion
import com.example.ultimate_flashcard_app.databinding.FragmentTestBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class TestFragment: Fragment(R.layout.fragment_test) {

    private var _binding: FragmentTestBinding? = null
    private val args: TestFragmentArgs by navArgs()
    val viewmodel by activityViewModels<FlashcardViewModel>()
    private val binding get() = _binding!!

    private val questionsTotal = 10
    private var questionCounter_ = 0

    private var correctAnswers = 0
    private var correctOptionIndex = 0
    private var currentQuestion = ""

    private var flashcards = arrayListOf<Flashcard>()

    private var optionsButtons = arrayListOf<Button>()

    private var questionsSets = arrayListOf<TestQuestion>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTestBinding.bind(requireView())

        flashcards.clear()

        // fill questions & answers
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.getFlashcardsByGroupId(args.groupId).observe(viewLifecycleOwner){flashcardsList ->
                for (f in flashcardsList)
                {
                    flashcards.add(f)
                }

                // load first question
                loadQuestion()
            }
        }

        // when option is selected load new question

        binding.apply {

            optionsButtons.add(option1Button)
            optionsButtons.add(option2Button)
            optionsButtons.add(option3Button)
            optionsButtons.add(option4Button)

            option1Button.setOnClickListener(){
                checkIfCorrect(0)
                loadQuestion()
            }
            option2Button.setOnClickListener{
                checkIfCorrect(1)
                loadQuestion()
            }
            option3Button.setOnClickListener{
                checkIfCorrect(2)
                loadQuestion()
            }
            option4Button.setOnClickListener{
                checkIfCorrect(3)
                loadQuestion()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadQuestion()
    {
        // get correct set
        val correctSet = flashcards[Random.nextInt(0, flashcards.count())]
        flashcards.remove(correctSet)

        // Set correct button
        correctOptionIndex = Random.nextInt(0, optionsButtons.count())
        optionsButtons[correctOptionIndex].text = processText(correctSet.content)
        currentQuestion = correctSet.title

        // get 3 incorrect answers
        val incorrectAnswers = arrayListOf<String>()
        for (i in 0 until optionsButtons.lastIndex) {
            val randomFlashcard = flashcards[Random.nextInt(0, flashcards.count())]
            var answerText = processText(randomFlashcard.content)
            if (randomFlashcard.answer != "" && randomFlashcard.answer != null)
            {
                answerText = processText(randomFlashcard.answer)
            }

            incorrectAnswers.add(answerText)
        }

        // load current question and current answers in UI
        setUI(correctSet, incorrectAnswers)
    }

    @SuppressLint("SetTextI18n")
    private fun setUI(correctSet: Flashcard, incorrectAnswers: List<String>)
    {
        binding.apply {
            questionCounter.text = "${questionCounter_}/${questionsTotal}"
            questionText.text = correctSet.title

            var helper = 0
            for (i in 0 until 4)
            {
                if (i == correctOptionIndex)
                {
                    optionsButtons[i].text = processText(correctSet.content)
                    if (correctSet.answer != null && correctSet.answer != "")
                    {
                        optionsButtons[i].text = processText(correctSet.answer)
                    }

                    helper = -1
                    continue
                }

                optionsButtons[i].text = incorrectAnswers[i + helper]
            }
        }
    }

    private fun checkIfCorrect(option: Int)
    {
        questionCounter_ += 1
        if (questionCounter_ > questionsTotal)
        {
            navigateReview()
        }

        val chosenOption = optionsButtons[option].text
        val correctOption = optionsButtons[correctOptionIndex].text

        val questionSet = TestQuestion(currentQuestion, correctOption.toString(), chosenOption.toString())
        questionsSets.add(questionSet)

        if (option == correctOptionIndex)
        {
            correctAnswers++
        }
        println("Correct answers: $correctAnswers")
    }

    private fun processText(text: String): String
    {
        return text.trimStart('\n')
    }

    private fun navigateReview()
    {
        // get arg
        val action = TestFragmentDirections.actionTestFragmentToTestReviewFragment(
                questionsSets.toTypedArray(),
                groupId = args.groupId,
                correctAnswers = correctAnswers)

        findNavController().navigate(action)
    }
}