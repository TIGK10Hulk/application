package com.example.dorisapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    var start : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_main, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_main, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        val layout = findViewById<RelativeLayout>(R.id.mainActivity)
        val backroundAnimation : AnimationDrawable = layout.background as AnimationDrawable
        backroundAnimation.setEnterFadeDuration(4500)
        backroundAnimation.setExitFadeDuration(4500)
        backroundAnimation.start()
    }

    fun startAndStop(view: View) {

        val parkButton : Button = findViewById(R.id.parkButton)
        val statusText : TextView = findViewById(R.id.drivingStatusTextView)
        val statusImage : ImageView = findViewById(R.id.dorisStatusImage)

        if (start) {
            /*sendCoordinate(this)*/
            /*val testCoord = Coord(105, 105, false, session = 1)
            sendCoordinate(this, testCoord)*/
            val startButton : Button = findViewById(R.id.startAndStopButton)
            startButton.text = "Stop"
            statusText.text = resources.getString(R.string.driving_status_driving_text_view)
            parkButton.isEnabled = false
            statusImage.setImageDrawable(resources.getDrawable(R.drawable.doris2))
            start = false
        } else {
            val stopButton : Button = findViewById(R.id.startAndStopButton)
            stopButton.text = "Start"
            statusText.text = resources.getString(R.string.driving_status_default_text_view)
            statusImage.setImageDrawable(resources.getDrawable(R.drawable.doris2bw))
            parkButton.isEnabled = true
            start = true
        }
    }

    fun park(view: View) {

        var statusText : TextView = findViewById(R.id.drivingStatusTextView)
        statusText.text = "Going to garage"
    }
}