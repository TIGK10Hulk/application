package com.example.dorisapp

import android.app.Activity
import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.toast
import java.util.*


class BLEConstants {
    companion object {
        const val ACTION_GATT_CONNECTED = "com.example.dorisapp.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.example.dorisapp.ACTION_GATT_DISCONNECTED"
        const val ACTION_DATA_WRITTEN = "com.example.dorisapp.ACTION_DATA_WRITTEN"
        const val ACTION_DATA_READ = "com.example.dorisapp.ACTION_DATA_READ"
        const val EXTRA_DATA = "com.example.dorisapp.EXTRA_DATA"
        const val MAC_ADDRESS = "00:1B:10:65:FC:75"
        var SERVICE_UUID_ROBOT: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
        var SERVICE_UUID_ROBOT_ALTERNATIVE: UUID = UUID.fromString("9e5d1e47-5c13-43a0-8635-82ad38a1386f")
        var CHAR_UUID_ROBOT_WRITE: UUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb")
        var CHAR_UUID_ROBOT_READ: UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")
        var DESCRIPTOR_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

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

    var m_bluetoothGattCharacteristic: BluetoothGattCharacteristic? = null

    var m_bluetoothGattReadCharacteristic: BluetoothGattCharacteristic? = null

    var m_BLUETOOTH_CONNECTED = false
    var m_REGISTERAPP_UUID: UUID = UUID.fromString("f564e90a-382c-4872-9d9e-256a81261116")

    lateinit var context: Context

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class MyLocalBinder : Binder() {
        fun getService() : BluetoothLeService {
            return this@BluetoothLeService
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context = this
        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            //pushListOfCoordinates(this)

            if(!isBluetoothLEConnected()) {
                init()
            }
        }
        mHandler.postDelayed(mRunnable, 10000)
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
                return false
            }
        }

        m_bluetoothAdapter = m_bluetoothManager!!.adapter
        if(m_bluetoothAdapter == null) {
            return false
        }
        connect(BLEConstants.MAC_ADDRESS)
        return true
    }

    private fun connect(deviceAddress: String?) : Boolean {
        val device = m_bluetoothAdapter!!.getRemoteDevice(deviceAddress)

        if(device == null || m_bluetoothAdapter == null) {
            return false
        }

        m_deviceAddress = deviceAddress;
        m_bluetoothGatt = device.connectGatt(this, false, gattCallback)
        m_BLUETOOTH_CONNECTED = true;
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
                broadcastUpdate(BLEConstants.ACTION_GATT_CONNECTED)
                m_bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                broadcastUpdate(BLEConstants.ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                m_bluetoothGattService = m_bluetoothGatt!!.getService(BLEConstants.SERVICE_UUID_ROBOT)

                val writeCharacteristic = findCharacteristicsFromDevice(BLEConstants.MAC_ADDRESS, BLEConstants.CHAR_UUID_ROBOT_WRITE)
                if(writeCharacteristic != null) {
                    m_bluetoothGattCharacteristic = writeCharacteristic
                }

                val readCharacteristic = findCharacteristicsFromDevice(BLEConstants.MAC_ADDRESS, BLEConstants.CHAR_UUID_ROBOT_READ)
                if(readCharacteristic == null) {
                    return
                }
                m_bluetoothGattReadCharacteristic = readCharacteristic

                gatt!!.setCharacteristicNotification(readCharacteristic, true)
                val descriptor = readCharacteristic.getDescriptor(BLEConstants.DESCRIPTOR_UUID)
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                gatt!!.writeDescriptor(descriptor)
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
                    val dataInput = characteristic!!.value
                }
            }
        }

        override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Thread.sleep(100)
                broadcastUpdate(BLEConstants.ACTION_DATA_WRITTEN, characteristic)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            parseByteArr(characteristic!!.value)
            broadcastUpdate(BLEConstants.ACTION_DATA_READ, characteristic)
            println("******'Received: ${characteristic!!.value.contentToString()}")
            //Add new coordinate to list of coordinates not pushed to backend

            val newCoord = Coord(RobotData.xPosition, RobotData.yPosition, false, RobotData.session)
            sendCoordinate(context, newCoord)
            /*
            if(RobotData.unpushedCoords!!.isEmpty()){
                RobotData.unpushedCoords!!.add(newCoord)
            } else if(newCoord != RobotData.unpushedCoords!!.last()){
                RobotData.unpushedCoords!!.add(newCoord)
            }

             */
        }
    }

    fun getCharThenWrite(action: Int, command: Int) {
        if(m_bluetoothGattCharacteristic == null) {
            return
        }
        writeCharacteristics(m_bluetoothGattCharacteristic!!, action, command)
    }

    private fun writeCharacteristics(characteristic: BluetoothGattCharacteristic, action: Int, command: Int) {
        //check we access to BT radio
        if(m_bluetoothAdapter == null || m_bluetoothGatt == null) {
            return
        }
        var byteArray:ByteArray? = null

        byteArray = byteArrayOf(action.toByte(),command.toByte())
        characteristic.value = byteArray
        m_bluetoothGatt!!.writeCharacteristic(characteristic)
    }

    fun findCharacteristicsFromDevice(Mac_address: String, characteristicUUID: UUID) : BluetoothGattCharacteristic? {

        if(m_bluetoothGatt == null) {
            return null;
        }

        for(service in m_bluetoothGatt!!.services) {
            val characteristic : BluetoothGattCharacteristic? = service!!.getCharacteristic(characteristicUUID)
            if(characteristic != null) {
                return characteristic
            }
        }
        return null;

    }

    fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        val intent = Intent(action)
        val data: ByteArray? = characteristic!!.value
        intent.putExtra(BLEConstants.EXTRA_DATA, "${data!!.contentToString()}")
        sendBroadcast(intent)
    }

    fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    //TODO fix the func below
    fun parseByteArr(byteArr: ByteArray) {
        when(byteArr[0].toInt()) {
            0 -> {
                if (byteArr[1].toInt() == 0) RobotData.manualControl = false else RobotData.manualControl = true
            }
            1 -> {
                RobotData.speed = byteArr[1].toInt()
            }
            2 -> {
                RobotData.xPosition += byteArr[1].toInt()
                RobotData.yPosition += byteArr[2].toInt()
            }

            //todo Update backend with new values
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Bluetooth Service Destroyed")
        stopService(Intent(this, BluetoothLeService::class.java))
        mHandler.removeCallbacks(mRunnable)
    }

    fun getDescriptorUUID(bluetoothGattCharacteristic: BluetoothGattCharacteristic) {
        val descriptors = bluetoothGattCharacteristic.descriptors
        for (descriptor in descriptors) {
            Log.i(m_TAG, "Descriptors in given characteristic: " + descriptor.uuid.toString())
        }
    }
}