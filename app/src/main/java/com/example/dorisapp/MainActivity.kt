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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_main, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        initializeDrawerListeners(R.layout.activity_main, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system
    }
}