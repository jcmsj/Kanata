package com.jcsj.kanata

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit.MINUTES

class BluetoothOffReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // Turn off Bluetooth.
        val btMgr = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btMgr.adapter.disable()
        Log.d("BluetoothOffReceiver", "Turning off Bluetooth")
        val toast = Toast.makeText(context, "Bluetooth was shutdown", Toast.LENGTH_LONG)
        toast.show()
    }
}
fun scheduleBluetoothTurnOff(context: Context, minutes: Long) {
    val workRequest = OneTimeWorkRequest.Builder(BluetoothOffWorker::class.java)
        .setInitialDelay(minutes, MINUTES)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

class BluetoothOffWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val btMgr = this.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // Disable Bluetooth
        btMgr.adapter.disable()
        return Result.success()
    }
}

//public class Yuu(
//    private val ctx: Context,
//) {
//    fun start(minutes: Int = 10) {
//        Log.d("YUU:", "START")
//        scheduleBluetoothTurnOff(ctx, minutes)
//    }
//}