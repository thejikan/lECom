package com.example.lecom.ui.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lecom.R
import com.example.lecom.databinding.FragmentSettingsBinding

/**
 * Settings screen fragment
 * Provides an example of boolean decision logic
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEvaluate.setOnClickListener {
            val cartValue = binding.inputCartValue.text?.toString()?.toDoubleOrNull() ?: 0.0
            val threshold = binding.inputThreshold.text?.toString()?.toDoubleOrNull() ?: 200.0
            val eligible = shouldEnableFreeShipping(cartValue, threshold)
            binding.textDecision.text = if (eligible) {
                getString(R.string.settings_free_shipping_yes, cartValue)
            } else {
                getString(R.string.settings_free_shipping_no, threshold - cartValue)
            }
        }

        binding.buttonBackHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Simple rule showing how configurable thresholds can be handled.
     */
    private fun shouldEnableFreeShipping(cartValue: Double, threshold: Double): Boolean =
        cartValue >= threshold
}

