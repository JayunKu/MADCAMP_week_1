package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private val messageList = mutableListOf<ChatMessage>()

    private val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    }

    //private val receiverId: String = "test_receiver" // ← 실제 상대방 ID를 여기에 설정
    private lateinit var receiverId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.feed_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiverId = arguments?.getString("receiverId") ?: return

        recyclerView = view.findViewById(R.id.chatRecyclerView)
        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<Button>(R.id.btnSend)

        adapter = ChatAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

//        messageList.add(ChatMessage("테스트 메시지", "user123", "user456"))
//        adapter.notifyItemInserted(0)

        // 🔹 Firebase 경로 설정
        val chatRoomId = listOf(currentUserId, receiverId).sorted().joinToString("_")
        database = FirebaseDatabase.getInstance().getReference("chat/$chatRoomId")
        //database = FirebaseDatabase.getInstance().getReference("chat/room1")

        // 🔹 실시간 수신
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg = snapshot.getValue(ChatMessage::class.java)
                if (msg != null &&
                    ((msg.senderId == currentUserId && msg.receiverId == receiverId) ||
                            (msg.senderId == receiverId && msg.receiverId == currentUserId))) {
                    messageList.add(msg)
                    adapter.notifyItemInserted(messageList.size - 1)
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        })

        // 🔹 메시지 전송
        btnSend.setOnClickListener {
            val text = etMessage.text.toString()
            if (text.isNotBlank()) {
                val msg = ChatMessage(
                    message = text,
                    senderId = currentUserId,
                    receiverId = receiverId
                )
                database.push().setValue(msg)
                etMessage.text.clear()
            }
        }
    }
}
