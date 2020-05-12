package com.example.dorisapp

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_data.*
import org.json.JSONException

class DataActivity : AppCompatActivity() {

    var previousX = ""
    var previousY = ""

    var dataItemList = ArrayList<CoordinateData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        HelperFunctions.initializeContentView(R.layout.activity_data, findViewById(android.R.id.content), layoutInflater) //Adds activity_data.xml to current view
        HelperFunctions.initializeDrawerListeners(R.layout.activity_data, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        var toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Coordinate list"

        val dataRecycler = findViewById(R.id.dataRecycler) as RecyclerView

        dataRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        getSession()

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                getLatestCoord()
                handler.postDelayed(this, 3000)
            }
        })
    }

    private fun getLatestCoord() {

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()
                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val x = json.string("xCoord")
                    val y = json.string("yCoord")
                    val collision = json.boolean("isCollision")

                    if (x != null && y != null) {
                        if (previousX != x || previousY != y) {
                            dataItemList.add(CoordinateData("X: $x", "Y: $y", "Collision: $collision"))
                            previousX = x
                            previousY = y
                        }
                    }

                },

                Response.ErrorListener { error ->
                    error
                    println(error)

                }
            )
            queue.add(jsonObjectRequest)
        } catch (error: JSONException) {

        }

        val adapter = DataListAdapter (dataItemList)

        dataRecycler.adapter = adapter

    }

    private fun getCoordArray(session: String?) {

        val queue = Volley.newRequestQueue(this)
        val url =
            "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/sessions/$session"

        try {
            val jsonArrayRequest = JsonObjectRequest(
                Request.Method.GET, url, null,

                Response.Listener {
                    val parser: Parser = Parser.default()
                    val posArray = it.getString("positions")
                    val stringBuilder: StringBuilder = StringBuilder(posArray)
                    val array: JsonArray<JsonObject> =
                        parser.parse(stringBuilder) as JsonArray<JsonObject>
                    println(array.toJsonString())

                    array.forEach { i ->
                        i
                        val x = i.string("xCoord")
                        val y = i.string("yCoord")
                        val collision = i.boolean("isCollision")
                        dataItemList.add(CoordinateData("X: $x", "Y: $y", "Collision: $collision"))

                    }

                },

                Response.ErrorListener { error ->
                    error
                    println(error)

                })
            queue.add(jsonArrayRequest)
        } catch (error: JSONException) {

        }

    }

    private fun getSession() {

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()

                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val session = json.string("session")

                    if (session != null) {
                        getCoordArray(session)

                    } else {
                    }
                },

                Response.ErrorListener { error ->
                    error
                    println(error)

                }
            )
            queue.add(jsonObjectRequest)
        } catch (error: JSONException) {
            println(error)
        }

    }
}