package com.example.a173_lableanandriod

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

// =============================================================================
// Mission 6: View Interoperability (WebView ใน Compose)
// =============================================================================

// 1. สร้าง ViewModel เพื่อเก็บสถานะของ URL ที่เรากำลังดูอยู่
class WebViewModel : ViewModel() {
    // ค่าเริ่มต้นเป็น Google
    var currentUrl by mutableStateOf("https://www.google.com")
        private set

    fun updateUrl(newUrl: String) {
        // ออโต้เติม https:// ให้เมื่อไม่มี
        val formattedUrl = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            newUrl
        } else {
            "https://$newUrl"
        }
        currentUrl = formattedUrl
    }
}

class Part6WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebViewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled") // ยอมให้เว็บรัน JS เพื่อการแสดงผลที่ถูกต้อง
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(modifier: Modifier = Modifier, viewModel: WebViewModel = viewModel()) {
    
    // ดึงค่า URL ปัจจุบัน
    val currentUrl = viewModel.currentUrl
    
    // State สำหรับกล่องข้อความพิมพ์ (อิสระจากโชว์จริง เพราะค่ายังไม่ได้ยืนยันจนกว่าจะกดปุ่ม)
    var inputText by remember { mutableStateOf(currentUrl) }
    
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize()) {

        // 4. สร้าง TextField พร้อม ปุ่มพิมพ์ 
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("URL") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        viewModel.updateUrl(inputText)
                        keyboardController?.hide()
                    }
                )
            )

            IconButton(
                onClick = { 
                    viewModel.updateUrl(inputText) 
                    keyboardController?.hide()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Go")
            }
        }

        // 2 & 3. ใช้ AndroidView เพื่อโหลด component แบบดั้งเดิมจาก Android Library
        // ในที่นี้เราจะทดลองดึงจากโครงสร้าง layout_webview.xml ท่่สร้างไว้
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            // block แรก: จะรันครั้งเดียวตอนสร้าง View (ตอนต้น)
            factory = { context ->
                // ทำการโยงฝั่ง (Inflate) XML ที่เราจัดหน้าไว้มาใช้ใน Compose
                val view = android.view.LayoutInflater.from(context).inflate(R.layout.layout_webview, null)
                val webView = view.findViewById<WebView>(R.id.my_webview)

                // กำหนดค่าต่างๆ เบื้องต้น
                webView.settings.javaScriptEnabled = true 
                
                // ตั้งเป็น WebViewClient เพื่อป้องกันไม่ให้เด้งไปแอพ Chrome ภายนอก
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        return false // false = โหลดในนี้เลย
                    }
                }
                
                view // return ตัวหน้าจอหลักกลับไปให้ AndroidView วาด
            },
            // block update: จะรันใหม่เสมอเมื่อตัวแปรที่เป็น State ด้านนอกเปลี่ยนแปลง (currentUrl)
            update = { view ->
                val webView = view.findViewById<WebView>(R.id.my_webview)
                // หาก URL ไม่ตรงกันกับอันล่าสุด ให้โหลดใหม่
                if(webView.url != currentUrl){
                    webView.loadUrl(currentUrl)
                }
            }
        )
    }
}
