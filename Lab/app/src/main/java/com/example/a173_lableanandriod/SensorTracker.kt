package com.example.a173_lableanandriod

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Layer ที่ 1: Hardware Layer
 *
 * คลาสนี้ทำหน้าที่คุยกับ Hardware โดยตรง (Accelerometer)
 * และแปลงค่าเซนเซอร์ให้เป็น StateFlow เพื่อให้ Layer ด้านบนดึงไปใช้ได้
 *
 * ข้อดีของการแยกคลาสนี้ออกมา:
 * - ViewModel ไม่ต้องรู้จัก SensorManager เลย
 * - Composable ไม่มี SensorEventListener ปะปน (ถูกต้องตาม Architecture)
 * - ทดสอบ (Unit Test) ได้ง่ายขึ้น เพราะแยก concern ออกจากกัน
 */
class SensorTracker(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // MutableStateFlow (ส่วน private) — ใช้อัปเดตค่าภายในคลาสนี้เท่านั้น
    private val _sensorData = MutableStateFlow(AccelerometerData())

    // StateFlow (ส่วน public) — เปิดให้ Layer อื่นอ่านค่าได้ แต่แก้ไขไม่ได้
    val sensorData: StateFlow<AccelerometerData> = _sensorData.asStateFlow()

    /** เรียกเพื่อเริ่มรับค่าจากเซนเซอร์ (ควรเรียกใน onResume หรือตอน ViewModel เริ่มทำงาน) */
    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    /** เรียกเพื่อหยุดรับค่าจากเซนเซอร์ (ควรเรียกใน onPause หรือตอน ViewModel ถูก clear) */
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    // ─── SensorEventListener Callbacks ───────────────────────────────────────

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // ทุกครั้งที่เซนเซอร์อัปเดต — อัปเดตค่าใน StateFlow
            _sensorData.value = AccelerometerData(
                x = event.values[0],
                y = event.values[1],
                z = event.values[2]
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // ไม่จำเป็นต้องใช้ในตัวอย่างนี้
    }
}

// Lab 24/3
