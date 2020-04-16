package com.example.dorisapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

fun initializeDrawerListeners(currentLayout: Int, view: View, context: Context){
    //context is needed to allow for intents to be started
    //view is needed to find the correct subviews and elements
    //currentLayout is used to determine if the activity we want to go to is the one we're already on, in that case the drawer is simply closed.

    //Set up the listeners for each item on the drawer menu
    val menuBar = view.findViewById<NavigationView>(R.id.nav_view)
    val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawer_layout)
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
            R.id.nav_main_information_activity -> {
                drawerLayout.closeDrawers()
                if(currentLayout != R.layout.activity_main_information){
                    val intent = Intent(context, MainInformationActivity::class.java)
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

fun initializeContentView(currentActivity: Int, view: View, layoutInflater: LayoutInflater){
    //Dynamically include the desired activity xml layout file into the navigation drawer layout
    val relativeLayout = view.findViewById<RelativeLayout>(R.id.rl)
    val childView = layoutInflater.inflate(currentActivity, null) //this should be the xml corresponding to the current activity
    childView.setLayoutParams(
        RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    )
    relativeLayout.addView(childView)
}