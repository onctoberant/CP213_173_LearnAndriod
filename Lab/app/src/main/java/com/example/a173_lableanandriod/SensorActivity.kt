package com.example.a173_lableanandriod

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AccelerometerData(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f)

class SensorViewModel : ViewModel() {
    private val _sensorData = MutableStateFlow(AccelerometerData())
    val sensorData: StateFlow<AccelerometerData> = _sensorData.asStateFlow()

    fun updateSensorData(x: Float, y: Float, z: Float) {
        _sensorData.value = AccelerometerData(x, y, z)
    }
}

class SensorActivity : ComponentActivity(), SensorEventListener {

    private val sensorViewModel: SensorViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        viewModel = sensorViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            sensorViewModel.updateSensorData(x, y, z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this example
    }
}

@Composable
fun SensorScreen(viewModel: SensorViewModel, modifier: Modifier = Modifier) {
    val sensorData by viewModel.sensorData.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Accelerometer Data", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "X: ${"%.2f".format(sensorData.x)}", fontSize = 20.sp)
        Text(text = "Y: ${"%.2f".format(sensorData.y)}", fontSize = 20.sp)
        Text(text = "Z: ${"%.2f".format(sensorData.z)}", fontSize = 20.sp)
    }
}
