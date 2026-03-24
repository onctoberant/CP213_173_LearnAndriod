package com.example.a173_lableanandriod

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Layer ที่ 1: Hardware Layer
 *
 * คลาสนี้ทำหน้าที่คุยกับ Hardware โดยตรง (Accelerometer + GPS)
 * และแปลงค่าเซนเซอร์ให้เป็น StateFlow เพื่อให้ Layer ด้านบนดึงไปใช้ได้
 *
 * ข้อดีของการแยกคลาสนี้ออกมา:
 * - ViewModel ไม่ต้องรู้จัก SensorManager หรือ LocationManager เลย
 * - Composable ไม่มี SensorEventListener ปะปน (ถูกต้องตาม Architecture)
 * - ทดสอบ (Unit Test) ได้ง่ายขึ้น เพราะแยก concern ออกจากกัน
 */
class SensorTracker(private val context: Context) : SensorEventListener, LocationListener {

    // ─── Accelerometer ───────────────────────────────────────────────────────
    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // MutableStateFlow (ส่วน private) — ใช้อัปเดตค่าภายในคลาสนี้เท่านั้น
    private val _sensorData = MutableStateFlow(AccelerometerData())

    // StateFlow (ส่วน public) — เปิดให้ Layer อื่นอ่านค่าได้ แต่แก้ไขไม่ได้
    val sensorData: StateFlow<AccelerometerData> = _sensorData.asStateFlow()

    // ─── GPS Location ────────────────────────────────────────────────────────
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _locationData = MutableStateFlow(LocationData())
    val locationData: StateFlow<LocationData> = _locationData.asStateFlow()

    /** เรียกเพื่อเริ่มรับค่าจากเซนเซอร์ (ควรเรียกใน onResume หรือตอน ViewModel เริ่มทำงาน) */
    fun start() {
        // เริ่มรับค่า Accelerometer
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        // เริ่มรับค่า GPS Location
        startLocationUpdates()
    }

    /** เรียกเพื่อหยุดรับค่าจากเซนเซอร์ (ควรเรียกใน onPause หรือตอน ViewModel ถูก clear) */
    fun stop() {
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        try {
            // ใช้ GPS_PROVIDER เป็นหลัก, ถ้าไม่มีจะ fallback ไปใช้ NETWORK_PROVIDER
            val provider = when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                    LocationManager.GPS_PROVIDER
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                    LocationManager.NETWORK_PROVIDER
                else -> null
            }

            provider?.let {
                locationManager.requestLocationUpdates(
                    it,
                    2000L,    // อัปเดตทุก 2 วินาที
                    1f,       // หรือเมื่อเลื่อน 1 เมตร
                    this
                )

                // พยายามดึงค่า Last Known Location มาแสดงก่อน
                locationManager.getLastKnownLocation(it)?.let { loc ->
                    updateLocation(loc)
                }
            }
        } catch (e: SecurityException) {
            // Permission ยังไม่ได้ grant — จะแสดงค่า default (0, 0)
            e.printStackTrace()
        }
    }

    private fun updateLocation(location: Location) {
        _locationData.value = LocationData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude
        )
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

    // ─── LocationListener Callbacks ──────────────────────────────────────────

    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    @Deprecated("Deprecated in API 29+")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // ไม่จำเป็นสำหรับ API 29+
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}

// Lab 24/3
