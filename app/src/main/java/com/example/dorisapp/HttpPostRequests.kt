package com.example.dorisapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONObject


fun sendCoordinate(context: Context, coord: Coord){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"
    val myCoord = Coord(coord.xCoord, coord.yCoord, coord.isCollision, coord.session)
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