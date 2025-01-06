package com.example.ultimate_flashcard_app.ui.group_edit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.util.copy
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import com.example.ultimate_flashcard_app.data.Entities.Template
import com.example.ultimate_flashcard_app.databinding.GroupeditFragmentBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.GroupViewModel
import com.example.ultimate_flashcard_app.ui.viewmodels.TemplatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class groupedit_fragment: Fragment(R.layout.groupedit_fragment) {

    private var mdView = false
    private val viewModel by viewModels<GroupViewModel>()
    private val templateViewModel by viewModels<TemplatesViewModel>()
    private lateinit var binding: GroupeditFragmentBinding
    private val args: groupedit_fragmentArgs by navArgs()

    private var markwon: Markwon? = null
    private var realTextContent: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = GroupeditFragmentBinding.bind(view)
        markwon = Markwon.create(requireContext())

        setUpMarkDownPreview()
        setUpDetails()
        setUpAutoComplete()
    }

    private fun setUpMarkDownPreview()
    {
        binding.apply {
            previewFab.setOnClickListener {
                if (mdView) {
                    contentEdit.setText(realTextContent)
                } else {
                    realTextContent = contentEdit.text.toString()
                    markwon?.setMarkdown(contentEdit, realTextContent)
                }
                mdView = !mdView
            }

            contentEdit.setOnClickListener {
                if (mdView) {
                    contentEdit.setText(realTextContent)
                    mdView = false
                }
            }
        }
    }

    private fun setUpDetails()
    {
        if (args.group != null){
            binding.apply {
                realTextContent = args.group!!.description.toString()
                titleEdit.setText(args.group!!.title)
                contentEdit.setText(args.group!!.description)

                saveFab.setOnClickListener{
                    templateViewModel.getAllTemplates().observe(viewLifecycleOwner) { templates ->
                        templates?.let {
                            val selectedTemplateName = autoCompleteTxt.text.toString()
                            val selectedTemplate = templates.find { it.name == selectedTemplateName }
                            val templateId = selectedTemplate?.templateId
                            println("selected template id: $templateId") // outputs 1 (as expected)
                            val title = titleEdit.text.toString()
                            val description = contentEdit.text.toString()
                            val updatedGroup = args.group!!.copy(title = title, description = description, date = System.currentTimeMillis(), templateId = templateId )
                            println("updatedGroup template id: ${updatedGroup.templateId}")
                            viewModel.updateGroup(updatedGroup)
                            navigateBackHome()
                        }
                    }
                }

                deleteFab.setOnClickListener{
                    viewModel.deleteGroup(args.group!!)
                    navigateBackHome()
                }
            }
        }
        else{
            binding.apply {

                saveFab.setOnClickListener{
                    templateViewModel.getAllTemplates().observe(viewLifecycleOwner) { templates ->
                        templates?.let {
                            val selectedTemplateName = autoCompleteTxt.text.toString()
                            val selectedTemplate = templates.find { it.name == selectedTemplateName }
                            println("selected template id" + selectedTemplate?.templateId)
                            val templateId = selectedTemplate?.templateId
                            val newGroup = saveGroup(templateId)
                            viewModel.insertGroup(newGroup) // this works
                            navigateBackHome()
                        }
                    }
                }

                deleteFab.setOnClickListener{
                    navigateBackHome()
                }
            }
        }
    }

    private fun setUpAutoComplete() {
        binding.apply {
            templateViewModel.getAllTemplates().observe(viewLifecycleOwner) { templates ->
                val items = templates.map { it.name }
                val adapterItems = ArrayAdapter(requireContext(), R.layout.item_dropdown, items)

                autoCompleteTxt.setAdapter(adapterItems)
                autoCompleteTxt.setOnClickListener {
                    autoCompleteTxt.showDropDown()
                }

                autoCompleteTxt.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    autoCompleteTxt.setText(selectedItem, false)
                    autoCompleteTxt.clearFocus()

                    select.hint = null
                }
            }
        }
    }


    private fun saveGroup(templateId: Int?): FlashcardGroup {
        binding.apply {
            val title = titleEdit.text.toString()
            val description = contentEdit.text.toString()

            return FlashcardGroup(
                title = title,
                templateId = templateId,
                description = description,
                date = System.currentTimeMillis()
            )
        }
    }

    private fun navigateBackHome()
    {
        val action = groupedit_fragmentDirections.actionGroupeditFragmentToHomeFragmentGroups()
        findNavController().navigate(action)
    }
}