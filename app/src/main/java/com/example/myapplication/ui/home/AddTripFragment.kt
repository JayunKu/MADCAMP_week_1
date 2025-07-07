package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.CalendarAddTripBinding
import com.example.myapplication.R
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AppCompatActivity




class AddTripFragment : Fragment() {

    private var _binding: CalendarAddTripBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CalendarAddTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveTrip.setOnClickListener {
            val place = binding.etPlace.text.toString()
            val date = binding.etDate.text.toString()
            val plan = binding.etPlan.text.toString()
            //Toast.makeText(requireContext(), "입력값: $place, $date, $plan", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(), "여행 계획이 등록되었습니다!", Toast.LENGTH_SHORT).show()
            // 여기에 실제 저장/이동 동작 넣으면 됨

            val dates = date.split("~").map { it.trim() }
            val startDate = dates.getOrNull(0) ?: ""
            val endDate = dates.getOrNull(1) ?: ""

            val result = Bundle().apply {
                putString("place", place)
                putString("date", date)
                putString("plan", plan)
            }

            parentFragmentManager.setFragmentResult("addTripResult", result)

            findNavController().navigate(R.id.navigation_home)
//            val trip = TripPlan(
//                imageResId = R.drawable.main,
//                placeName = place,
//                startDate = startDate,
//                endDate = endDate,
//                planDetail = plan
//            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
