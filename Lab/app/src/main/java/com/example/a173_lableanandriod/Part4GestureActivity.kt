package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// =============================================================================
// Mission 4: Advanced Gestures & Interactive UI (Swipe & Drag)
// =============================================================================

class TodoViewModel : ViewModel() {
    val todoList = mutableStateListOf(
        "เรียน Kotlin",
        "ทำแบบฝึกหัด Compose พื้นฐาน",
        "ศึกษา Clean Architecture",
        "ออกกำลังกายตอนเย็น",
        "ดูหนัง Netflix ชิลล์ ๆ"
    )

    fun removeItem(item: String) {
        todoList.remove(item)
    }
}

class Part4GestureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GestureScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GestureScreen(modifier: Modifier = Modifier, viewModel: TodoViewModel = viewModel()) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // =======================================================
        // Section 1: Drag and Drop (Modifier.pointerInput)
        // =======================================================
        Text(
            text = "1. Drag & Drop (pointerInput)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("พื้นที่ลากวาง", color = Color.LightGray)

            // State เก็บพิกัด x, y ปัจจุบันของกล่อง
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            // กล่องที่ลากได้
            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .size(80.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(16.dp))
                    // .pointerInput คือตัวจับ Event การสัมผัสขั้นสูง
                    .pointerInput(Unit) {
                        // detectDragGestures ดักจับการลาก
                        detectDragGestures { change, dragAmount ->
                            change.consume() // บริโภค event เพื่อไม่ให้ส่งผ่านไปข้างล่าง
                            offsetX += dragAmount.x // บวกระยะทางที่นิ้วเลื่อนไปในแกน X
                            offsetY += dragAmount.y // บวกระยะทางที่นิ้วเลื่อนไปในแกน Y
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Drag Me!", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        // =======================================================
        // Section 2: Swipe to Dismiss (LazyColumn)
        // =======================================================
        Text(
            text = "2. To-Do List (Swipe ลบได้)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = viewModel.todoList,
                key = { it }
            ) { item ->
                var isDismissed by remember { mutableStateOf(false) }

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        // ถ้าปัดไปทางซ้ายสุด
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            isDismissed = true
                            true
                        } else {
                            false
                        }
                    }
                )

                LaunchedEffect(isDismissed) {
                    if (isDismissed) {
                        delay(250) // หน่วงให้แอนิเมชั่นเล่นเสร็จ
                        viewModel.removeItem(item)
                    }
                }

                AnimatedVisibility(
                    visible = !isDismissed,
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 250)) + fadeOut()
                ) {
                    // ใช้ SwipeToDismissBox เพื่อความสามารถ Swipe-to-Dismiss
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false, // ให้ปัดไปซ้ายทางเดียว
                        backgroundContent = {
                            val color = when (dismissState.dismissDirection) {
                                SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF5252)
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .background(color, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White
                                )
                            }
                        },
                        content = {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPart4() {
    _173_LabLeanAndriodTheme {
        GestureScreen()
    }
}
