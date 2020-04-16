package com.example.dorisapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    var start : Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_main, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_main, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system
    }

    fun startAndStop(view: View) {

        var parkButton : Button = findViewById(R.id.parkButton)
        var statusText : TextView = findViewById(R.id.drivingStatusTextView)

        if (start) {
            examplePostRequest(this)
            var startButton : Button = findViewById(R.id.startAndStopButton)
            startButton.text = "Stop"
            statusText.text = "Driving"
            parkButton.isEnabled = false
            start = false
        } else {
            var stopButton : Button = findViewById(R.id.startAndStopButton)
            stopButton.text = "Start"
            statusText.text = "Idle"
            parkButton.isEnabled = true
            start = true
        }
    }

    fun park(view: View) {

        var statusText : TextView = findViewById(R.id.drivingStatusTextView)
        statusText.text = "Going to garage"
    }
}