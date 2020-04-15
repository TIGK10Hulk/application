package com.example.dorisapp

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import androidx.core.app.ActivityCompat.startActivityForResult
import org.jetbrains.anko.toast


class BluetoothHandler : Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private val mBinder = MyLocalBinder()


    var m_bluetoothAdapter: BluetoothAdapter? = null
    var BLUETOOTH_CONNECTED = 0

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class MyLocalBinder : Binder() {
        fun getService() : BluetoothHandler {
            return this@BluetoothHandler
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        toast("Bluetooth Service started.")

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable { initiateBluetooth() }
        mHandler.postDelayed(mRunnable, 2000)
        //Line 38 needs to be recalled as soon as the previous call has been finished
        //This is going to be the function that will loop and check for new data in the bluetooth stream

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Bluetooth Service Destroyed")
        mHandler.removeCallbacks(mRunnable)
    }

    private fun initiateBluetooth() {
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        //Make sure that bluetooth adapter is not null, if it is then we return
        if(m_bluetoothAdapter == null){
            toast("Bluetooth not available")
            return
        }

        //Continue with bluetooth. "!!" means that the adapter is guaranteed to not be null
        if(!m_bluetoothAdapter!!.isEnabled){
            //Connect to desired devices
        }

    }

    //Test functions to check if the service correctly receives and sends data
    fun testInData(data: String){
        toast("received: $data")
    }

    fun testOutData(): String {
        return("Hej från service")
    }

}