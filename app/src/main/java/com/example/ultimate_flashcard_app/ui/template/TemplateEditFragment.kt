package com.example.ultimate_flashcard_app.ui.template

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.Template
import com.example.ultimate_flashcard_app.databinding.FragmentTemplateEditBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.TemplatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class TemplateEditFragment: Fragment(R.layout.fragment_template_edit) {

    private var mdView = false
    private var _binding: FragmentTemplateEditBinding? = null
    private val viewModel by viewModels<TemplatesViewModel>()
    private val markwon: Markwon? = context?.let { Markwon.create(it) }
    private val args: TemplateEditFragmentArgs by navArgs()
    private var realTextContent: String = ""

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTemplateEditBinding.bind(requireView())

        setUpMarkDownPreview()
        setUpDetails()
    }

    private fun setUpMarkDownPreview()
    {
        binding.apply {
            previewFab.setOnClickListener {
                if (mdView) {
                    formatEdit.setText(realTextContent)
                } else {
                    realTextContent = formatEdit.text.toString()
                    markwon?.setMarkdown(formatEdit, realTextContent)
                }
                mdView = !mdView
            }

            formatEdit.setOnClickListener {
                if (mdView) {
                    formatEdit.setText(realTextContent)
                    mdView = false
                }
            }
        }
    }

    private fun getNewTemplate(): Template
    {
        binding.apply {
            val name = titleEdit.text.toString()
            val format = formatEdit.text.toString()
            val newTemplate = Template(
                name = name,
                format = format,
                lastUsed = System.currentTimeMillis()
            )
            return newTemplate
        }
    }


    private fun setUpDetails()
    {
        if (args.template != null){
            binding.apply {
                realTextContent = args.template!!.format
                titleEdit.setText(args.template!!.name)
                formatEdit.setText(args.template!!.format)

                saveFab.setOnClickListener{
                    val updatedGroup = getNewTemplate()
                    viewModel.updateTemplate(updatedGroup)
                    navigateBackHome()
                }

                deleteFab.setOnClickListener{
                    viewModel.deleteTemplate(args.template!!)
                    navigateBackHome()
                }
            }
        }
        else{
            binding.apply {

                saveFab.setOnClickListener{
                    val newTemplate = getNewTemplate()
                    viewModel.insertTemplate(newTemplate)
                    navigateBackHome()
                }

                deleteFab.setOnClickListener{
                    navigateBackHome()
                }
            }
        }
    }


    private fun navigateBackHome()
    {
        val action = TemplateEditFragmentDirections.actionTemplateEditFragmentToNavGallery()
        findNavController().navigate(action)
    }
}