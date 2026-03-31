package com.example.a173_lableanandriod

import android.content.Intent
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a173_lableanandriod.ListActivity3
import com.example.a173_lableanandriod.MainActivity
import com.example.a173_lableanandriod.MainActivity2
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.v("MyTag", "Verbose: ข้อมูลยิบย่อย (เช่น ค่าแกน XYZ จาก Sensor ทุกๆ มิลลิวินาที)")
        Log.d("MyTag", "Debug: ข้อมูลไว้หาบั๊ก (เช่น ค่า ID ที่ดึงมาจาก Database = 123)")
        Log.i("MyTag", "Info: แจ้งสถานะทั่วไป (เช่น โหลดข้อมูล API สำเร็จแล้ว)")
        Log.w("MyTag", "Warn: เตือนว่าแปลกๆ นะ (เช่น โหลดภาพไม่ขึ้น เลยใช้ภาพ Default แทน)")
        Log.e("MyTag", "Error: พังแล้วจ้า (เช่น catch Exception ได้ หรือ API ร่วง)")

        setContent {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, MainActivity::class.java))
                }) {
                    Text("MainActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, MainActivity2::class.java))
                }) {
                    Text("MainActivity2")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, ListActivity3::class.java))
                }) {
                    Text("ListActivity3")
                }

                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, SharedPreferencesActivity::class.java))
                }) {
                    Text("SharedPreferencesActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, CameraActivity::class.java))
                }) {
                    Text("CameraActivity (Task 1)")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, SensorActivity::class.java))
                }) {
                    Text("SensorActivity (Task 2/3)")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, GalleryActivity::class.java))
                }) {
                    Text("GalleryActivity (Task 1 - Gallery)")
                }
            }
        }
    }
}

// check in 24/feb
// check in 31/3