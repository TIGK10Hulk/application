package com.example.dorisapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import org.json.JSONException

class HelperFunctions {
    companion object {
        fun initializeDrawerListeners(currentLayout: Int, view: View, context: Context){
            //context is needed to allow for intents to be started
            //view is needed to find the correct subviews and elements
            //currentLayout is used to determine if the activity we want to go to is the one we're already on, in that case the drawer is simply closed.

            //Set up the listeners for each item on the drawer menu
            val menuBar = view.findViewById<NavigationView>(R.id.nav_view)
            val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawer_layout)

            val hamburgerButton = view.findViewById<ImageButton>(R.id.nav_hamburger_button)

            hamburgerButton.setOnClickListener{
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }

            menuBar.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_main_activity -> {
                        drawerLayout.closeDrawers()
                        if(currentLayout != R.layout.activity_main){
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                    R.id.nav_remote_activity -> {
                        drawerLayout.closeDrawers()
                        if(currentLayout != R.layout.activity_remote){
                            val intent = Intent(context, RemoteActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                    R.id.nav_data_activity -> {
                        drawerLayout.closeDrawers()
                        if(currentLayout != R.layout.activity_data){
                            val intent = Intent(context, DataActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                    R.id.nav_visualize_activity -> {
                        drawerLayout.closeDrawers()
                        if(currentLayout != R.layout.activity_visualize){
                            val intent = Intent(context, VisualizeActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
                false
            }

        }

        fun initializeContentView(currentLayout: Int, view: View, layoutInflater: LayoutInflater){
            //Dynamically include the desired activity xml layout file into the navigation drawer layout
            val relativeLayout = view.findViewById<RelativeLayout>(R.id.rl)
            val childView = layoutInflater.inflate(currentLayout, null) //this should be the xml corresponding to the current activity
            childView.setLayoutParams(
                RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            relativeLayout.addView(childView)
        }

        fun getSession(context: Context) {

            val queue = Volley.newRequestQueue(context)
            val url = "https://us-central1-hulkdoris-4c6eb.cloudfunctions.net/api/positions/latest"
            try {
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,

                    // On Success
                    Response.Listener {

                        val parser: Parser = Parser.default()

                        val stringBuilder: StringBuilder = StringBuilder(it.toString())
                        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                        RobotData.session = json.int("session")!!.toInt()

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
}
