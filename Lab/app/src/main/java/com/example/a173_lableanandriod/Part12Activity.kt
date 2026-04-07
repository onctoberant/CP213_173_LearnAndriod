package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 12: Modal Bottom Sheet & Middle Dialog
// =============================================================================

class Part12Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OverlayConceptScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayConceptScreen(modifier: Modifier = Modifier) {
    // 1. ควบคุมตัวแปรสถานะว่าเปิด/ปิดป๊อปอัปอยู่หรือไม่
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    
    // 2. สถานะสำหรับคุมแอนิเมชั่นของ BottomSheet โดยเฉพาะ
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Modal & Dialogs",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Dialog กลางจอ: เอาไว้ 'ขัดจังหวะ' คอนเฟิร์มสิ่งที่สำคัญ\n" +
                   "Bottom Sheet: เอาไว้เปิดรายการตั้งค่าที่ยาว หรือเสริมการใช้งานโดยไม่ต้องย้ายหน้า",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ปุ่มสั่งเปิด Dialog
        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("เปิด Middle Dialog (เตือน/ยืนยัน)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ปุ่มสั่งเปิด Bottom Sheet
        Button(onClick = { showBottomSheet = true }, modifier = Modifier.fillMaxWidth()) {
            Text("เปิด Modal Bottom Sheet (เมนูตั้งค่า)")
        }
    }

    // =============================================
    // การวาด UI ลอยทับเมื่อตัวแปรเป็น True
    // =============================================

    // ถ้า showDialog เป็น true ให้เรียก AlertDialog (ป๊อปอัพกลางจอ)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                // หากแตะพื้นหลังสีดำรอบๆ ให้ปิด (ตั้งเป็น false)
                showDialog = false 
            },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            title = {
                Text(text = "ยืนยันการทำรายการ")
            },
            text = {
                Text(text = "Middle Dialog จะบล็อกการใช้งานพื้นหลังจนกว่าผู้ใช้จะให้คำตอบ ใช้เวลาที่จะให้กดยืนยันการลบไฟล์ เป็นต้น")
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("ตกลง (Confirm)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("ยกเลิก (Cancel)")
                }
            }
        )
    }

    // ถ้า showBottomSheet เป็น true ให้แสดงชีทเลื่อนจากข้างล่าง
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            // เนื้อหาข้างใน Bottom Sheet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "เมนูเครื่องมือ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(5) { index ->
                        ListItem(
                            headlineContent = { Text("ตั้งค่ารูปแบบที่ $index") },
                            leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) }
                        )
                        Divider()
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
