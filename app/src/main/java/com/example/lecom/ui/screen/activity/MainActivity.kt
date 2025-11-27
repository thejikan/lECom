package com.example.lecom.ui.screen.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lecom.R
import com.example.lecom.databinding.ActivityMainBinding
import com.example.lecom.utils.DesignSystem

/**
 * Main Activity - Entry point of the application
 * This is the first page that loads when the app starts
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DesignSystem.initialize(this)

        setupNavigation()
        setupButtons()
    }
    
    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateButtonState(destination.id)
            binding.toolbar.subtitle = when (destination.id) {
                R.id.homeFragment -> getString(R.string.nav_home_label)
                R.id.detailsFragment -> getString(R.string.nav_details_label)
                R.id.settingsFragment -> getString(R.string.nav_settings_label)
                else -> null
            }
        }
    }

    private fun setupButtons() {
        binding.buttonHome.setOnClickListener { navigateTo(R.id.homeFragment) }
        binding.buttonDetails.setOnClickListener { navigateTo(R.id.detailsFragment) }
        binding.buttonSettings.setOnClickListener { navigateTo(R.id.settingsFragment) }
    }

    private fun navigateTo(destinationId: Int) {
        if (!::navController.isInitialized) return
        if (navController.currentDestination?.id == destinationId) return
        navController.navigate(destinationId)
    }

    private fun updateButtonState(destinationId: Int) {
        binding.buttonHome.isEnabled = destinationId != R.id.homeFragment
        binding.buttonDetails.isEnabled = destinationId != R.id.detailsFragment
        binding.buttonSettings.isEnabled = destinationId != R.id.settingsFragment
    }
    
    override fun onResume() {
        super.onResume()
        // Update design system if needed (e.g., theme changes)
        DesignSystem.updateTheme(this)
    }
}

