package com.db.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MasterActivity : AppCompatActivity() {
    private val CHANNEL_ID = "example_channel_id"
    private val NOTIFICATION_ID = 101

    private fun createNotificationChannel() {
        // Notification channels are required for Android 8.0 (Oreo) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "This is an example channel for notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        // Build the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background) // Set your own notification icon
            .setContentTitle("Health Notification")
            .setContentText("HRV notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }
    fun playMusic(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.response)
        mediaPlayer.start()

        // Optional: Release the MediaPlayer when the music is finished
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        val buttonText = intent.getStringExtra("BUTTON_TEXT")
        val database = Firebase.database
        val myRef = database.getReference("TRIGGER")

        val specificTriggerRef = myRef.child("trigger")
        specificTriggerRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<Boolean>()
                Log.d(TAG, "Value is: " + value)
                if(value == true)
                {

                    createNotificationChannel()

                    showNotification()
                    sendSMS()
                    playMusic(applicationContext)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // ImageButton 1 (LocationActivity)
        val imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
        imageButton1.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        // ImageButton 2 (ChatActivity)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        imageButton2.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        // ImageButton 3 (HealthActivity)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButton3)
        imageButton3.setOnClickListener {
            val intent = Intent(this, HealthActivity::class.java)
            startActivity(intent)
        }

        // ImageButton 4 (DoctorActivity)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)
        imageButton4.setOnClickListener {
            val msg = "I hope this message finds you well. I am writing to inform you of concerning observations regarding the heart rate of patient, whom I have been monitoring closely.\n" +
                    "\n" +
                    "During recent assessments, Patient has consistently exhibited elevated heart rates, notably exceeding 120 on multiple occasions. This observation is outside the normal range we typically expect for this individual.\n" +
                    "\n" +
                    "I believe it is important for us to discuss these findings further to determine the appropriate course of action. Please let me know a convenient time for us to meet and review the detailed observations. Alternatively, if you prefer, I can arrange a meeting at your earliest convenience.\n" +
                    "\n" +
                    "Thank you for your attention to this matter. I look forward to your prompt response."
            sendEmail("externdroid456@gmail.com","patient report",msg)
        }
        // ImageButton 4 (DoctorActivity)
        val imageButton5 = findViewById<ImageButton>(R.id.imageButton5)
        imageButton5.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

        // FloatingActionButton (CallActivity)
        val floatingActionButton = findViewById<ImageButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
//            Toast.makeText(this,"fab",Toast.LENGTH_LONG).show()
//            val intent = Intent(this, CallActivity::class.java)
//            startActivity(intent)
            openDialerWithNumber("8638050827")
        }
        val floatingActionButtonSOS = findViewById<ImageButton>(R.id.floatingActionButton2)
        if (buttonText=="Caregiver")
        {
         floatingActionButtonSOS.visibility = View.GONE
        }
        // FloatingActionButton2 (CallActivity)

        floatingActionButtonSOS.setOnClickListener {
            openDialerWithNumber("911")
        }
    }

    private fun sendSMS() {
        // Add this in your activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        val message = "The patient's health condition is concerning"
        val phoneNumber = "9833410396"
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(applicationContext, "SMS sent to the CareGiver.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "SMS failed, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun openDialerWithNumber(phoneNumber: String) {
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG)
            .show()

        val u = Uri.parse("tel:" + phoneNumber)

        val i = Intent(Intent.ACTION_DIAL, u)
        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i)
        } catch (s: SecurityException) {
            // show() method display the toast with
            // exception message.
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
                .show()
        }
    }
    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message

            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}