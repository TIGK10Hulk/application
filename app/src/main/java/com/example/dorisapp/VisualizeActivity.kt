package com.example.dorisapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.*
import org.json.JSONException
import org.json.JSONObject

class VisualizeActivity: AppCompatActivity() {

    var previousX = 0F
    var previousY = 0F
    private val paint : Paint = Paint(Color.BLACK)
    private val paintCollision : Paint = Paint(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_visualize, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_visualize, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

            val image = setImageView()
            val bitMap = setBitMap(image)

            // Commente out for now, need later
            //getSession(image, bitMap)
            getListOfCordsForGraph("1", image, bitMap)

          /*  val handler = Handler(Looper.getMainLooper())
            handler.post(object : Runnable {
                override fun run() {
                    getLatestCordsForGraph(image, bitMap)
                    handler.postDelayed(this, 3000)
                }
            })

           */
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
                    val pos = it.getString("position")
                    val stringBuilder: StringBuilder = StringBuilder(pos)
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val x = json.string("xCoord")
                    val y = json.string("yCoord")
                    val collision = json.string("isCollision")

                    if (x != null && y != null) {
                        val xFloat= x.toFloat()
                        val yFloat = y.toFloat()

                        // TODO: Kolla timestamp här

                        paint(xFloat, yFloat, collision!!.toBoolean(), bitMap, image)
                            //val tempBitmap : Bitmap = Bitmap.createBitmap(bitMap.width, bitMap.height, Bitmap.Config.ARGB_8888)

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
        val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/sessions/2"

        previousX = bitMap.width / 2F
        previousY = bitMap.height / 2F

        //tempCanvas.drawBitmap(bitMap, 0.0F, 0.0F, null)
        try {
            val jsonArrayRequest = JsonObjectRequest(Request.Method.GET, url, null,

                Response.Listener {
                    val parser : Parser = Parser.default()
                    val posArray = it.getString("positions")
                    val stringBuilder : StringBuilder = StringBuilder(posArray)
                    val array : JsonArray<JsonObject> = parser.parse(stringBuilder) as JsonArray<JsonObject>
                    println(array)

                    array.forEach{i -> i
                        val x : Float = i.string("xCoord")!!.toFloat()
                        println(x)
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

                    val pos = it.getString("position")
                    val stringBuilder: StringBuilder = StringBuilder(pos)
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

        val tempCanvas = Canvas(bitMap)
        tempCanvas.drawBitmap(bitMap, 0F, 0F, null)

        if (collision!!) {
            tempCanvas.drawPoint(x, y, paintCollision)
        }
        tempCanvas.drawLine(previousX,previousY, previousX+x, previousY-y, paint)
        previousX += x
        previousY += y
        image.setImageDrawable(BitmapDrawable(resources, bitMap))
    }

}

