package com.example.a173_lableanandriod

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.flow.StateFlow

// ─────────────────────────────────────────────────────────────────────────────
// Data Models (ใช้ร่วมกันระหว่าง SensorTracker และ ViewModel)
// ─────────────────────────────────────────────────────────────────────────────
data class AccelerometerData(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f)
data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0
)

// ─────────────────────────────────────────────────────────────────────────────
// Layer ที่ 2: ViewModel Layer
//
// ViewModel ทำหน้าที่:
//   1. ถือ Instance ของ SensorTracker (Hardware Layer)
//   2. Expose StateFlow ของ sensorData + locationData ต่อให้ UI ดึงไปใช้
//   3. สั่ง start/stop tracker ตาม lifecycle ของตัวเอง
//
// ข้อสำคัญ: ViewModel ไม่รู้จัก SensorManager หรือ Activity เลย
//           มันรู้จักแค่ SensorTracker เท่านั้น
// ─────────────────────────────────────────────────────────────────────────────
class SensorViewModel(private val tracker: SensorTracker) : ViewModel() {

    // นำ StateFlow จาก SensorTracker มาเปิดให้ UI อ่านได้โดยตรง
    val sensorData: StateFlow<AccelerometerData> = tracker.sensorData
    val locationData: StateFlow<LocationData> = tracker.locationData

    init {
        // เริ่มรับค่าจากเซนเซอร์ทันทีที่ ViewModel ถูกสร้าง
        tracker.start()
    }

    override fun onCleared() {
        super.onCleared()
        // หยุดรับค่าจากเซนเซอร์เมื่อ ViewModel ถูก destroy (ป้องกัน Memory Leak)
        tracker.stop()
    }

    /**
     * Factory สำหรับส่ง SensorTracker เข้า ViewModel
     * (จำเป็นเพราะ ViewModel ปกติสร้างได้เฉพาะ Constructor ที่ไม่มี Parameter)
     */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SensorViewModel(SensorTracker(context)) as T
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Activity — ทำหน้าที่แค่ "เชื่อม" ViewModel กับ UI
// ไม่มี SensorEventListener, SensorManager, หรือ Logic ใดๆ ที่นี่เลย
// ─────────────────────────────────────────────────────────────────────────────
class SensorActivity : ComponentActivity() {

    // ส่ง Factory เพื่อให้ ViewModelProvider รู้วิธีสร้าง SensorViewModel
    private val sensorViewModel: SensorViewModel by viewModels {
        SensorViewModel.Factory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // ส่งแค่ ViewModel ให้ UI — ไม่มี Context, Sensor, หรืออะไรพวกนั้น
                    SensorScreen(
                        viewModel = sensorViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Layer ที่ 3: UI Layer (Jetpack Compose)
//
// Composable นี้ทำหน้าที่แค่แสดงผล ไม่มีการแตะ Sensor ใดๆ ทั้งสิ้น!
// ข้อมูลมาจาก StateFlow ผ่าน collectAsState() เท่านั้น
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SensorScreen(viewModel: SensorViewModel, modifier: Modifier = Modifier) {

    // collectAsState() แปลง StateFlow → Compose State
    // ทุกครั้งที่ StateFlow เปลี่ยน Composable นี้จะ Recompose อัตโนมัติ
    val sensorData by viewModel.sensorData.collectAsState()
    val locationData by viewModel.locationData.collectAsState()

    // ─── ขอ Permission สำหรับ Location ───────────────────────────────────
    var locationPermissionGranted by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions.values.all { it }
    }

    // ขอ Permission ตอนเปิดหน้าจอครั้งแรก
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ─── Accelerometer Card ──────────────────────────────────────────
        Text(
            text = "📡 Accelerometer",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SensorValueRow(axis = "X", value = sensorData.x)
                HorizontalDivider()
                SensorValueRow(axis = "Y", value = sensorData.y)
                HorizontalDivider()
                SensorValueRow(axis = "Z", value = sensorData.z)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ─── GPS Location Card ───────────────────────────────────────────
        Text(
            text = "📍 GPS Location",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LocationValueRow(label = "Latitude", value = locationData.latitude)
                HorizontalDivider()
                LocationValueRow(label = "Longitude", value = locationData.longitude)
                HorizontalDivider()
                LocationValueRow(label = "Altitude", value = locationData.altitude)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "อัปเดตทุกครั้งที่เซนเซอร์เปลี่ยน",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun SensorValueRow(axis: String, value: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "แกน $axis",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${"%.4f".format(value)} m/s²",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun LocationValueRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${"%.6f".format(value)}°",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
