package com.example.dorisapp

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.jetbrains.anko.toast

class RemoteActivity: AppCompatActivity() {
    var bluetoothService: BluetoothLeService? = null
    var isBound = false
    private val m_TAG = "RemoteActivity :) :)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        initializeContentView(R.layout.activity_remote, findViewById(android.R.id.content), layoutInflater) //Adds activity_remote.xml to current view
        initializeDrawerListeners(R.layout.activity_remote, findViewById(android.R.id.content), this) //Initialize button listeners for navigation system

        this.registerReceiver(gattUpdateReceiver, gattUpdateIntentFilter())

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
                    val data = intent.getByteArrayExtra(BLEConstants.EXTRA_DATA)
                    toast("You have written to robot: " + data)
                    Log.i(m_TAG, "DATA WRITTEN $data")
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

}
