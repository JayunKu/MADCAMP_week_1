package com.example.myapplication.ui.notifications

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.notifications.PlaceUserGroup

class PlaceUserAdapter(
    private val groups: List<PlaceUserGroup>,
    private val userIdMap: Map<String, String>,
    private val onUserClick: (String) -> Unit  // ✅ 추가
) : RecyclerView.Adapter<PlaceUserAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlace: TextView = itemView.findViewById(R.id.tvPlace)
        val container: LinearLayout = itemView.findViewById(R.id.userContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place_user_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.tvPlace.text = group.place
        holder.container.removeAllViews()

        for (username in group.users) {
            val userTextView = TextView(holder.itemView.context).apply {
                text = username
                textSize = 16f
                setPadding(16)
                setTextColor(resources.getColor(R.color.light_purple, null))
                setOnClickListener {
                    onUserClick(username)  // ✅ 클릭 시 콜백 호출
                }
            }
            holder.container.addView(userTextView)
        }
    }

    override fun getItemCount(): Int = groups.size
}

