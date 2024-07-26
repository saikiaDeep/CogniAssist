package com.db.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    private lateinit var chatsRV: RecyclerView
    private lateinit var sendMsgIB: ImageButton
    private lateinit var userMsgEdt: EditText
    private val USER_KEY = "user"
    private val BOT_KEY = "bot"
    private val API_KEY = ""

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var messageModelArrayList: ArrayList<MessageModel>
    private lateinit var messageRVAdapter: MessageRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatsRV = findViewById(R.id.idRVChats)
        sendMsgIB = findViewById(R.id.idIBSend)
        userMsgEdt = findViewById(R.id.idEdtMessage)

        mRequestQueue = Volley.newRequestQueue(this@ChatActivity)
        mRequestQueue.cache.clear()

        messageModelArrayList = ArrayList()

        sendMsgIB.setOnClickListener {
            if (userMsgEdt.text.toString().isEmpty()) {

                Toast.makeText(this@ChatActivity, "Please enter your message..", Toast.LENGTH_SHORT)
                        .show()
                return@setOnClickListener
            }

            send(userMsgEdt.text.toString())

            userMsgEdt.setText("")
        }

        messageRVAdapter = MessageRVAdapter(messageModelArrayList, this)

        val linearLayoutManager =
                LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)

        chatsRV.layoutManager = linearLayoutManager

        chatsRV.adapter = messageRVAdapter
    }
    private fun send(userMsg: String) {
        messageModelArrayList.add(MessageModel(userMsg, USER_KEY))
        messageRVAdapter.notifyDataSetChanged()
        val accessToken = ""
        val url =
                "https://discoveryengine.googleapis.com/v1alpha/projects/1045173208486/locations/global/collections/default_collection/dataStores/cognious-final_1721306613043/servingConfigs/default_search:search"

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $accessToken"
        headers["Content-Type"] = "application/json"

        val jsonBody = JSONObject()
        jsonBody.put("query", userMsg)
        jsonBody.put("pageSize", 10)
        jsonBody.put("queryExpansionSpec", JSONObject().put("condition", "AUTO"))
        jsonBody.put("spellCorrectionSpec", JSONObject().put("mode", "AUTO"))

        val contentSearchSpec = JSONObject()
        val summarySpec = JSONObject()
        summarySpec.put("ignoreAdversarialQuery", true)
        summarySpec.put("includeCitations", true)
        summarySpec.put("summaryResultCount", 5)
        summarySpec.put(
                "modelSpec",
                JSONObject().put("version", "gemini-1.0-pro-002/answer_gen/v1")
        )
        contentSearchSpec.put("summarySpec", summarySpec)
        contentSearchSpec.put("snippetSpec", JSONObject().put("returnSnippet", true))
        contentSearchSpec.put(
                "extractiveContentSpec",
                JSONObject().put("maxExtractiveAnswerCount", 1)
        )

        jsonBody.put("contentSearchSpec", contentSearchSpec)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest =
                object :
                        JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonBody,
                                Response.Listener { response ->
                                    // Handle the response
                                    println("Response: $response")
                                    Log.d("VolleyRes", response.toString())
                                    val summaryText = response.optJSONObject("summary")
                                    Log.d(
                                            "volley",
                                            "The type of 'number' is: ${summaryText::class.simpleName}"
                                    )

                                    if (summaryText != null) {
                                        Log.d("VolleyRes", summaryText.toString())
                                    }

                                    println(summaryText.toString())
                                    val jsonObject = JSONObject(summaryText.toString())

                                    // Extract the 'summaryText' field
                                    val summaryText2 = jsonObject.getString("summaryText")

                                    messageModelArrayList.add(
                                            MessageModel(summaryText2.toString(), BOT_KEY)
                                    )
                                    messageRVAdapter.notifyDataSetChanged()
                                    if (summaryText2 != null) {
                                        Log.d("VolleyRes", summaryText2.toString())
                                    } else {
                                        Log.d("VolleyRes", "error")
                                    }
                                    //                val summary =
                                    // response.getJSONObject("summary")
                                    //                val summaryText =
                                    // summary.getString("summaryText")
                                    //                Log.d("VolleyRes", summaryText)
                                    //                val summaryText =
                                    //
                                    // response.optJSONObject("summary")?.optString("summaryText",
                                    // "") ?: ""
                                    //                println(summaryText)
                                    //                Log.d("VolleyRes", summaryText)

                                },
                                Response.ErrorListener { error ->
                                    // Handle the error
                                    error.printStackTrace()
                                    Log.e("VolleyRes", error.toString())
                                }
                        ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        return headers
                    }
                }
        jsonObjectRequest.retryPolicy =
                DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

        requestQueue.add(jsonObjectRequest)
    }
    private fun sendMessage(userMsg: String) {

        messageModelArrayList.add(MessageModel(userMsg, USER_KEY))
        messageRVAdapter.notifyDataSetChanged()
        val url =
                "https://discoveryengine.googleapis.com/v1alpha/projects/1045173208486/locations/global/collections/default_collection/dataStores/cognious-final_1721306613043/conversations/-:converse"

        val jsonRequest =
                JSONObject(
                        """
            {
                "query": {"input": "demantia"},
                "summarySpec": {
                    "ignoreAdversarialQuery": true,
                    "includeCitations": true,
                    "summaryResultCount": 5,
                    "modelSpec": {"version": "gemini-1.0-pro-002/answer_gen/v1"},
                    "modelPromptSpec": {
                        "preamble": "Given the conversation between a user and a helpful assistant and some search results, create a final answer for the assistant. The answer should use all relevant information from the search results, not introduce any additional information, and use exactly the same words as the search results when possible. The assistant's answer should be no more than 20 sentences. The user is a member of the general public who doesn't have in-depth knowledge of the subject matter. The assistant should avoid using specialized knowledge, and instead answer in a non-technical manner that anyone can understand."
                    }
                }
            }
        """
                )
        Log.d("VolleyInput", "Response: $jsonRequest")
        val volleyRequest = VolleyRequest(this)

        volleyRequest.postRequest(
                url,
                jsonRequest,
                object : VolleyRequest.VolleyResponseListener {
                    override fun onResponse(response: JSONObject) {
                        // Handle the response
                        Log.d("VolleyResponse", "Response: $response")
                        // Process your JSON response here
                        //                val replyObject = response.getJSONObject("reply")
                        //                val replyMessage = replyObject.getString("reply")
                        // Log.d("ExtractedMessage", "Extracted Message: $replyMessage")
                        //                messageModelArrayList.add(MessageModel(replyMessage,
                        // BOT_KEY))
                        //                messageRVAdapter.notifyDataSetChanged()
                    }

                    override fun onError(errorMessage: String) {
                        // Handle errors
                        Log.e("VolleyError", "Error: $errorMessage")
                    }
                }
        )
        //        val url =
        // "http://api.brainshop.ai/get?bid=182709&key=tdFtF8zBznlg9w1n&uid=[$API_KEY]&msg=[$userMsg]"
        //        val volleyRequest = VolleyRequest(this)
        //
        //        volleyRequest.getRequest(url, object : VolleyRequest.VolleyResponseListener {
        //            override fun onResponse(response: JSONObject) {
        //                // Handle the response
        //                Log.d("VolleyResponse", "Response: $response")
        //                // Process your JSON response here
        //                val botResponse = response.getString("cnt")
        //                println(botResponse)
        //                messageModelArrayList.add(MessageModel(botResponse, BOT_KEY))
        //                messageRVAdapter.notifyDataSetChanged()
        //            }
        //
        //            override fun onError(errorMessage: String) {
        //                // Handle errors
        //                Log.e("VolleyError", "Error: $errorMessage")
        //                messageModelArrayList.add(MessageModel("Sorry no response found",
        // BOT_KEY))
        //                messageRVAdapter.notifyDataSetChanged()
        //                Toast.makeText(this@ChatActivity, "No response from the bot..",
        // Toast.LENGTH_SHORT).show()
        //
        //            }
        //        })

    }
}
