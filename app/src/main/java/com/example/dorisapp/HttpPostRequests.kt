package com.example.dorisapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import org.json.JSONArray
import org.json.JSONObject


fun sendCoordinate(context: Context, coord: Coord){
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"
    val myCoord = Coord(coord.xCoord, coord.yCoord, coord.isCollision, RobotData.session)
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

fun pushListOfCoordinates(context: Context, coords: ArrayList<Coord>?) {
    if(RobotData.unpushedCoords == null){
        return
    }

    var stringList: ArrayList<String> = ArrayList()
    for(coord in RobotData.unpushedCoords!!){
        stringList.add(Klaxon().toJsonString(coord))
    }

    var jsonArray: JSONArray = JSONArray(stringList)
    println("JSONARR:::: $jsonArray")
    val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/array"

    val queue = Volley.newRequestQueue(context)
    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.POST, url, jsonArray,
        Response.Listener<JSONArray> { response ->
            println(response.toString())
        },
        Response.ErrorListener { error -> println("Error: $error") }
    )

    //Todo push list of coords to the backend
    queue.add(jsonArrayRequest)

    //After List of Coordinates has been pushed, reset list to null
    RobotData.unpushedCoords = null
    RobotData.unpushedCoords = ArrayList()

}