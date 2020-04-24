package com.example.dorisapp

import android.app.DownloadManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_visualize.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.util.logging.Level.parse
import kotlin.math.sinh

class VisualizeActivity: AppCompatActivity() {

   /* var image: ImageView = findViewById(R.id.chart)
    var bitMap : Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
    private val paint : Paint = Paint(Color.BLACK)

    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_visualize, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_visualize, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        val session = getSession()
        getListOfCordsForGraph(session)

        val handler = Handler(Looper.getMainLooper())
        handler.post( object : Runnable {
            override fun run() {
                getLatestCordsForGraph()
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun getLatestCordsForGraph() {

        var x : Float = 0.0F
        var y : Float = 0.0F
        var previousX : Float = 0.0F
        var previousY : Float = 0.0F

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            // On Success
            Response.Listener {

                val parser: Parser = Parser.default()
                val pos = it.getString("position")
                println(pos)
                val stringBuilder: StringBuilder = StringBuilder(pos)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                val x = json.string("xCoord")
                val y = json.string("yCoord")
                val xInt= x!!.toInt()
                val yInt = y!!.toInt()
                val sum = xInt + yInt
                println(sum)

               /* if (coords != null) {
                    if (coords.xCoordinateValue.toFloat() != x || coords.yCoordinateValue.toFloat() != y) {
                        x = coords.xCoordinateValue.toFloat()
                        y = coords.yCoordinateValue.toFloat()

                        val tempBitmap : Bitmap = Bitmap.createBitmap(bitMap.width, bitMap.height, Bitmap.Config.ARGB_8888)
                        val tempCanvas = Canvas(tempBitmap)

                        tempCanvas.drawBitmap(bitMap, 0.0F, 0.0F, null)
                        tempCanvas.drawLine(previousX, previousY, x, y, paint)
                        image.setImageDrawable(BitmapDrawable(resources, tempBitmap))

                        previousX = x
                        previousY = y

                    }
                }

                */
            },

            Response.ErrorListener { error -> error
                println(error)
            }
        )
        queue.add(jsonObjectRequest)

    }

    private fun getListOfCordsForGraph(session: Int) {

        var x : Float = 0.0F
        var y : Float = 0.0F
        var previousX : Float = 0.0F
        var previousY : Float = 0.0F

        println(session)

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/$session"

        //tempCanvas.drawBitmap(bitMap, 0.0F, 0.0F, null)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,

            Response.Listener {
                val array = Klaxon().parseArray<JSONObject>(it.toString())

                println(array)
            },

            Response.ErrorListener { error -> error
                println(error)
            })
        queue.add(jsonArrayRequest)

    }

    private fun getSession() : Int {

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"
        var sessionInt = 0

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            // On Success
            Response.Listener {

                val parser: Parser = Parser.default()

                val pos = it.getString("position")
                val stringBuilder: StringBuilder = StringBuilder(pos)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                val session = json.string("session")
                println(session)

                if (session != null) {
                    sessionInt = session.toInt()
                }
            },

            Response.ErrorListener { error -> error
                println(error)
            }
        )
        queue.add(jsonObjectRequest)

        return sessionInt
    }

}

