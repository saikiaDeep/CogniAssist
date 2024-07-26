package com.db.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnUser: Button = findViewById(R.id.btnUser)
        val btnCaregiver: Button = findViewById(R.id.btnCaregiver)

        btnUser.setOnClickListener {
            openMasterActivity(btnUser.text.toString())
        }

        btnCaregiver.setOnClickListener {
            openMasterActivity(btnCaregiver.text.toString())

        }
        // alexa demo
        val btnAlexa: FloatingActionButton = findViewById(R.id.floatingActionButton3)
        val url = "https://storage.googleapis.com/plot_normalhrv/normal_chart.png"
        btnAlexa.setOnClickListener {
            val intent = Intent(this, CallActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)

        }

    }

    private fun openMasterActivity(buttonText: String) {
        val intent = Intent(this, MasterActivity::class.java).apply {
            putExtra("BUTTON_TEXT", buttonText)
        }
        startActivity(intent)
        finish()
    }
}

