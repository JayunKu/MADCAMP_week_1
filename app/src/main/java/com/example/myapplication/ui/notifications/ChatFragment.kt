package com.example.myapplication.ui.notifications  // ← 실제 패키지명

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.notifications.ChatAdapter
import com.example.myapplication.ui.notifications.ChatMessage
import com.example.myapplication.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError



class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feed_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("chat/room1")

        recyclerView = view.findViewById(R.id.chatRecyclerView)
        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<Button>(R.id.btnSend)

        adapter = ChatAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔹 메시지 수신
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg = snapshot.getValue(ChatMessage::class.java)
                if (msg != null) {
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
                val msg = ChatMessage(text, isUser = true)
                database.push().setValue(msg)
                etMessage.text.clear()
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        recyclerView = view.findViewById(R.id.chatRecyclerView)
//        val etMessage = view.findViewById<EditText>(R.id.etMessage)
//        val btnSend = view.findViewById<Button>(R.id.btnSend)
//
//        adapter = ChatAdapter(messageList)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        btnSend.setOnClickListener {
//            val msg = etMessage.text.toString()
//            if (msg.isNotBlank()) {
//                messageList.add(ChatMessage(msg, isUser = true))
//                adapter.notifyItemInserted(messageList.size - 1)
//                recyclerView.scrollToPosition(messageList.size - 1)
//                etMessage.text.clear()
//
//                // 간단한 자동 응답
//                messageList.add(ChatMessage("🤖 자동응답: \"$msg\" 잘 들었습니다.", isUser = false))
//                adapter.notifyItemInserted(messageList.size - 1)
//                recyclerView.scrollToPosition(messageList.size - 1)
//            }
//        }
//    }
}
