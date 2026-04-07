package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 3: Graphics, Effects & Canvas (Showcasing multiple capabilities)
// =============================================================================

class Part3CanvasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CanvasShowcaseScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CanvasShowcaseScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Canvas Showcase",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // =======================================================
        // 1. Animated Donut Chart (จาก Requirement เดิม)
        // =======================================================
        SectionTitle("1. Sweep Angle Animation (Donut)")
        AnimatedDonutChart(
            proportions = listOf(30f, 40f, 30f),
            colors = listOf(Color(0xFFFF5252), Color(0xFF4CAF50), Color(0xFF2196F3)),
            modifier = Modifier.size(200.dp)
        )

        DividerSpacer()

        // =======================================================
        // 2. Line Chart ด้วย Path + Cubic Curve
        // =======================================================
        SectionTitle("2. Custom Path (Smooth Line Chart)")
        PathLineChartExample(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        DividerSpacer()

        // =======================================================
        // 3. วาดรูปร่าง (Shapes) และ Gradient ลวดลาย
        // =======================================================
        SectionTitle("3. Draw Shapes with Gradients")
        ShapesWithGradientExample(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        DividerSpacer()

        // =======================================================
        // 4. การจัดการ Transform (Rotate) สร้าง Animation หมุนดาว
        // =======================================================
        SectionTitle("4. Transform & Rotation (Star)")
        MovingRotatingStarExample(modifier = Modifier.size(160.dp))

        DividerSpacer()

        // =======================================================
        // 5. วาดข้อความลงบน Canvas (ใช้ TextMeasurer)
        // =======================================================
        SectionTitle("5. Drawing Text Directly on Canvas")
        CanvasDrawingTextExample(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        color = Color(0xFF666666)
    )
}

@Composable
fun DividerSpacer() {
    Spacer(modifier = Modifier.height(36.dp))
}

// -----------------------------------------------------------------------------
// ตัวอย่างที่ 1: Donut Chart
// -----------------------------------------------------------------------------
@Composable
fun AnimatedDonutChart(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = proportions.sum()
    val sweepAnglePercentage = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        sweepAnglePercentage.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    Canvas(modifier = modifier) {
        var startAngle = -90f
        val strokeWidth = 50f
        val radius = size.minDimension / 2 - strokeWidth / 2

        proportions.forEachIndexed { index, proportion ->
            val sweepAngle = (proportion / total) * 360f

            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweepAngle * sweepAnglePercentage.value,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )

            startAngle += sweepAngle * sweepAnglePercentage.value
        }
    }
}

// -----------------------------------------------------------------------------
// ตัวอย่างที่ 2: วาดเส้นกราฟด้วย Path และให้เส้นโค้งมน (Cubic Curve)
// -----------------------------------------------------------------------------
@Composable
fun PathLineChartExample(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        // จุดข้อมูลจำลอง (แกน Y คว่ำลง ดังนั้นยิ่งเลขเยอะ ยิ่งอยู่ล่าง)
        val points = listOf(
            Offset(0f, height * 0.9f),
            Offset(width * 0.2f, height * 0.4f),
            Offset(width * 0.4f, height * 0.6f),
            Offset(width * 0.6f, height * 0.2f),
            Offset(width * 0.8f, height * 0.7f),
            Offset(width, height * 0.3f)
        )

        val path = Path()
        path.moveTo(points.first().x, points.first().y)
        
        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            
            // ใช้ cubicTo เพื่อทำลากเส้นโค้ง (Smooth Curve) ระหว่าง 2 จุด
            val controlPoint1 = Offset((p0.x + p1.x) / 2, p0.y)
            val controlPoint2 = Offset((p0.x + p1.x) / 2, p1.y)
            path.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                p1.x, p1.y
            )
        }

        // วาดเส้นตาม Path ที่คำนวณไว้
        drawPath(
            path = path,
            color = Color(0xFF673AB7),
            style = Stroke(
                width = 8f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // วาดจุดกลมๆ ทับพิกัดข้อมูลให้ดูสวยขึ้น
        for (p in points) {
            drawCircle(color = Color(0xFFFF9800), radius = 12f, center = p)
        }
    }
}

// -----------------------------------------------------------------------------
// ตัวอย่างที่ 3: วาดรูปร่างอิสระ พร้อมใส่สีแบบไล่ระดับ (Gradient)
// -----------------------------------------------------------------------------
@Composable
fun ShapesWithGradientExample(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // Gradient Brush สำหรับพื้นผิว
        val linearGradient = Brush.linearGradient(
            colors = listOf(Color(0xFF00C9FF), Color(0xFF92FE9D))
        )
        
        // วาดสี่เหลี่ยมขอบมนซ้ายมือ
        drawRoundRect(
            brush = linearGradient,
            size = Size(size.width * 0.55f, size.height),
            cornerRadius = CornerRadius(40f, 40f)
        )
        
        // วาดวงกลมรัศมีแบบไล่สี (Radial Gradient) ขวามือ
        val radialGradient = Brush.radialGradient(
            colors = listOf(Color(0xFFFFEE58), Color(0xFFFF9800), Color(0xFFF44336)),
            center = Offset(size.width * 0.8f, size.height * 0.5f),
            radius = size.height * 0.45f
        )
        drawCircle(
            brush = radialGradient,
            radius = size.height * 0.45f,
            center = Offset(size.width * 0.8f, size.height * 0.5f)
        )
    }
}

// -----------------------------------------------------------------------------
// ตัวอย่างที่ 4: Transform (Rotate) สร้างดาวหมุน
// -----------------------------------------------------------------------------
@Composable
fun MovingRotatingStarExample(modifier: Modifier = Modifier) {
    // กำหนด Animation วนลูปไม่รู้จบเพื่อหมุนดาว
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "StarRotation"
    )

    Canvas(modifier = modifier) {
        val path = Path()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val outerRadius = size.width / 2
        val innerRadius = outerRadius / 2.5f
        
        // วาดตีกรอบพิกัดดาว 5 แฉก
        val sweeps = 5
        var angle = -Math.PI / 2
        val anglePerSweep = Math.PI / sweeps
        
        path.moveTo(
            (centerX + Math.cos(angle) * outerRadius).toFloat(),
            (centerY + Math.sin(angle) * outerRadius).toFloat()
        )
        
        for (i in 0 until sweeps) {
            angle += anglePerSweep
            path.lineTo(
                (centerX + Math.cos(angle) * innerRadius).toFloat(),
                (centerY + Math.sin(angle) * innerRadius).toFloat()
            )
            angle += anglePerSweep
            path.lineTo(
                (centerX + Math.cos(angle) * outerRadius).toFloat(),
                (centerY + Math.sin(angle) * outerRadius).toFloat()
            )
        }
        path.close()

        // ใช้คำสั่ง withTransform (rotate) เพื่อหมุนตัวกล้องของ Canvas ก่อนค่อยวาด
        withTransform({
            rotate(degrees = rotation, pivot = Offset(centerX, centerY))
        }) {
            drawPath(
                path = path,
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Red, Color.Magenta, Color.Blue, Color.Cyan, Color.Green, Color.Yellow, Color.Red),
                    center = Offset(centerX, centerY)
                )
            )
        }
    }
}

// -----------------------------------------------------------------------------
// ตัวอย่างที่ 5: เขียนข้อความบน Canvas โดยคำนวณกรอบด้วย TextMeasurer
// -----------------------------------------------------------------------------
@Composable
fun CanvasDrawingTextExample(modifier: Modifier = Modifier) {
    // ต้องจดจำ TextMeasurer ไว้ เพื่อใช้วัดอักษรล่วงหน้าก่อนลงพู่กันวาด
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        // ประเมินขนาด Text
        val measuredText = textMeasurer.measure(
            text = "Hello Canvas \uD83C\uDFA8",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        )
        
        // วาดเงาข้อความ
        drawText(
            textLayoutResult = measuredText,
            color = Color(0x66000000), // สีดำโปร่งใส
            topLeft = Offset(
                x = (center.x - measuredText.size.width / 2) + 6f, // ขยับเกิดกรอบเงา X
                y = (center.y - measuredText.size.height / 2) + 6f // ขยับเกิดกรอบเงา Y
            )
        )
        
        // วาดข้อความตัวจริงทับ
        drawText(
            textLayoutResult = measuredText,
            color = Color(0xFFE91E63), // สีชมพู
            topLeft = Offset(
                x = center.x - measuredText.size.width / 2,
                y = center.y - measuredText.size.height / 2
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPart3Showcase() {
    _173_LabLeanAndriodTheme {
        CanvasShowcaseScreen()
    }
}
