package com.example.a173_lableanandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =============================================================================
// Mission 2: Complex Lists & Pagination (Sticky Header & Infinite Scroll)
// =============================================================================

// 1. สร้าง ViewModel เพื่อจัดการข้อมูลและสถานะการโหลด
class ContactViewModel : ViewModel() {
    // Mock ข้อมูลรายชื่อ A-Z (รายชื่อจำลองทั้งหมด)
    private val allNames = listOf(
        "Alice", "Adam", "Amelia", "Aaron", "Anna",
        "Bob", "Brian", "Bella", "Benjamin", "Bailey",
        "Charlie", "Chloe", "Carter", "Claire", "Caleb",
        "David", "Diana", "Daniel", "Daisy", "Dylan",
        "Eve", "Ethan", "Emma", "Elijah", "Emily",
        "Frank", "Fiona", "Felix", "Faith", "Finn",
        "George", "Grace", "Gabriel", "Gianna", "Gavin",
        "Harry", "Hannah", "Henry", "Hazel", "Harrison",
        "Ian", "Isabella", "Isaac", "Ivy", "Isaiah"
    ).sorted()

    // State เก็บ List ของรายชื่อที่จะแสดงในหน้าจอ
    var contacts by mutableStateOf<List<String>>(emptyList())
        private set

    // State แจ้งว่ากำลังโหลดข้อมูลเพิ่มไหม
    var isLoading by mutableStateOf(false)
        private set

    // ตัวแปรสำหรับแบ่งโหลดทีละ 15 คน
    private val pageSize = 15
    private var currentPage = 0

    init {
        // โหลดข้อมูลชุดแรกเมื่อ ViewModel ถูกใช้งาน
        loadMoreContacts()
    }

    // 3. ฟังก์ชันจำลองการโหลดข้อมูลเพิ่ม (Pagination)
    fun loadMoreContacts() {
        // หากกำลังโหลดอยู่หรือข้อมูลหมดแล้ว ให้ข้ามไปเลย
        if (isLoading || contacts.size >= allNames.size) return

        viewModelScope.launch {
            isLoading = true
            // ใช้ delay หน่วงเวลา 2 วินาที (จำลองการดึง API)
            delay(2000)

            val nextIndex = (currentPage + 1) * pageSize
            val newContacts = allNames.take(nextIndex)
            
            contacts = newContacts
            currentPage++
            
            isLoading = false
        }
    }
}

class Part2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _173_LabLeanAndriodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactViewModel = viewModel()
) {
    val contacts = viewModel.contacts
    val isLoading = viewModel.isLoading

    // จัดกลุ่มรายชื่อแยกตามตัวอักษรแรก เพื่อนำไปทำ Sticky Header
    val groupedContacts = contacts.groupBy { it.first().uppercaseChar() }
    
    // State สำหรับจดจำตำแหน่งการ Scroll
    val listState = rememberLazyListState()

    // เลื่อนจนถึงด่านล่างสุดหรือยัง
    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                // ตรวจว่า item สุดท้ายที่โชว์คือ item ที่ลงท้ายแล้วและสุดจอหรือเปล่า
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount) &&
                        (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }

    // 3. Trigger เพื่อ Loadmore
    LaunchedEffect(isScrolledToEnd) {
        if (isScrolledToEnd && !isLoading) {
            viewModel.loadMoreContacts()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // ค่อยๆ โชว์ทีละกลุ่มอักษรแรก
            groupedContacts.forEach { (initial, names) ->
                // 2. ใช้ stickyHeader แสดงชื่อกลุ่ม
                stickyHeader {
                    Text(
                        text = initial.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(names) { name ->
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }

            // 4. แสดง CircularProgressIndicator ด้านล่างสุดเมื่อ isLoading = true
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    _173_LabLeanAndriodTheme {
        ContactListScreen()
    }
}