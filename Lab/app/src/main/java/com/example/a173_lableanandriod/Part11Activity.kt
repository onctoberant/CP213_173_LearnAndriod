package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.delay

// =============================================================================
// Mission 11: Skeleton Loading
// =============================================================================

class Part11Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SkeletonLoadingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// 1. สร้าง Modifier Extension เพื่อทำเอฟเฟกต์ Shimmer แสงกวาดผ่านกล่องมืดๆ
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    
    // ตั้งค่ารัน Animation วนลูปไม่สิ้นสุด
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f, // เลื่อนตำแหน่งสีแสงสว่างไปทางขวาเรื่อยๆ
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_x"
    )

    // วางแปรงไล่สี (Gradient) โดยมีสีสว่างอยู่ตรงกลาง
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(startOffsetX, 0f),
        end = Offset(startOffsetX + 250f, 250f)
    )

    this.background(brush)
}

@Composable
fun SkeletonLoadingScreen(modifier: Modifier = Modifier) {
    // 2. จำลอง State ว่ากำลังติดโหลดอยู่
    var isLoading by remember { mutableStateOf(true) }

    // 3. จำลองการเชื่อมต่อ Database 3 วินาที
    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Skeleton Loading (Shimmer)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "การทำ Skeleton (กระดูก) ของ UI เพื่อแจ้งให้ User รู้ตัวว่าจะต้องเห็นภาพตรงไหน หรือข้อความตรงไหน ช่วยลดความรู้สึกอึดอัดในการรอ แทนการหมุนวงกลมๆ แบบดั้งเดิม",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { isLoading = true }) {
            Text("โหลดข้อมูลใหม่")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(10) {
                if (isLoading) {
                    // แสดงโครงกระดูกตอนยังไม่เสร็จ (Shimmer Effect ดัดแปลงเอง)
                    ListItemSkeleton()
                } else {
                    // แสดงของจริง
                    ListItemReal()
                }
            }
        }
    }
}

@Composable
fun ListItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // วงกลมหลอก (แทนรูปภาพ)
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, CircleShape)
                .shimmerEffect() // เสียบ Modifier เรียกใช้แสงกวาด
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            // เส้นทึบหลอก (แทนบรรทัดแรก)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // เส้นทึบหลอกสั้นๆ (แทนบรรทัดที่สอง)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(15.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun ListItemReal() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("\uD83D\uDE04", color = Color.White) // อีโมจิหน้ายิ้ม
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text("โหลดข้อมูลเสร็จแล้ว!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("รายละเอียดที่เซิร์ฟเวอร์ส่งมา...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
