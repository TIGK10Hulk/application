package com.example.dorisapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data.*

class DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val listView = findViewById<ListView>(R.id.dataListView)

        listView.adapter = AllDataAdapter(this)
    }

    private class AllDataAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }
        //amount of views in list
        override fun getCount(): Int {
            return 5 //just 5 rows, for test
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "Test string"
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "here is a listview row"
            return textView
        }

    }
}