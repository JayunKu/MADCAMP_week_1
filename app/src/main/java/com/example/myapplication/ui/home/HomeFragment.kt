package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tripAdapter: TripAdapter
    companion object {
        val tripList = mutableListOf<TripPlan>()
    }

    //private val tripList = mutableListOf<TripPlan>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 🔹 기존 텍스트 관찰자 설정
        val textView: TextView = binding.textCalendar
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // 🔹 RecyclerView 어댑터 설정
        //tripAdapter = TripAdapter(tripList)
        tripAdapter = TripAdapter(tripList) { trip ->
            val bundle = Bundle().apply {
                putString("place", trip.placeName)
                putString("startDate", trip.startDate)
                putString("endDate", trip.endDate)
                putString("planDetail", trip.planDetail)
            }
            findNavController().navigate(R.id.tripDetailFragment, bundle)
        }

        binding.recyclerTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTrips.adapter = tripAdapter

        // 🔹 + 버튼 클릭 시 여행 추가 화면으로 이동
        binding.fabAddTrip.setOnClickListener {
            findNavController().navigate(
                com.example.myapplication.R.id.addTripFragment
            )
        }

        val hintText = binding.emptyHintText

        parentFragmentManager.setFragmentResultListener("addTripResult", viewLifecycleOwner) { _, bundle ->
            val place = bundle.getString("place") ?: return@setFragmentResultListener
            val date = bundle.getString("date") ?: ""
            val plan = bundle.getString("plan") ?: ""

            val trip = TripPlan(
                placeName = place,
                startDate = date.split("~").getOrNull(0)?.trim() ?: "",
                endDate = date.split("~").getOrNull(1)?.trim() ?: "",
                imageResId = com.example.myapplication.R.drawable.main, // 예시 이미지
                planDetail = plan
            )

            hintText.visibility = View.GONE

            tripAdapter.addTrip(trip)
            binding.recyclerTrips.scrollToPosition(0)
        }

        hintText.visibility = if (tripList.isEmpty()) View.VISIBLE else View.GONE
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
