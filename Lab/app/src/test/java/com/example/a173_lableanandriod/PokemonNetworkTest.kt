package com.example.a173_lableanandriod

import com.example.a173_lableanandriod.utils.PokemonNetwork
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit Tests สำหรับ PokemonNetwork (Retrofit Singleton)
 *
 * ทดสอบว่า Singleton instance ถูกสร้างอย่างถูกต้อง
 * และ API Service ถูก configure ตาม spec
 */
class PokemonNetworkTest {

    @Test
    fun `PokemonNetwork api should not be null`() {
        // Singleton ต้องสร้าง API Service ได้สำเร็จ
        assertNotNull(PokemonNetwork.api)
    }

    @Test
    fun `PokemonNetwork api should be same instance (singleton)`() {
        // Lazy initialization ต้องคืน instance เดิมทุกครั้ง
        val api1 = PokemonNetwork.api
        val api2 = PokemonNetwork.api
        assertSame(api1, api2)
    }
}
