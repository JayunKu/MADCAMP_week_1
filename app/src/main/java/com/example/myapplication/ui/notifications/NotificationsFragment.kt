package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.model.Photo
import com.example.myapplication.ui.notifications.PlaceUserAdapter
import com.example.myapplication.ui.notifications.PlaceUserGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val currentUserId by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUserPhotosRef = FirebaseDatabase.getInstance().getReference("photos")
        val allPhotosRef = FirebaseDatabase.getInstance().getReference("photos")

        // 🔹 Step 1: 내가 올린 장소 목록 수집
        currentUserPhotosRef.get().addOnSuccessListener { snapshot ->
            val myPlaces = mutableSetOf<String>()

            for (child in snapshot.children) {
                val photo = child.getValue(Photo::class.java)
                if (photo != null && photo.userId == currentUserId) {
                    val place = photo.description
                    if (!place.isNullOrBlank()) {
                        myPlaces.add(place)
                    }
                }
            }

            // 🔹 Step 2: 모든 사진에서, 내가 올린 장소에 해당하면서 다른 유저들의 사진만 필터링
            val groupedMap = mutableMapOf<String, MutableSet<String>>()  // 장소 → 유저이름 목록
            val usernameToUid = mutableMapOf<String, String>()            // 유저이름 → UID

            allPhotosRef.get().addOnSuccessListener { snapshot2 ->
                for (child in snapshot2.children) {
                    val photo = child.getValue(Photo::class.java)
                    if (photo != null &&
                        photo.userId != currentUserId &&
                        photo.description in myPlaces
                    ) {
                        val username = photo.userEmail?.substringBefore("@") ?: "Unknown"
                        val place = photo.description ?: "Unknown"

                        groupedMap.getOrPut(place) { mutableSetOf() }.add(username)
                        usernameToUid[username] = photo.userId
                    }
                }

                val groupList = groupedMap.map { (place, users) ->
                    PlaceUserGroup(place, users.toList())
                }

                val adapter = PlaceUserAdapter(groupList, usernameToUid) { clickedUsername ->
                    val receiverId = usernameToUid[clickedUsername]
                    if (receiverId != null) {
                        val bundle = Bundle().apply {
                            putString("receiverId", receiverId)
                            putString("receiverUsername", clickedUsername)
                        }
                        findNavController().navigate(com.example.myapplication.R.id.chatFragment, bundle)
                    }
                }

                binding.groupedUserRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.groupedUserRecyclerView.adapter = adapter
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
