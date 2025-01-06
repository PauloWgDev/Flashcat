package com.example.ultimate_flashcard_app.ui.template

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.adapter.AdapterTemplate
import com.example.ultimate_flashcard_app.data.Entities.Template
import com.example.ultimate_flashcard_app.databinding.FragmentTemplatesBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.TemplatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TemplatesFragment : Fragment(R.layout.fragment_templates), AdapterTemplate.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<TemplatesViewModel>()
        val binding = FragmentTemplatesBinding.bind(requireView())


        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)

            newTemplateButton.setOnClickListener{
                val action = TemplatesFragmentDirections.actionNavGalleryToTemplateEditFragment(null)
                findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getAllTemplates().observe(viewLifecycleOwner, Observer { templates->

                    val adapter = AdapterTemplate(
                        templates,
                        this@TemplatesFragment
                    )
                    recyclerView.adapter = adapter
                })
            }
        }
    }

    override fun onTemplateClick(template: Template) {
        val action = TemplatesFragmentDirections.actionNavGalleryToTemplateEditFragment(template)
        findNavController().navigate(action)
    }

    override fun onTemplateLongClick(template: Template) {
        println("On long Click works")
    }
}