package com.example.dorisapp

import android.R.attr
import android.R.attr.bitmap
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.json.JSONException


class VisualizeActivity: AppCompatActivity() {

    var previousX = 0F
    var previousY = 0F
    private val paint : Paint = Paint(Color.BLACK)
    private val paintCollision : Paint = Paint(Color.YELLOW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_visualize, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_visualize, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

            val image = setImageView()
            val bitMap = setBitMap(image)

            // Commente out for now, need later
            getSession(image, bitMap)

            val handler = Handler(Looper.getMainLooper())
            handler.post(object : Runnable {
                override fun run() {
                    getLatestCordsForGraph(image, bitMap)
                    handler.postDelayed(this, 3000)
                }
            })

    }

    private fun setImageView() : ImageView {

        val image: ImageView = findViewById(R.id.chart)
        return image

    }

    private fun setBitMap(image : ImageView) : Bitmap {

        return Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    }

    private fun getLatestCordsForGraph(image: ImageView, bitMap: Bitmap) {

        val statusText : TextView = findViewById(R.id.visualizeStatusText)
        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()
                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val x = json.string("xCoord")
                    val y = json.string("yCoord")
                    val collision = json.boolean("isCollision")

                    if (x != null && y != null) {
                        val xFloat= x.toFloat()
                        val yFloat = y.toFloat()

                        if (previousX != xFloat || previousY != yFloat) {

                            paint(xFloat, yFloat, collision!!, bitMap, image)
                        }
                    }

                },

                Response.ErrorListener { error -> error
                    println(error)
                    statusText.text = resources.getString(R.string.visualize_coord_error)

                }
            )
            queue.add(jsonObjectRequest)
        } catch (error : JSONException) {
            statusText.text = resources.getString(R.string.visualize_coord_error)
        }

    }

    private fun getListOfCordsForGraph(session: String?, image : ImageView, bitMap : Bitmap) {

        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/sessions/$session"

        previousX = bitMap.width / 2F
        previousY = bitMap.height / 2F
        println("fuck")

        try {
            val jsonArrayRequest = JsonObjectRequest(Request.Method.GET, url, null,

                Response.Listener {
                    val parser : Parser = Parser.default()
                    val posArray = it.getString("positions")
                    val stringBuilder : StringBuilder = StringBuilder(posArray)
                    val array : JsonArray<JsonObject> = parser.parse(stringBuilder) as JsonArray<JsonObject>
                    println(array.toJsonString())

                    array.forEach{i -> i
                        val x : Float = i.string("xCoord")!!.toFloat()
                        val y: Float = i.string("yCoord")!!.toFloat()
                        val collision = i.boolean("isCollision")
                        paint(x, y, collision, bitMap, image)
                    }

                },

                Response.ErrorListener { error -> error
                    println(error)

                })
            queue.add(jsonArrayRequest)
        } catch (error : JSONException) {

        }

    }

    private fun getSession(image : ImageView, bitMap : Bitmap) {

        val statusText : TextView = findViewById(R.id.visualizeStatusText)
        val queue = Volley.newRequestQueue(this)
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"

        try {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

                // On Success
                Response.Listener {

                    val parser: Parser = Parser.default()

                    val stringBuilder: StringBuilder = StringBuilder(it.toString())
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val session = json.string("session")

                    if (session != null) {
                        statusText.text = resources.getString(R.string.visualize_coord_success)
                        getListOfCordsForGraph(session, image, bitMap)

                    } else {
                        statusText.text = resources.getString(R.string.visualize_coord_error)
                    }
                },

                Response.ErrorListener { error -> error
                    println(error)
                    statusText.text = resources.getString(R.string.visualize_coord_error)

                }
            )
            queue.add(jsonObjectRequest)
        } catch (error : JSONException) {
            statusText.text = resources.getString(R.string.visualize_coord_error)
            println(error)
        }

    }

    private fun paint(x : Float, y : Float, collision : Boolean?, bitMap : Bitmap, image : ImageView) {

        val origoX = bitMap.width / 2F
        val origoY = bitMap.height / 2F
        var fixedX = origoX + (x * 6)
        var fixedY = origoY - (y * 6)

        println("x: $x")
        println("y: $y")

        /* val matrix = Matrix()
        matrix.reset()
        matrix.postTranslate(
            -bitMap.width / 2F,
            -bitMap.height / 2F
        ) // Centers image     matrix.postRotate(rotation);

        matrix.postTranslate(x, y)

         */

        val tempCanvas = Canvas(bitMap)
        tempCanvas.drawBitmap(bitMap, 0F,0F, null)

        tempCanvas.drawLine(previousX, previousY, fixedX, fixedY, paint)
        image.setImageDrawable(BitmapDrawable(resources, bitMap))

        if (collision!!) {
            tempCanvas.drawOval(fixedX - 5, fixedY - 5, fixedX + 5, fixedY + 5, paintCollision)
            image.setImageDrawable(BitmapDrawable(resources, bitMap))

        }

        previousX = fixedX
        previousY = fixedY
    }

}

