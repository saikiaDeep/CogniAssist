package com.db.myapplication

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HealthActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    fun loadImageFromUrl(context: Context, imageView: ImageView, url: String) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        var url = "https://storage.googleapis.com/plot_normalhrv/normal_chart_zeta.png"
        //val url = intent.getStringExtra("URL")
//        webView = findViewById(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//        url?.let { webView.loadUrl(it) }
        val imageView: ImageView = findViewById(R.id.imageView)
        val database = Firebase.database
        val myRef = database.getReference("TRIGGER")

        val specificTriggerRef = myRef.child("trigger")
        specificTriggerRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val value = snapshot?.getValue<Boolean>()
                Log.d("Dolley", "Value is: $value")
                if (value != null) {
                    if (value == true)
                    {
                        url = "https://storage.googleapis.com/plot_normalhrv/abnormal_chart_upsilon.png"
                    }
                    loadImageFromUrl(this, imageView, url)


                }
            } else {
                Log.w("Dolley", "Failed to read value.", task.exception)
            }
        }


    }
}