package com.example.a173_lableanandriod

import com.example.a173_lableanandriod.utils.SharedPreferencesUtil
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit Tests สำหรับ SharedPreferencesUtil
 *
 * เนื่องจาก SharedPreferencesUtil ต้องใช้ Android Context ในการ init()
 * test นี้จะเน้นทดสอบพฤติกรรมเมื่อยังไม่ได้ init (sharedPreferences == null)
 * ซึ่งเป็น edge case ที่สำคัญในการป้องกัน NullPointerException
 *
 * สำหรับ test แบบเต็มรูปแบบที่ใช้ Context จริง ควรใช้ androidTest (Instrumented Test)
 */
class SharedPreferencesUtilTest {

    // ═══════════════════════════════════════════════════════════════════════
    // Tests เมื่อยังไม่ได้ init (graceful null handling)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `getString - should return default value when not initialized`() {
        // เมื่อยังไม่ได้เรียก init() sharedPreferences เป็น null
        // ต้อง return defaultValue แทนที่จะ crash
        val result = SharedPreferencesUtil.getString("any_key", "default_val")
        assertEquals("default_val", result)
    }

    @Test
    fun `getString - default defaultValue should be empty string`() {
        val result = SharedPreferencesUtil.getString("any_key")
        assertEquals("", result)
    }

    @Test
    fun `getInt - should return default value when not initialized`() {
        val result = SharedPreferencesUtil.getInt("any_key", 42)
        assertEquals(42, result)
    }

    @Test
    fun `getInt - default defaultValue should be zero`() {
        val result = SharedPreferencesUtil.getInt("any_key")
        assertEquals(0, result)
    }

    @Test
    fun `getBoolean - should return default value when not initialized`() {
        val result = SharedPreferencesUtil.getBoolean("any_key", true)
        assertTrue(result)
    }

    @Test
    fun `getBoolean - default defaultValue should be false`() {
        val result = SharedPreferencesUtil.getBoolean("any_key")
        assertFalse(result)
    }

    @Test
    fun `saveString - should not crash when not initialized`() {
        // ต้องไม่ throw exception
        SharedPreferencesUtil.saveString("key", "value")
    }

    @Test
    fun `saveInt - should not crash when not initialized`() {
        SharedPreferencesUtil.saveInt("key", 123)
    }

    @Test
    fun `saveBoolean - should not crash when not initialized`() {
        SharedPreferencesUtil.saveBoolean("key", true)
    }

    @Test
    fun `remove - should not crash when not initialized`() {
        SharedPreferencesUtil.remove("key")
    }

    @Test
    fun `clearAll - should not crash when not initialized`() {
        SharedPreferencesUtil.clearAll()
    }
}
