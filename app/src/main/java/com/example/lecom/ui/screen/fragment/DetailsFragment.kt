package com.example.lecom.ui.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lecom.R
import com.example.lecom.databinding.FragmentDetailsBinding

/**
 * Details screen fragment
 * Shows product detail logic and navigation to settings
 */
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGenerateSummary.setOnClickListener {
            val summary = buildProductSummary(
                name = binding.inputName.text?.toString().orEmpty().ifBlank { "Sample Sneakers" },
                price = binding.inputPrice.text?.toString()?.toDoubleOrNull() ?: 89.99,
                discount = binding.inputDiscount.text?.toString()?.toIntOrNull() ?: 15
            )
            binding.textSummary.text = summary
        }

        binding.buttonGoToSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Demonstrates simple business logic that calculates a discounted price
     * and returns a user-friendly summary.
     */
    private fun buildProductSummary(name: String, price: Double, discount: Int): String {
        val discountedPrice = price * (1 - (discount / 100.0))
        val savings = price - discountedPrice
        return getString(
            R.string.details_summary_template,
            name,
            price,
            discount,
            String.format("%.2f", discountedPrice),
            String.format("%.2f", savings)
        )
    }
}

