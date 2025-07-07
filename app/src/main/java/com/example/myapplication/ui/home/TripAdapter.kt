package com.example.myapplication.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class TripAdapter(private val tripList: MutableList<TripPlan>, private val onItemClick: (TripPlan) -> Unit) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    fun addTrip(trip: TripPlan) {
        tripList.add(0, trip)
        notifyItemInserted(0)
    }

    // 🔹 뷰홀더: 한 줄짜리 뷰 묶음 (calendar_trip_item.xml과 연결됨)
    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPlace: ImageView = itemView.findViewById(R.id.imgPlace)
        val tvPlaceName: TextView = itemView.findViewById(R.id.tvPlaceName)
        val tvDates: TextView = itemView.findViewById(R.id.tvDates)
    }

    // 🔹 한 줄짜리 뷰를 실제로 만들어줌 (XML → View 객체)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_trip_item, parent, false)
        return TripViewHolder(view)
    }

    // 🔹 각 줄에 데이터를 넣어주는 부분
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.imgPlace.setImageResource(trip.imageResId)
        holder.tvPlaceName.text = trip.placeName
        holder.tvDates.text = "${trip.startDate} ~ ${trip.endDate}"

        holder.itemView.setOnClickListener {
            onItemClick(trip) // 클릭 시 외부에서 정의한 동작 실행
        }
    }

    override fun getItemCount(): Int = tripList.size
}
