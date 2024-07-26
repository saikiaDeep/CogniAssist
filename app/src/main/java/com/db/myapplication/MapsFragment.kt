package com.db.myapplication

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.maps.route.extensions.drawMarker
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.getTravelEstimations
import com.maps.route.extensions.moveCameraOnMap
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

class MapsFragment : Fragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var latitude:String = "0"
    private var longitude:String = "0"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)

    }

    override fun onDestroy() {

        super.onDestroy()
        activity?.finish()
    }
    private val latch = CountDownLatch(1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //initialized google maps
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val database = Firebase.database
        val myRef = database.getReference("current")
        val lat = myRef.child("LAT")
        val lng = myRef.child("LNG")
        lng.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val value = snapshot?.getValue<String>()
                Log.d("Dolley", "Value is: $value")
                if (value != null) {
                    longitude = value
                    getData()
                }
            } else {
                Log.w("Dolley", "Failed to read value.", task.exception)
            }
        }
        lat.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val value = snapshot?.getValue<String>()
                Log.d("Dolley", "Value is: $value")
                if (value != null) {
                    latitude = value
                    getData()
                }
            } else {
                Log.w("Dolley", "Failed to read value.", task.exception)
            }
        }

        mapFragment.getMapAsync(this)




    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0


    }

    private fun getData() {
        val source = LatLng(latitude.toDouble(), longitude.toDouble()) //starting point (LatLng)
        val destination = LatLng(18.503062, 73.924218) // ending point (LatLng)



        googleMap?.run {
            //if you want to move the map on specific location
            moveCameraOnMap(latLng = source)

            //if you want to drop a marker of maps, call it
            drawMarker(location = source, context = requireContext(), title = "test marker", resDrawable = R.drawable.ic_location_green)
            drawMarker(location = destination, context = requireContext(), title = "test marker", resDrawable = R.drawable.ic_location_home)
            //if you only want to draw a route on maps
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                "API KEY",
                source = source,
                destination = destination,
                context = requireContext(),
                markers = false
            )

        }




    }

    private fun resizeBitmap(drawableRes: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(resources, drawableRes)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


}