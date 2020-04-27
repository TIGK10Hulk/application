package com.example.dorisapp

import android.app.Service
import android.bluetooth.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import org.jetbrains.anko.toast
import java.util.*
import kotlin.experimental.and

class BLEConstants {
    companion object {
        const val ACTION_DATA_WRITTEN = "com.example.dorisapp.ACTION_DATA_WRITTEN"
        const val EXTRA_DATA = "com.example.dorisapp.EXTRA_DATA"
        const val MAC_ADDRESS = "00:1B:10:65:FC:75"
        var SERVICE_UUID_ROBOT: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
        var SERVICE_UUID_ROBOT_ALTERNATIVE: UUID = UUID.fromString("9e5d1e47-5c13-43a0-8635-82ad38a1386f")
        var SERVICE_UUID_ROBOT_WRITE: UUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb")
        var SERVICE_UUID_ROBOT_READ: UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")



    }
}


class BluetoothLeService : Service() {

    private val m_TAG = "BLE :) :) :)"
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private val mBinder = MyLocalBinder()

    //High level manager used to obtain an instance of an BluetoothAdapter and to conduct overall Bluetooth Management.
    var m_bluetoothManager: BluetoothManager? = null;
    //There's one Bluetooth adapter for the entire system, and your application can interact with it using this object.
    var m_bluetoothAdapter: BluetoothAdapter? = null
    //MAC Address of the connected BLE device
    var m_deviceAddress: String? = null
    //Used to conduct GATT client operations
    var m_bluetoothGatt: BluetoothGatt? = null

    var m_bluetoothGattService: BluetoothGattService? = null

    var m_BLUETOOTH_CONNECTED = false
    var m_REGISTERAPP_UUID: UUID = UUID.fromString("f564e90a-382c-4872-9d9e-256a81261116")

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class MyLocalBinder : Binder() {
        fun getService() : BluetoothLeService {
            return this@BluetoothLeService
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        toast("Bluetooth Service started.")

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            if(isBluetoothLEConnected()) {
                //TODO hantera läs skriv
            } else {
                init()
            }
        }
        mHandler.postDelayed(mRunnable, 2000)
        //Line 38 needs to be recalled as soon as the previous call has been finished
        //This is going to be the function that will loop and check for new data in the bluetooth stream

        return START_STICKY
    }

    private fun isBluetoothLEConnected () : Boolean {
        return m_BLUETOOTH_CONNECTED;
    }

    // Initialize BluetoothAdapter from BLuetoothManager
    private fun init() : Boolean {
        if(m_bluetoothManager == null) {
                m_bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if(m_bluetoothManager == null) {
                Log.e(m_TAG, "Unable to initialize BLuetoothManager" )
                return false
            }
        }

        m_bluetoothAdapter = m_bluetoothManager!!.adapter
        if(m_bluetoothAdapter == null) {
            Log.e(m_TAG, "Unable to get initialize BluetoothAdapter")
            return false
        }
        connect(BLEConstants.MAC_ADDRESS)
        return true
    }

    private fun isEnabled(): Boolean {
        return this.m_bluetoothAdapter!!.isEnabled
    }

    //scan here egentligen

    private fun connect(deviceAddress: String?) : Boolean {
        val device = m_bluetoothAdapter!!.getRemoteDevice(deviceAddress)

        if(device == null || m_bluetoothAdapter == null) {
            Log.e(m_TAG, "Cannot connect to device")
            return false
        }

        Log.i(m_TAG,"Device object: " + device.toString())

        m_deviceAddress = deviceAddress;
        m_bluetoothGatt = device.connectGatt(this, false, gattCallback)

        Log.i(m_TAG,"GATT_SUCCES code : " +BluetoothGatt.GATT_SUCCESS.toString());


        if(m_bluetoothGatt != null) {
            Log.i(m_TAG,"Services in connect: " + m_bluetoothGatt!!.services)
        }


        m_BLUETOOTH_CONNECTED = true;
        Log.i(m_TAG, "We are connnected to Gatt server on Doris")
        return true
    }

    //Used to deliver results to the client, such as connection status, as well as any further GATT client operations.
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
        ) {
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                //TODO say we are connected
                m_bluetoothGatt?.discoverServices()

                //TODO temporarily
                //Log.i(m_TAG ,findCharacteristicsFromDevice(BLEConstants.MAC_ADDRESS, m_REGISTERAPP_UUID).toString())

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //TODO say we are disconnected
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(m_TAG,"SERVICES HÄR : "+ m_bluetoothGatt?.services.toString())
                m_bluetoothGattService = m_bluetoothGatt!!.getService(BLEConstants.SERVICE_UUID_ROBOT)
                Log.i(m_TAG, m_bluetoothGattService.toString())

                //findCharacteristicsFromDevice(BLEConstants.MAC_ADDRESS, BLEConstants.SERVICE_UUID_ROBOT_WRITE)
                Log.i(m_TAG, "Bra jobbat :)")
            } else {
                Log.w(m_TAG, "onServicesdeicovered: " + status)
            }
        }

        //A request to Read has completed
        override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
        ) {
            //read data from characteristic.value
            when(status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(m_TAG, "Successfully read from characteristics: $characteristic")
                }
            }
        }

        override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //TODO say we have written data
                Log.i(TAG, "Data written $characteristic")
                //TODO broadcast intent that says we have written data
                broadcastUpdate(BLEConstants.ACTION_DATA_WRITTEN, characteristic)
            }
        }
    }

    fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        val intent = Intent(action)
    //TODO format the characteristics and send a intent
        val data: ByteArray? = characteristic!!.value
        intent.putExtra(BLEConstants.EXTRA_DATA, "$data")

        sendBroadcast(intent)

    }

    fun writeCharacteristics(characteristic: BluetoothGattCharacteristic) {
        //check we access to BT radio
        if(m_bluetoothAdapter == null || m_bluetoothGatt == null) {
            return
        }

        //TODO(write to char here?)

    }

    fun findCharacteristicsFromDevice(Mac_address: String, characteristicUUID: UUID) : BluetoothGattCharacteristic? {
        Log.i(m_TAG, "I AM IN FIND CHAR")


        if(m_bluetoothGatt == null) {
            Log.e(m_TAG, "BLuetoothgatt is null")
            return null;
        }

        Log.i(m_TAG, m_bluetoothGatt!!.services.toString())

        for(service in m_bluetoothGatt!!.services) {
            Log.i(m_TAG, "I AIM IN FOR LOOP")
            val characteristic : BluetoothGattCharacteristic = service.getCharacteristic(characteristicUUID)
            if(characteristic != null) {
                Log.i(m_TAG, "CHAR" + characteristic.toString())
                return characteristic
            }
        }
        return null;

    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Bluetooth Service Destroyed")
        mHandler.removeCallbacks(mRunnable)
    }



    //Test functions to check if the service correctly receives and sends data
    fun testInData(data: String){
        toast("received: $data")
    }

    fun testOutData(): String {
        return("Hej från service")
    }

    fun sendCommand(input: String) {

    }
    fun disconnect() {

    }

}