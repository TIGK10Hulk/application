package com.example.dorisapp

import android.content.Context
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONObject

public fun examplePostRequest(context: Context){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/position"
    val myCoord = Coord(105, 105, false)
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

}