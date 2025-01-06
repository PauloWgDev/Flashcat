package com.example.ultimate_flashcard_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ultimate_flashcard_app.databinding.ActivityMainBinding
import com.example.ultimate_flashcard_app.ui.viewmodels.FlashcardViewModel
import com.example.ultimate_flashcard_app.ui.group.HomeFragment_Groups
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val flashcardViewModel: FlashcardViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var importUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment_Groups, R.id.nav_gallery, R.id.cards_ViewPager_Fragment
            ), drawerLayout
        )

        // Handle the intent that may have triggered the activity
        handleIntent(intent)

        // Add listener for navigation changes to check if we land on HomeFragment_Groups
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment_Groups) {
                println("Navigated to HomeFragment_Groups")
                importUri?.let { uri ->
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                    val currentFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull()

                    if (currentFragment is HomeFragment_Groups) {
                        println("Current fragment is HomeFragment_Groups")
                        // Set the callback for when the fragment is fully ready
                        currentFragment.onFragmentReady = {
                            println("IMPORT: " + currentFragment.import.toString())
                            currentFragment.importFile(uri)
                            // Clear the URI so it doesn't import multiple times
                            importUri = null
                        }
                    } else {
                        println("Current fragment is not HomeFragment_Groups")
                    }
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle new intent when activity is already running
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_VIEW == action) {
            intent.data?.let { uri ->
                importUri = uri
                println("URI: $importUri")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}