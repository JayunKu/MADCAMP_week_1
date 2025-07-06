package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.CalendarTripDetailBinding
import android.widget.TextView
import com.example.myapplication.R

class TripDetailFragment : Fragment() {
    private var _binding: CalendarTripDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CalendarTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val place = args?.getString("place") ?: ""
        val start = args?.getString("startDate") ?: ""
        val end = args?.getString("endDate") ?: ""
        val plan = args?.getString("planDetail") ?: ""

        binding.tvDetailPlace.text = place
        binding.tvDetailDate.text = "$start ~ $end"

        showParsedPlan(plan)
    }

    private fun showParsedPlan(plan: String) {
        val container = binding.detailContainer
        val lines = plan.split("\n")

        for (line in lines) {
            if (line.matches(Regex("""\d{2}/\d{2}"""))) {
                // 날짜 박스
                val dateView = TextView(requireContext()).apply {
                    text = line
                    setBackgroundResource(R.drawable.rounded_day_background)
                    setTextColor(android.graphics.Color.WHITE)
                    textSize = 18f
                    setPadding(24, 12, 24, 12)
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 32, 0, 16)
                    }
                }
                container.addView(dateView)
            } else if (line.isNotBlank()) {
                // 시간 + 내용
                val planItem = TextView(requireContext()).apply {
                    text = line
                    textSize = 16f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                container.addView(planItem)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
