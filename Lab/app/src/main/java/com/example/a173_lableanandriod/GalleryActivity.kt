package com.example.a173_lableanandriod

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GalleryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GalleryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // ระบุ Permission ที่ต้องการตามเวอร์ชัน Android
    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES  // Android 13+
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE  // Android 12 และต่ำกว่า
    }

    // Launcher สำหรับเปิด Gallery รับค่ากลับมาเป็น Uri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Launcher สำหรับขอ Permission เข้าถึงไฟล์
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // หากผู้ใช้อนุญาต ให้เปิด Gallery ทันที
            galleryLauncher.launch("image/*")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedImageUri != null) {
            // แสดงรูปภาพด้วย Coil AsyncImage (รองรับการโหลดจาก Uri)
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )
        } else {
            Text(
                text = "ยังไม่ได้เลือกรูปภาพ",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(onClick = {
            // ตรวจสอบว่าได้รับ Permission แล้วหรือยัง
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                requiredPermission
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                // มี Permission แล้ว — เปิด Gallery ได้เลย
                galleryLauncher.launch("image/*")
            } else {
                // ยังไม่มี Permission — ขอ Permission จากผู้ใช้ก่อน
                permissionLauncher.launch(requiredPermission)
            }
        }) {
            Text(text = "เลือกรูปภาพจาก Gallery")
        }
    }
}
