package com.example.myapplication.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.google.firebase.database.FirebaseDatabase
import com.example.myapplication.model.Photo
import com.example.myapplication.ui.dashboard.PhotoAdapter
import com.google.firebase.auth.FirebaseAuth

import androidx.navigation.fragment.findNavController
import android.os.Handler
import android.os.Looper


class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val database = FirebaseDatabase.getInstance().getReference("photos")
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // 예: 로그인 화면으로 이동하거나 메시지 표시
            return root
        }
        val userid = user!!.uid

        database.get().addOnSuccessListener { snapshot ->
            val photoList = mutableListOf<Photo>()
            for (child in snapshot.children) {
                val photo = child.getValue(Photo::class.java)
                if (photo != null&&photo.userId!=userid) {
                    photoList.add(photo)
                }
            }

            val adapter = PhotoAdapter(photoList) // 필요시 context 전달
            recyclerView.adapter = adapter
        }

        Handler(Looper.getMainLooper()).post {
            val bundle = Bundle().apply {
                putString("receiverId", "tIWt7rsdl4agzP7KV3sLk8DaT843") // 임시로 상대방 UID 지정
            }
            findNavController().navigate(com.example.myapplication.R.id.chatFragment, bundle)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
