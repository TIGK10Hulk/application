package com.example.dorisapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainInformationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_main_information, findViewById(android.R.id.content), layoutInflater) //Adds activity_main_information.xml to current view
        initializeDrawerListeners(R.layout.activity_main_information, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        var toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Information"

    }

}