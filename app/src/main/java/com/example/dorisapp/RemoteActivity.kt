package com.example.dorisapp

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RemoteActivity: AppCompatActivity() {
    var bluetoothService: BluetoothLeService? = null
    var isBound = false
    private val m_TAG = "RemoteActivity :) :)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_remote, findViewById(android.R.id.content), layoutInflater) //Adds activity_remote.xml to current view
        initializeDrawerListeners(R.layout.activity_remote, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        //Initiate Service, start it and then bind to it.
        val serviceClass = BluetoothLeService::class.java
        val intent = Intent(applicationContext, serviceClass)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE )

    }

    fun drive(view: View){
        bluetoothService!!.getCharThenWrite(1)
    }

    fun reverse(view: View) {
        bluetoothService!!.getCharThenWrite(5)
    }

    fun stop(view: View) {
        bluetoothService!!.getCharThenWrite(0)
    }

    fun turnLeft(view: View){
        bluetoothService!!.getCharThenWrite(2)
    }

    fun turnRight(view: View) {
        bluetoothService!!.getCharThenWrite(3)
    }









    private val gattUpdateReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action

            when(action) {
                BLEConstants.ACTION_DATA_WRITTEN -> {
                    val data = intent.getByteArrayExtra(BLEConstants.EXTRA_DATA)
                    Log.i(m_TAG, "DATA WRITTEN $data")
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
