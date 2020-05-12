package com.example.dorisapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var bluetoothService: BluetoothLeService? = null
    var isBound = false
    var isSessionActive: Boolean = false

    var start : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        HelperFunctions.initializeContentView(R.layout.activity_main, findViewById(android.R.id.content), layoutInflater) //Adds activity_main.xml to current view
        HelperFunctions.initializeDrawerListeners(R.layout.activity_main, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        val layout = findViewById<RelativeLayout>(R.id.mainActivity)
        val backroundAnimation : AnimationDrawable = layout.background as AnimationDrawable
        backroundAnimation.setEnterFadeDuration(4500)
        backroundAnimation.setExitFadeDuration(4500)
        backroundAnimation.start()

        //Initiate Service, start it and then bind to it.
        val serviceClass = BluetoothLeService::class.java
        val intent = Intent(applicationContext, serviceClass)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )

        //Get latest session
        HelperFunctions.getSession(this)
    }

    fun startAndStop(view: View) {

        val parkButton : Button = findViewById(R.id.parkButton)
        val statusText : TextView = findViewById(R.id.drivingStatusTextView)
        val statusImage : ImageView = findViewById(R.id.dorisStatusImage)

        /*
        if(bluetoothService == null || !bluetoothService!!.m_BLUETOOTH_CONNECTED){
            return
        }
        */

        if (start) {
            /*sendCoordinate(this)*/
            /*val testCoord = Coord(105, 105, false, session = 1)
            sendCoordinate(this, testCoord)*/
            if(!isSessionActive){
                RobotData.session += 1
                isSessionActive = true
            }
            println("SESSION::::::: ${RobotData.session}")
            val startButton : Button = findViewById(R.id.startAndPauseButton)
            startButton.text = "Pause"
            statusText.text = resources.getString(R.string.driving_status_driving_text_view)
            parkButton.isEnabled = false
            statusImage.setImageDrawable(resources.getDrawable(R.drawable.doris2))
            start = false
            bluetoothService!!.getCharThenWrite(0, 1)
        } else {
            val stopButton : Button = findViewById(R.id.startAndPauseButton)
            stopButton.text = "Start"
            statusText.text = resources.getString(R.string.driving_status_default_text_view)
            statusImage.setImageDrawable(resources.getDrawable(R.drawable.doris2bw))
            parkButton.isEnabled = true
            start = true
            bluetoothService!!.getCharThenWrite(0, 0)
        }
    }

    fun park(view: View) {
        isSessionActive = false
        var statusText : TextView = findViewById(R.id.drivingStatusTextView)
        statusText.text = "Going to garage"
    }

    //Returns an object used to access public methods of the bluetooth service
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as BluetoothLeService.MyLocalBinder
            bluetoothService = binder.getService()
            isBound = true
            println("Bind connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            println("Bind disconnected")
            isBound = false
        }
    }
}