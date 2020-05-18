package com.example.dorisapp

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.logging.Level.parse


class VisualizeActivity : AppCompatActivity() {

    var previousX = 0F
    var previousY = 0F
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        HelperFunctions.initializeContentView(
            R.layout.activity_visualize,
            findViewById(android.R.id.content),
            layoutInflater
        ) //Adds activity_main.xml to current view
        HelperFunctions.initializeDrawerListeners(
            R.layout.activity_visualize,
            findViewById(android.R.id.content),
            this
        ) //Initialize button listeners for navigation system

        var toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Coordinate graph"

        val image = setImageView()
        val bitMap = setBitMap(image)

        val tempCanvas = Canvas(bitMap)
        tempCanvas.drawBitmap(bitMap, 0F, 0F, null)

        val axisPaint = Paint(Color.WHITE)
        axisPaint.color = Color.WHITE
        tempCanvas.drawLine(
            0F,
            bitMap.height / 2F,
            bitMap.width.toFloat(),
            bitMap.height / 2F,
            axisPaint
        )
        tempCanvas.drawLine(
            bitMap.width / 2F,
            0F,
            bitMap.width / 2F,
            bitMap.height.toFloat(),
            axisPaint
        )
        image.setImageDrawable(BitmapDrawable(resources, bitMap))

        getSession(image, bitMap)

        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                getLatestCoord(image, bitMap)
                handler.postDelayed(this, 1000)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setImageView(): ImageView {

        val image: ImageView = findViewById(R.id.chart)
        return image

    }

    private fun setBitMap(image: ImageView): Bitmap {

        return Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    }

    private fun getLatestCoord(image: ImageView, bitMap: Bitmap) {

        val statusText: TextView = findViewById(R.id.visualizeStatusText)
        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()
                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val x = json.int("xCoord").toString()
                    val y = json.int("yCoord").toString()
                    val collision = json.boolean("isCollision")

                    if (x != null && y != null) {
                        val xFloat = x.toFloat()
                        val yFloat = y.toFloat()

                        if (previousX != xFloat || previousY != yFloat) {

                            paint(xFloat, yFloat, collision!!, bitMap, image)
                        }
                    }

                },

                Response.ErrorListener { error ->
                    error
                    println(error)
                    statusText.text = resources.getString(R.string.visualize_coord_error)

                }
            )
            queue.add(jsonObjectRequest)
            statusText.text = resources.getString(R.string.visualize_coord_success)
        } catch (error: JSONException) {
            statusText.text = resources.getString(R.string.visualize_coord_error)
        }

    }

    private fun getCoordArray(session: String?, image: ImageView, bitMap: Bitmap) {

        val queue = Volley.newRequestQueue(this)
        val url =
            "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/sessions/$session"

        previousX = bitMap.width / 2F
        previousY = bitMap.height / 2F

        try {
            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,

                Response.Listener {

                    for (i in 0 until it.length()) {

                        val item = it.getJSONObject(i)
                        val x : Float = item.getString("xCoord").toFloat()
                        val y : Float = item.getString("yCoord").toFloat()
                        val collision : Boolean = item.getBoolean("isCollision")

                        paint(x, y, collision, bitMap, image)
                        println(item)
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

    private fun getSession(image: ImageView, bitMap: Bitmap) {

        val statusText: TextView = findViewById(R.id.visualizeStatusText)
        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()

                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val session = json.int("session").toString()
                    if (session != null) {
                        statusText.text = resources.getString(R.string.visualize_coord_success)
                        getCoordArray(session, image, bitMap)

                    } else {
                        statusText.text = resources.getString(R.string.visualize_coord_error)
                    }
                },

                Response.ErrorListener { error ->
                    error
                    println(error)
                    statusText.text = resources.getString(R.string.visualize_coord_error)

                }
            )
            queue.add(jsonObjectRequest)
        } catch (error: JSONException) {
            statusText.text = resources.getString(R.string.visualize_coord_error)
            println(error)
        }

    }

    private fun paint(x: Float, y: Float, collision: Boolean?, bitMap: Bitmap, image: ImageView) {

        val paint = Paint(Color.BLACK)
        paint.strokeWidth = 3F

        val origoX = bitMap.width / 2F
        val origoY = bitMap.height / 2F
        var fixedX = origoX + (x * 6)
        var fixedY = origoY - (y * 6)

        println("x: $x")
        println("y: $y")

        val tempCanvas = Canvas(bitMap)
        tempCanvas.drawBitmap(bitMap, 0F, 0F, null)

        tempCanvas.drawLine(previousX, previousY, fixedX, fixedY, paint)

        if (collision!!) {
            paint.color = Color.RED
            tempCanvas.drawOval(fixedX - 8, fixedY - 8, fixedX + 8, fixedY + 8, paint)
        }

        image.setImageDrawable(BitmapDrawable(resources, bitMap))

        previousX = fixedX
        previousY = fixedY
    }

}

