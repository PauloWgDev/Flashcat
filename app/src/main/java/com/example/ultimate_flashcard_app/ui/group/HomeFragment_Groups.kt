package com.example.ultimate_flashcard_app.ui.group

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ultimate_flashcard_app.R
import com.example.ultimate_flashcard_app.SharedViewModel
import com.example.ultimate_flashcard_app.adapter.AdapterGroup
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import com.example.ultimate_flashcard_app.data.import_export.Import
import com.example.ultimate_flashcard_app.databinding.FragmentHomeGroupBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment_Groups: Fragment(R.layout.fragment_home_group), AdapterGroup.OnClickListener {

    private var _binding: FragmentHomeGroupBinding? = null
    private val viewModel: FlashcardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    public var import: Import? = null

    private val binding get() = _binding!!

    var onFragmentReady: (() -> Unit)? = null // A callback that will notify MainActivity when ready

    private val importLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                Log.d("HomeFragment_Groups", "Importing file from URI: $uri")
                import?.importFLSHG(uri)
                if (import == null) {
                    Log.e("HomeFragment_Groups", "Could not import, 'import' is null")
                } else {
                    Log.d("HomeFragment_Groups", "Should have imported the file")
                }
            }
        } else {
            Log.e("HomeFragment_Groups", "File picker result not OK")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("Fragment onViewCreated: initializing import object")

        _binding = FragmentHomeGroupBinding.bind(requireView())
        val root: View = binding.root

        import = Import(context = requireContext(), flashcardDao = viewModel.getFlashcardDao(), groupDao = viewModel.getGroupDao())

        println("Fragment onViewCreated: import object initialized")

        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)

            newGroupButton.setOnClickListener {
                val action = HomeFragment_GroupsDirections.actionHomeFragmentGroupsToGroupeditFragment(null)
                findNavController().navigate(action)
            }

            importButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "text/plain"
                }
                importLauncher.launch(intent)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getAllGroups().observe(viewLifecycleOwner, Observer { groups ->

                    val adapter = AdapterGroup(
                        groups,
                        this@HomeFragment_Groups,
                        viewModel.getGroupDao(),
                        viewLifecycleOwner.lifecycleScope
                    )
                    recyclerView.adapter = adapter
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        println("Fragment onResume: fragment is now resumed and ready")
        // Notify MainActivity that the fragment is ready to process the import
        onFragmentReady?.invoke()
    }

    fun importFile(uri: Uri) {
        if (import != null) {
            println("Importing file with URI: $uri")
            import?.importFLSHG(uri)
        } else {
            Log.e("HomeFragment_Groups", "Import object is null")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGroupClick(group: FlashcardGroup) {
        sharedViewModel.currentGroupId = group.groupId
        val action =
            HomeFragment_GroupsDirections.actionHomeFragmentGroupsToNavHome(groupId = group.groupId)
        findNavController().navigate(action)
    }

    override fun onGroupLongClick(group: FlashcardGroup) {
        sharedViewModel.currentGroupId = group.groupId
        val action =
            HomeFragment_GroupsDirections.actionHomeFragmentGroupsToGroupeditFragment(group)
        findNavController().navigate(action)

    }
}