package com.example.dorisapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

fun initializeListeners(context: Context, view: View){
    //Set up the listeners for each item on the drawer menu
    val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_main_activity -> {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_remote_activity -> {
                val intent = Intent(context, RemoteActivity::class.java)
                context.startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    val menuBar = view.findViewById<NavigationView>(R.id.nav_view)
    menuBar.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
}