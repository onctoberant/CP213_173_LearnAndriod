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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 8: Adaptive Layouts (การรองรับจอหลายขนาด)
// =============================================================================

class Part8AdaptiveLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdaptiveProfileScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AdaptiveProfileScreen(modifier: Modifier = Modifier) {
    // 1. & 2 & 3: ใช้ BoxWithConstraints เพื่อตรวจสอบพื้นที่ๆ มีให้ (maxWidth, maxHeight)
    BoxWithConstraints(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // หากจอ กว้างน้อยกว่า 600.dp (เป็นแนวนอนแคบ หรือมือถือแนวตั้งธรรมดา)
        if (maxWidth < 600.dp) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileImageMock(size = 150)
                Spacer(modifier = Modifier.height(24.dp))
                ProfileDetailsMock()
            }
        } 
        // หากจอ กว้างมากกว่าหรือเท่ากับ 600.dp (หน้าจอแนวนอน แนวนอน Tablet หรือ Foldable screen กางออก)
        else {
            Row(
                modifier = Modifier.fillMaxWidth().height(250.dp), // Fix ความสูงไว้ระดับหนึ่งกันไม่ให้ยืดเต็มจอ
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ให้รูปภาพอยู่ด้านซ้าย และข้อมูลอยู่ด้านขวา (จัดหน้าด้วย weight)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileImageMock(size = 200)
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                Box(modifier = Modifier.weight(2f)) {
                    ProfileDetailsMock()
                }
            }
        }
    }
}

@Composable
fun ProfileImageMock(size: Int) {
    // โครงร่างสมมติให้เป็นรูปโปรไฟล์สีเทากลม (หรือขอบมน)
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("รูปโปรไฟล์", color = Color.DarkGray, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileDetailsMock() {
    // โครงร่างข้อมูลส่วนตัว
    Column {
        Text("ชื่อ: วริศรา ดิลกกาญจนมาลย์", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("ตำแหน่ง: Android Developer", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ความสามารถ: \n- Jetpack Compose\n- MVVM Architecture\n- Adaptive Design",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun PreviewMobilePart8() {
    _173_LabLeanAndriodTheme {
        AdaptiveProfileScreen()
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun PreviewTabletPart8() {
    _173_LabLeanAndriodTheme {
        AdaptiveProfileScreen()
    }
}
