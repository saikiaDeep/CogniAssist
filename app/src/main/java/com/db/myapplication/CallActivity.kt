package com.db.myapplication

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class CallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        val videoView = findViewById<VideoView>(R.id.videoView)

        // For video in the raw resources directory
        val videoPath = "android.resource://" + packageName + "/" + R.raw.sample_video

        // If your video is in local storage, use a different path, e.g.:
        // val videoPath = "file:///storage/emulated/0/Download/sample_video.mp4"

        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        // Add media controller
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Start the video
        videoView.start()

    }

}