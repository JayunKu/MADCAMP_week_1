package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarTripDetailBinding

class TripDetailFragment : Fragment() {

    private var _binding: CalendarTripDetailBinding? = null
    private val binding get() = _binding!!

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // ✅ 기기 백버튼 콜백
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                requireActivity().onBackPressedDispatcher.onBackPressed()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }

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

        // ✅ 툴바 설정
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            title = place
            setDisplayHomeAsUpEnabled(true)
        }

        // ✅ 툴바 메뉴 이벤트를 수신할 수 있도록 설정
        setHasOptionsMenu(true)

        // ✅ 일정 출력
        showParsedPlan(plan)
    }

    // ✅ 툴바 ← 버튼 클릭 시 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 80, 0, 16)
                        gravity = Gravity.START
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
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = null
    }
}
