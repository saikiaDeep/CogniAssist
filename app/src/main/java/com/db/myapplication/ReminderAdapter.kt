package com.db.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReminderAdapter(private val reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.bind(reminder)
    }

    override fun getItemCount(): Int = reminders.size

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val frequencyTextView: TextView = itemView.findViewById(R.id.tvFrequency)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val timeTextView: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(reminder: Reminder) {
            frequencyTextView.text = reminder.frequency
            nameTextView.text = reminder.name
            timeTextView.text = reminder.time
        }
    }
}
