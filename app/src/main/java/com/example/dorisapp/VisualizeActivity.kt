package com.example.dorisapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class VisualizeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_visualize, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_visualize, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system
    }

}