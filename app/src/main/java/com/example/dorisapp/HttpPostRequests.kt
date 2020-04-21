package com.example.dorisapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONObject


/*public fun examplePostRequest(context: Context){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"
    val myCoord = Coord(105, 105, false, session = 1)
    val myJson = Klaxon().toJsonString(myCoord)
    val jsontwo = JSONObject(myJson)
    val queue = Volley.newRequestQueue(context)
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.POST, url, jsontwo,
        Response.Listener<JSONObject> { response ->
            println(response.toString())
        },
        Response.ErrorListener { error -> println("Error: $error") }
    )
    queue.add(jsonObjectRequest)

}*/


//TEST!!!!!!! Singleton
/*class RequestQueueSingleton private constructor(private val context: Context) {
    private var requestQueue: RequestQueue?
    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            requestQueue =
                Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue
    }

    companion object {
        private var requestQueueSingleton: RequestQueueSingleton? = null

        @Synchronized
        fun getInstance(context: Context): RequestQueueSingleton? {
            if (requestQueueSingleton == null) {
                requestQueueSingleton =
                    RequestQueueSingleton(context)
            }
            return requestQueueSingleton
        }
    }

    init {
        requestQueue = getRequestQueue()
    }
}*/




fun sendCoordinate(context: Context){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"
    val myCoord = Coord(105, 105, false, session = 1)
    val myJson = Klaxon().toJsonString(myCoord)
    val jsonObject = JSONObject(myJson)
    val queue = Volley.newRequestQueue(context)
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.POST, url, jsonObject,
        Response.Listener<JSONObject> { response ->
            println(response.toString())
        },
        Response.ErrorListener { error -> println("Error: $error") }
    )
    queue.add(jsonObjectRequest)
}

fun getCoordinate(context: Context){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"
    val queue = Volley.newRequestQueue(context)
    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
        Response.Listener { response ->
            println(response.toString())
        },
        Response.ErrorListener { error ->
            Response.ErrorListener { error -> println("Error: $error")
        }
        }
    )

// Add the request to the RequestQueue.
    queue.add(jsonObjectRequest)
}