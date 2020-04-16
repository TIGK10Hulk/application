package com.example.dorisapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class NavDrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
    }

    fun printStuff(view: View){
        println("hola")
    }
}
