package com.example.lecom.ui.screen.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lecom.databinding.ActivitySummaryBinding

/**
 * Simple page that can be launched directly from any button click.
 * Demonstrates navigation outside of the Navigation Component graph.
 */
class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonClose.setOnClickListener { finish() }
    }
}

