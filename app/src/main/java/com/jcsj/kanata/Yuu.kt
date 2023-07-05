package com.jcsj.kanata

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit.MINUTES

fun sched(w: Class<out ListenableWorker>): (Context) -> (Long) -> Unit {
    return fun(context: Context): (Long) -> Unit {
        return fun(minutes:Long) {
            val workRequest = OneTimeWorkRequest.Builder(w)
                .setInitialDelay(minutes, MINUTES)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

val schedBTOff = sched(BluetoothOffWorker::class.java)


class BluetoothOffWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val btMgr = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // Disable Bluetooth
        btMgr.adapter.disable()
        return Result.success()
    }
}