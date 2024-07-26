package com.db.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val reminders = listOf(
            Reminder("Daily", "Diet Alert", "09:00 AM"),
            Reminder("Daily", "Breathing exercise", "07:00 AM"),
            Reminder("Daily", "Take a walk", "08:00 AM"),
            Reminder("Daily", "Sleep", "10:00 PM"),
            Reminder("Monthly", "Doctor's Appointment", "10:00 AM")
        )

        val adapter = ReminderAdapter(reminders)
        recyclerView.adapter = adapter
    }
}