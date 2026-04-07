package com.example.a173_lableanandriod

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

// =============================================================================
// Mission 10: Jetpack Glance App Widget Component
// =============================================================================

class MyGlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // ให้บริการ UI ของ Widget
        provideContent {
            GlanceTheme { // ใช้ Theme ของ Glance เพื่อรองรับ Material You (สีตามระบบ)
                WidgetContent()
            }
        }
    }

    @Composable
    private fun WidgetContent() {
        // แตกต่างจาก Compose ปกติตรงที่ต้องใช้ Component ของ androidx.glance.* เท่านั้น
        // ห้าม import พื้นฐานของ androidx.compose.material3.* หรือ androidx.compose.foundation.*
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp)
                // เมื่อกดที่ Widget จะสั่งเปิดไปยังแอพหน้า Part 10
                .clickable(actionStartActivity<Part10Activity>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Glance Widget",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "เขียนด้วย Compose แตะเพื่อเปิดแอป",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface
                )
            )
        }
    }
}

// คลาสที่ใช้เชื่อมโยงกับระบบของ Android (สืบทอดจาก BroadcastReceiver)
class MyGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    // กำหนดว่า Receiver ตัวนี้จะแสดง Widget ตัวไหน
    override val glanceAppWidget: GlanceAppWidget = MyGlanceWidget()
}
