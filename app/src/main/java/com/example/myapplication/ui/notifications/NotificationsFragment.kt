package com.example.myapplication.ui.dashboard

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
        val groupedMap = mutableMapOf<String, MutableSet<String>>()  // 장소 → 유저이름 목록
        val usernameToUid = mutableMapOf<String, String>()            // 유저이름 → UID

        FirebaseDatabase.getInstance().getReference("photos")
            .get()
            .addOnSuccessListener { snapshot ->
                for (child in snapshot.children) {
                    val photo = child.getValue(Photo::class.java)
                    if (photo != null && photo.userId != currentUserId) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
