package com.example.a173_lableanandriod

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat

// =============================================================================
// Mission 7: Multi-Activity Transitions (จัดการใช้งานในหน้า MenuActivity)
// =============================================================================

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // ปุ่มดั้งเดิม + เพิ่ม Transition 
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, MainActivity::class.java))
                }) {
                    Text("MainActivity (Default System Anim)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.fade_in, R.anim.fade_out)
                    startActivity(Intent(this@MenuActivity, MainActivity2::class.java), opts.toBundle())
                }) {
                    Text("MainActivity2 (Fade In / Out)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.slide_in_right, R.anim.stay)
                    startActivity(Intent(this@MenuActivity, ListActivity3::class.java), opts.toBundle())
                }) {
                    Text("ListActivity3 (Slide In Right)")
                }

                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.stay, R.anim.slide_out_left)
                    startActivity(Intent(this@MenuActivity, SharedPreferencesActivity::class.java), opts.toBundle())
                }) {
                    Text("SharedPreferences (Slide Out Left)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.slide_in_up, R.anim.stay)
                    startActivity(Intent(this@MenuActivity, CameraActivity::class.java), opts.toBundle())
                }) {
                    Text("CameraActivity (Slide In Up)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.stay, R.anim.slide_out_down)
                    startActivity(Intent(this@MenuActivity, SensorActivity::class.java), opts.toBundle())
                }) {
                    Text("SensorActivity (Slide Out Down)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.zoom_in, R.anim.stay)
                    startActivity(Intent(this@MenuActivity, GalleryActivity::class.java), opts.toBundle())
                }) {
                    Text("GalleryActivity (Zoom In)")
                }

                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.stay, R.anim.zoom_out)
                    startActivity(Intent(this@MenuActivity, Part1AnimationActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 1: AnimationActivity (Zoom Out)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.zoom_in, R.anim.zoom_out)
                    startActivity(Intent(this@MenuActivity, Part2Activity::class.java), opts.toBundle())
                }) {
                    Text("Part 2: Complex Lists (Zoom In + Out)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.slide_in_right, R.anim.fade_out)
                    startActivity(Intent(this@MenuActivity, Part3CanvasActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 3: Canvas Showcase (Slide R + Fade Out)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.slide_in_up, R.anim.slide_out_left)
                    startActivity(Intent(this@MenuActivity, Part4GestureActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 4: Gestures (Slide In Up + Slide Out Left)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.fade_in, R.anim.slide_out_down)
                    startActivity(Intent(this@MenuActivity, Part5SideEffectActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 5: Side Effects (Fade In + Slide Down)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.zoom_in, R.anim.fade_out)
                    startActivity(Intent(this@MenuActivity, Part6WebViewActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 6: WebView Interop (Zoom + Fade)")
                }
                
                Button(onClick = {
                    val opts = ActivityOptionsCompat.makeCustomAnimation(this@MenuActivity, R.anim.fade_in, R.anim.stay)
                    startActivity(Intent(this@MenuActivity, Part8AdaptiveLayoutActivity::class.java), opts.toBundle())
                }) {
                    Text("Part 8: Adaptive Layouts (Fade In Only)")
                }

                // ==========================================
                // Mission 9 - 12
                // ==========================================
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part9Activity::class.java))
                }) {
                    Text("Part 9: Collapsing Toolbar")
                }
                
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part10Activity::class.java))
                }) {
                    Text("Part 10: App Widget Concept")
                }
                
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part11Activity::class.java))
                }) {
                    Text("Part 11: Skeleton Loading (Shimmer)")
                }
                
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part12Activity::class.java))
                }) {
                    Text("Part 12: Bottom Sheet & Dialog")
                }
            }
        }
    }
}