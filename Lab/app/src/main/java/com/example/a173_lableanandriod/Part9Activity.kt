package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 9: Collapsing Toolbar
// =============================================================================

class Part9Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                CollapsingScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingScreen() {
    // 1. สร้าง ScrollBehavior ที่ควบคุมการยุบ/ขยายของ TopAppBar
    // exitUntilCollapsedScrollBehavior: พับเก็บจนหายไปเมื่อเลื่อนลง แต่จะโผล่กลับมาเฉพาะส่วนหัวเล็กๆ เมื่อเลื่อนขึ้น
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        // 2. ผูก ScrollBehavior กับ Modifier.nestedScroll ของ Scaffold เพื่อรับ Event การไถหน้าจอ
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // 3. ใช้ LargeTopAppBar เพื่อให้มันมีข้อความใหญ่ๆ ตอนกางออก
            LargeTopAppBar(
                title = { 
                    Text(
                        "Collapsing Toolbar",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { /* กลับหน้าก่อนหน้า */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary, // สีตอนหดตัว
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                scrollBehavior = scrollBehavior // ส่ง behavior ที่ผูกกับจอให้กับแถบด้านบน
            )
        }
    ) { innerPadding ->
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding // ใส่ padding ที่ได้จาก Scaffold สำคัญมาก ไม่งั้นโดนบัง
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Concept: Collapsing Top Bar",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "การทำแถบด้านบนแบบยืดหดได้ใน Compose (Material 3) ทำได้ง่ายๆ โดยการใช้ 'ScrollBehavior'.\n\n" +
                            "ระบบจะสร้างตัวแปลภาษา (NestedScrollConnection) เพื่อคอยฟังว่าเนื้อหาในจอ (เช่น LazyColumn) ถูกไถ (Scroll) ลงไปมากน้อยแค่ไหน แล้วส่งต่อไปสะกิดแถบ LargeTopAppBar ให้หดตัว หรือแปลงร่างเป็นแถบเล็กๆ (SmallTopAppBar) มอบพื้นที่หน้าจอให้กับเนื้อหาสำคัญต่อไป\n\n" +
                            "เลื่อนลงไปดูเนื้อหาจำลองข้างล่าง เพื่อสังเกตการหดตัวของขอบด้านบนได้เลย!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // สร้างข้อมูลจำลองสำหรับไถหน้าจอ
            items(50) { index ->
                Text(
                    text = "รายการที่ $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}
