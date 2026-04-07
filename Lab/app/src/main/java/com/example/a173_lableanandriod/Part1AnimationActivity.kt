package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 1: Compose Animation & Motion — Like Button
// =============================================================================
//
// แนวคิด (Concepts) ที่ใช้:
//
// 1. State — ใช้ remember { mutableStateOf(...) } เก็บสถานะ isLiked
//    เมื่อ State เปลี่ยน Compose จะ recompose แล้ว Animation จะทำงานตาม
//
// 2. animateFloatAsState + spring — ทำ Scale animation ให้ปุ่มขยาย/หด
//    เมื่อกด เปลี่ยน targetValue → Compose จะ animate ค่า Float ไปหาค่าเป้าหมาย
//    spring() ให้ความรู้สึก "เด้ง" เหมือนสปริง
//
// 3. animateColorAsState — ทำ Color animation เปลี่ยนสีพื้นหลังปุ่ม
//    จากสีเทา → สีชมพู แบบ smooth transition
//
// 4. AnimatedVisibility — แสดง/ซ่อน Icon หัวใจ แบบมี enter/exit animation
//    ใช้ fadeIn + slideInHorizontally (เข้า) / fadeOut + slideOutHorizontally (ออก)
//
// =============================================================================

class Part1AnimationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LikeButtonScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Composable Function หลัก: หน้าจอที่มีปุ่ม Like
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun LikeButtonScreen(modifier: Modifier = Modifier) {

    // =====================================================================
    // State: เก็บสถานะกดไลค์ (true = liked, false = not liked)
    // ใช้ remember + mutableStateOf เพื่อให้ Compose รู้ว่าต้อง recompose
    // เมื่อค่าเปลี่ยน → Animation ก็จะเล่นโดยอัตโนมัติ
    // =====================================================================
    var isLiked by remember { mutableStateOf(false) }

    // กราเดียนท์พื้นหลัง
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1a1a2e),  // Dark navy
            Color(0xFF16213e),  // Deep blue
            Color(0xFF0f3460)   // Midnight blue
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ──── หัวข้อ ────
            Text(
                text = "Compose Animation",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "กดปุ่ม Like เพื่อดู Animation",
                fontSize = 16.sp,
                color = Color(0xFFa0a0b0)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ──── ปุ่ม Like ที่มี Animation ────
            AnimatedLikeButton(
                isLiked = isLiked,
                onClick = { isLiked = !isLiked }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ──── สถานะปัจจุบัน ────
            Text(
                text = if (isLiked) "❤️ Liked!" else "ยังไม่ได้กด Like",
                fontSize = 18.sp,
                color = if (isLiked) Color(0xFFff6b9d) else Color(0xFF808090)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Composable Function: ปุ่ม Like ที่มี Animation 3 แบบ
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AnimatedLikeButton(
    isLiked: Boolean,
    onClick: () -> Unit
) {

    // =====================================================================
    // Animation 1: Scale — ขยายปุ่มเมื่อกด (animateFloatAsState + spring)
    // =====================================================================
    // targetValue: liked → ขยาย 1.15 เท่า, not liked → กลับ 1.0
    // spring: dampingRatio ต่ำ = เด้งมาก, stiffness สูง = เด้งเร็ว
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,  // เด้งระดับกลาง
            stiffness = Spring.StiffnessLow                  // สปริงนุ่มๆ
        ),
        label = "scaleAnimation"
    )

    // =====================================================================
    // Animation 2: Color — เปลี่ยนสีพื้นหลังปุ่ม (animateColorAsState)
    // =====================================================================
    // liked → สีชมพู, not liked → สีเทา
    // tween(300) = ใช้เวลา 300ms ในการเปลี่ยนสี
    val buttonColor by animateColorAsState(
        targetValue = if (isLiked) Color(0xFFe91e63) else Color(0xFF6b6b7b),
        animationSpec = tween(durationMillis = 300),
        label = "colorAnimation"
    )

    // =====================================================================
    // ปุ่ม (Button) — ใช้ scale modifier + สีที่ animate
    // =====================================================================
    Button(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)   // ← ใช้ค่า scale จาก animateFloatAsState
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor  // ← ใช้สีจาก animateColorAsState
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isLiked) 8.dp else 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // ─────────────────────────────────────────────────────────
            // Animation 3: AnimatedVisibility — แสดง Icon หัวใจ
            // ─────────────────────────────────────────────────────────
            // เมื่อ isLiked = true → Icon หัวใจจะโผล่ขึ้นมาข้างซ้ายของข้อความ
            // enter: fadeIn + scaleIn (โผล่ขึ้นแบบจางเข้า + ขยายตัว)
            // exit:  fadeOut + scaleOut (หายไปแบบจางออก + หดตัว)
            AnimatedVisibility(
                visible = isLiked,
                enter = fadeIn(animationSpec = tween(300)) +
                        scaleIn(
                            initialScale = 0.3f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            )
                        ),
                exit = fadeOut(animationSpec = tween(200)) +
                        scaleOut(targetScale = 0.3f)
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Liked Heart Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            // ──── ข้อความบนปุ่ม ────
            Text(
                text = if (isLiked) "Liked" else "Like",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Icon ขอบหัวใจ (แสดงเสมอ ตอน not liked)
            // Icon หัวใจเต็ม (แสดงเสมอ ตอน liked) — ตัวนี้อยู่ขวาข้อความ
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Like Icon",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview — ดูผลลัพธ์ใน Android Studio ได้เลย
// ─────────────────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LikeButtonScreenPreview() {
    _173_LabLeanAndriodTheme {
        LikeButtonScreen()
    }
}