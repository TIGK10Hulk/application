package com.example.dorisapp

import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.dorisapp.R.color.pink
import org.jetbrains.anko.toast

class RemoteActivity: AppCompatActivity() {
    var bluetoothService: BluetoothLeService? = null
    var isBound = false
    private val m_TAG = "RemoteActivity :) :)"

    var buttonLeft: ImageButton? = null
    var buttonRight: ImageButton? = null
    var buttonForward: ImageButton? = null
    var buttonReverse: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        HelperFunctions.initializeContentView(R.layout.activity_remote, findViewById(android.R.id.content), layoutInflater) //Adds activity_remote.xml to current view
        HelperFunctions.initializeDrawerListeners(R.layout.activity_remote, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        var toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Remote control"

        this.registerReceiver(gattUpdateReceiver, gattUpdateIntentFilter())

        //Initiate Service, start it and then bind to it.
        val serviceClass = BluetoothLeService::class.java
        val intent = Intent(applicationContext, serviceClass)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )

        buttonLeft = findViewById(R.id.turnLeftButton)
        buttonRight = findViewById(R.id.turnRightButton)
        buttonForward = findViewById(R.id.forwardButton)
        buttonReverse = findViewById(R.id.reverseButton)

    }

    fun drive(view: View){
        if(bluetoothService!!.m_BLUETOOTH_CONNECTED){
            bluetoothService!!.getCharThenWrite(1,1)
            setActiveButtonBackground(buttonForward)
        }
    }

    fun reverse(view: View) {
        if(bluetoothService!!.m_BLUETOOTH_CONNECTED) {
            bluetoothService!!.getCharThenWrite(1,4)
            setActiveButtonBackground(buttonReverse)
        }
    }

    fun stop(view: View) {
        if(bluetoothService!!.m_BLUETOOTH_CONNECTED) {
            bluetoothService!!.getCharThenWrite(1,0)
        }
    }

    fun turnLeft(view: View){
        if(bluetoothService!!.m_BLUETOOTH_CONNECTED) {
            bluetoothService!!.getCharThenWrite(1,2)
            setActiveButtonBackground(buttonLeft)
        }
    }

    fun turnRight(view: View) {
        if(bluetoothService!!.m_BLUETOOTH_CONNECTED) {
            bluetoothService!!.getCharThenWrite(1,3)
            setActiveButtonBackground(buttonRight)
        }
    }

    fun setActiveButtonBackground(button: ImageButton?){
        if(button == null){
            return
        }

        buttonForward!!.setColorFilter(Color.argb(0, 255, 255, 255))
        buttonReverse!!.setColorFilter(Color.argb(0, 255, 255, 255))
        buttonLeft!!.setColorFilter(Color.argb(0, 255, 255, 255))
        buttonRight!!.setColorFilter(Color.argb(0, 255, 255, 255))

        button.setColorFilter(Color.argb(255, 255, 255, 255))
    }

    private fun gattUpdateIntentFilter() : IntentFilter {
        val intentfilter: IntentFilter = IntentFilter()
        intentfilter.addAction(BLEConstants.ACTION_DATA_WRITTEN)
        intentfilter.addAction(BLEConstants.ACTION_GATT_CONNECTED)
        intentfilter.addAction("")

        return intentfilter

    }

    private val gattUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action

            when(action) {
                BLEConstants.ACTION_DATA_WRITTEN -> {
                    val data = intent.getStringExtra(BLEConstants.EXTRA_DATA)
                    //toast("You have written to robot: " + data.toString())
                    Log.i(m_TAG, "DATA WRITTEN ${data.toString()}")
                }
                BLEConstants.ACTION_GATT_CONNECTED -> {
                    toast("You are now connected to Gatt server on the robot")
                }
            }
        }
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
