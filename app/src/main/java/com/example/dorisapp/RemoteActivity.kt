package com.example.dorisapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class RemoteActivity: AppCompatActivity() {
    var bluetoothService: BluetoothHandler? = null
    var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_remote, findViewById(android.R.id.content), layoutInflater) //Adds activity_remote.xml to current view
        initializeDrawerListeners(R.layout.activity_remote, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        //Initiate Service, start it and then bind to it.
        val serviceClass = BluetoothHandler::class.java
        val intent = Intent(applicationContext, serviceClass)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )

    }
    
   //Returns an object used to access public methods of the bluetooth service
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder
        ) {
            val binder = service as BluetoothHandler.MyLocalBinder
            bluetoothService = binder.getService()
            isBound = true
            println("Bind connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            println("Bind disconnected")
            isBound = false
        }
    }

    fun driveForward() {
        // Do something
    }

    fun driveLeft() {
        // Do something
    }

    fun driveRight() {
        // Do something
    }

    fun driveBackwards() {
        // Do something
    }

}
