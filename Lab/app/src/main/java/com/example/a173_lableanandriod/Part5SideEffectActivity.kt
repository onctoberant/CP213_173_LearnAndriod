package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// =============================================================================
// Mission 5: Compose Side Effects (LaunchedEffect & Snackbar)
// =============================================================================

// 1. สร้าง ViewModel เพื่อจำลองการทำงานและส่ง Event ออกไปเป็นครั้งๆ (One-time event)
class SideEffectViewModel : ViewModel() {

    // ใช้ Channel สำหรับดูแล One-time Event เช่น การกระทำที่ส่งแล้วจบไป (Snackbar, Toast, Navigate)
    private val _errorEventChannel = Channel<String>()
    val errorEventFlow = _errorEventChannel.receiveAsFlow()

    // ฟังก์ชันจำลองการเกิด Error คล้ายกับการดึง API แล้วพลาด
    fun triggerApiError() {
        viewModelScope.launch {
            // สมมติว่าพยายามต่อ Server 1 วิ...
            delay(1000)
            
            // หมดเวลาหรือพัง ส่งผลลัพธ์ข้อความ Error
            _errorEventChannel.send("เกิดข้อผิดพลาด: ไม่สามารถเชื่อมต่อกับ Server ได้ (Timeout 504)")
        }
    }
}

class Part5SideEffectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                SideEffectScreen()
            }
        }
    }
}

@Composable
fun SideEffectScreen(viewModel: SideEffectViewModel = viewModel()) {
    
    // 2. เตรียม SnackbarHostState ไว้คอยคุม Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // 3. ใช้ LaunchedEffect (Side Effect) 
    // LaunchedEffect ทำหน้าที่เป็น Coroutine Scope ที่ผูกติดอยู่กับ Composable
    // Unit หรือ true หมายถึง "รันแค่ครั้งเดียวเมื่อเริ่มเปิดหน้านี้ครั้งแรก"
    LaunchedEffect(Unit) {
        viewModel.errorEventFlow.collect { errorMessage ->
            // เมื่อได้รับ Event (ข้อความแจ้งเตือน) ให้แสดงผล
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "รับทราบ",
                duration = SnackbarDuration.Short
            )
        }
    }

    // สร้าง UI ด้วย Scaffold 
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) } // ใส่ Component เตรียมรอแสดง
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "การจัดการ Side Effects",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "กดปุ่มด้านล่างเพื่อจำลองการเกิด Error จากระบบหลังบ้าน (ViewModel)\nและโชว์ Snackbar ผ่าน LaunchedEffect",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 4. ปุ่มสำหรับ Trigger ให้เกิด Error ทดสอบ
            var isLoading by remember { mutableStateOf(false) }
            
            Button(
                onClick = { 
                    viewModel.triggerApiError() 
                    isLoading = true 
                },
                modifier = Modifier.padding(16.dp)
            ) {
                if(isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    LaunchedEffect(Unit) {
                        delay(1005)
                        isLoading = false
                    }
                } else {
                    Text("จำลองการยิง API Error")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPart5() {
    _173_LabLeanAndriodTheme {
        SideEffectScreen()
    }
}
