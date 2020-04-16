package com.example.dorisapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.example.dorisapp.initializeListeners

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        //Dynamically include the desired xml layout file into the navigation drawer layout
        val relativeLayout = findViewById<RelativeLayout>(R.id.rl)
        val childView = layoutInflater.inflate(R.layout.activity_main, null) //this should be the xml corresponding to the current activity
        childView.setLayoutParams(
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        relativeLayout.addView(childView)
        initializeListeners(this, relativeLayout) //Initialize button listeners for navigation system
    }
}