package com.db.myapplication

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolleyRequest(context: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun postRequest(url: String, jsonRequest: JSONObject, listener: VolleyResponseListener) {
        val jsonObjectRequest =
                object :
                        JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonRequest,
                                Response.Listener<JSONObject> { response ->
                                    listener.onResponse(response)
                                },
                                Response.ErrorListener { error ->
                                    listener.onError(error.message ?: "Unknown error")
                                }
                        ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer ${getAccessToken()}"
                        headers["Content-Type"] = "application/json"
                        return headers
                    }
                }

        requestQueue.add(jsonObjectRequest)
    }

    private fun getAccessToken(): String {
        // Add logic to retrieve your access token
        // This could involve calling another service or retrieving it from storage
        return ""
    }
    interface VolleyResponseListener {
        fun onResponse(response: JSONObject)
        fun onError(errorMessage: String)
    }
}
