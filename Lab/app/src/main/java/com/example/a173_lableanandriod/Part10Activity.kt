package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 10: App Widget (with Jetpack Glance)
// =============================================================================

class Part10Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GlanceOverviewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GlanceOverviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Jetpack Glance Widgets",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Concept: Modern App Widgets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "เมื่อก่อนการสร้าง Widget ต้องเขียนด้วยภาษา XML โยงผ่าน RemoteViews สุดแสนจะยุ่งยาก แต่ปัจจุบัน Google ได้ออก 'Jetpack Glance' มาให้เราแล้ว!\n\n" +
                           "Glance ยอมให้เราใช้กลไกการเขียนแบบ Jetpack Compose (สไตล์เดิมที่คุ้นเคย) ไปสร้างหน้าตาให้ Widget ได้เลย โดยเบื้องหลังมันจะถูกแปลงร่างเป็น RemoteViews ให้เราอัตโนมัติ"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("วิธีการใช้งาน", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("1. ติดตั้งไลบรารี androidx.glance:glance-appwidget\n" +
             "2. สร้างคลาสสืบทอดจาก GlanceAppWidget ร่าง UI ด้วยโค้ด Glance\n" +
             "3. สร้างคลาส Receiver สืบทอดจาก GlanceAppWidgetReceiver\n" +
             "4. ประกาศ Receiver ใน AndroidManifest ให้เชื่อมกับ xml/info")

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "🎉 โปรเจกต์นี้ฝัง Widget ของจริงแล้ว ลองกดปุ่ม Home กลับไปหน้าจอหลัก แล้วเพิ่ม 'Lab' Widget มาดูผลงานได้เลยครับ!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

    }
}
