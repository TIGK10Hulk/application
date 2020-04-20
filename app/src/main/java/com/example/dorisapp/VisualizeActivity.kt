package com.example.dorisapp

import android.app.DownloadManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONArray
import kotlin.math.sinh

class VisualizeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_visualize, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_visualize, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        var x: Double = 0.0
        var y: Double = 0.0

        var graph : GraphView = findViewById(R.id.graph)
        var series : LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()

        var queue = Volley.newRequestQueue(this)
        var url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            // On Success
            Response.Listener { result -> result

                var coords =  Klaxon().parseArray<CoordinateData>(result.toString())

                println(coords)

              /*  val coordinates = Klaxon().<>(it.toString())!!

                var xCord: Double = coordinates.xCoord
                var yCord: Double = coordinates.yCoord

                series.appendData(DataPoint(xCord, yCord), true, 50)
                graph.addSeries(series)

               */

            },

            Response.ErrorListener { error -> error
                println(error)
            }
            )
        queue.add(jsonObjectRequest)

       /* x = -0.5

        for (i in 0..50)
        {
            x += 0.1
            y = sinh(x)
            series.appendData(DataPoint(x,y), true, 500)
        }

        graph.addSeries(series)
        */
        // val series: LineGraphSeries<DataPoint> = LineGraphSeries {DataPoint(0.1, 0.2)}
    }

    fun getCordsForGraph() {

    }




}