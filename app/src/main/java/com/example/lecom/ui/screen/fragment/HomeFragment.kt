package com.example.lecom.ui.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lecom.R
import com.example.lecom.databinding.FragmentHomeBinding
import com.example.lecom.ui.screen.activity.SummaryActivity
import kotlin.random.Random

/**
 * Home screen fragment
 * Presents a high-level overview and sample logic
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProcessOrders.setOnClickListener {
            val orders = generateMockOrders()
            val total = calculateDailyRevenue(orders)
            binding.textResult.text = getString(
                R.string.home_result_template,
                orders.size,
                String.format("%.2f", total)
            )
        }

        binding.buttonGoToDetails.setOnClickListener {
            findNavController().navigate(R.id.detailsFragment)
        }

        binding.buttonOpenSummaryPage.setOnClickListener {
            startActivity(Intent(requireContext(), SummaryActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Simple business logic example that sums daily revenue
     */
    private fun calculateDailyRevenue(orders: List<Double>): Double = orders.sum()

    /**
     * Generates random order amounts to feed into the logic
     */
    private fun generateMockOrders(): List<Double> {
        return List(size = Random.nextInt(3, 8)) {
            Random.nextDouble(25.0, 150.0)
        }
    }
}

