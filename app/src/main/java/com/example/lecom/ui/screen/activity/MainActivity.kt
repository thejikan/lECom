package com.example.lecom.ui.screen.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.lecom.R
import com.example.lecom.databinding.ActivityMainBinding
import com.example.lecom.utils.DesignSystem

/**
 * Main Activity - Entry point of the application
 * This is the first page that loads when the app starts
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize design system
        DesignSystem.initialize(this)
        
        setupUI()
    }
    
    /**
     * Setup UI components
     */
    private fun setupUI() {
        // Example: Set up toolbar
        setSupportActionBar(binding.toolbar)
        
        // Example: Set up click listeners
        binding.buttonPrimary.setOnClickListener {
            // Handle primary button click
        }
        
        binding.buttonSecondary.setOnClickListener {
            // Handle secondary button click
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Update design system if needed (e.g., theme changes)
        DesignSystem.updateTheme(this)
    }
}

