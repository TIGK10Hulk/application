package com.example.dorisapp

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_data, findViewById(android.R.id.content), layoutInflater) //Adds activity_data.xml to current view
        initializeDrawerListeners(R.layout.activity_data, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        val dataRecycler = findViewById(R.id.dataRecycler) as RecyclerView

        dataRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val coordinateDataItem = ArrayList<CoordinateData>()

        coordinateDataItem.add(CoordinateData("X: 1", "Y: 5", "Collision: No"))
        coordinateDataItem.add(CoordinateData("X: 3", "Y: 6", "Collision: Yes"))
        coordinateDataItem.add(CoordinateData("X: 2", "Y: 4", "Collision: No"))


        val adapter = DataListAdapter (coordinateDataItem)

        dataRecycler.adapter = adapter
    }
}