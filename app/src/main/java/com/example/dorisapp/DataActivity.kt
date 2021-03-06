package com.example.dorisapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_data.*
import org.json.JSONException

class DataActivity : AppCompatActivity() {
    lateinit var handler: Handler
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

        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                getLatestCoord()
                handler.postDelayed(this, 3000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
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
                    val x = json.int("xCoord").toString()
                    val y = json.int("yCoord").toString()
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
            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,

                Response.Listener {

                    for (i in 0 until it.length()) {

                        val item = it.getJSONObject(i)
                        val x : Float = item.getString("xCoord").toFloat()
                        val y : Float = item.getString("yCoord").toFloat()
                        val collision : Boolean = item.getBoolean("isCollision")
                        dataItemList.add(CoordinateData("X: $x", "Y: $y", "Collision: $collision"))

                    }

                },

                Response.ErrorListener { error ->
                    error
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
                    val session = json.int("session").toString()

                    if (session != null) {
                        getCoordArray(session)

                    } else {
                    }
                },

                Response.ErrorListener { error ->
                    error
                }
            )
            queue.add(jsonObjectRequest)
        } catch (error: JSONException) {
        }
    }
}