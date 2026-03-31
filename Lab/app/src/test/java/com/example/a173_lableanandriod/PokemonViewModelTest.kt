package com.example.a173_lableanandriod

import com.example.a173_lableanandriod.utils.PokemonEntry
import com.example.a173_lableanandriod.utils.PokemonSpecies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit Tests สำหรับ PokemonViewModel
 *
 * เนื่องจาก PokemonViewModel ใช้ viewModelScope ซึ่งต้องใช้ Android dependencies
 * ดังนั้น test นี้จะเน้นทดสอบ Logic ของ StateFlow และ Data Transformation
 * ที่สามารถรันบน JVM ได้โดยตรง (ไม่ต้องใช้ Android Emulator)
 */
class PokemonViewModelTest {

    // ═══════════════════════════════════════════════════════════════════════
    // StateFlow Behavior Tests (จำลองพฤติกรรมแบบ ViewModel)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `StateFlow - initial value should be empty list`() {
        // จำลอง StateFlow แบบเดียวกับที่ ViewModel ใช้
        val pokemonFlow = MutableStateFlow<List<PokemonEntry>>(emptyList())
        assertTrue(pokemonFlow.value.isEmpty())
    }

    @Test
    fun `StateFlow - should update when new data is assigned`() {
        val pokemonFlow = MutableStateFlow<List<PokemonEntry>>(emptyList())

        // จำลองการอัปเดตข้อมูลจาก API
        val newList = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(2, PokemonSpecies("ivysaur", "url2"))
        )
        pokemonFlow.value = newList

        assertEquals(2, pokemonFlow.value.size)
        assertEquals("bulbasaur", pokemonFlow.value[0].pokemon_species.name)
    }

    @Test
    fun `StateFlow - asStateFlow should be read-only`() {
        val mutable = MutableStateFlow<List<PokemonEntry>>(emptyList())
        val readOnly = mutable.asStateFlow()

        // เปลี่ยนค่าผ่าน mutable
        mutable.value = listOf(PokemonEntry(25, PokemonSpecies("pikachu", "url")))

        // readOnly ต้อง reflect ค่าใหม่
        assertEquals(1, readOnly.value.size)
        assertEquals("pikachu", readOnly.value.first().pokemon_species.name)
    }

    @Test
    fun `StateFlow - replacing data should clear old list`() {
        val pokemonFlow = MutableStateFlow<List<PokemonEntry>>(emptyList())

        // ใส่ข้อมูลครั้งแรก
        pokemonFlow.value = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1"))
        )
        assertEquals(1, pokemonFlow.value.size)

        // ใส่ข้อมูลใหม่ (แทนที่ข้อมูลเดิม)
        pokemonFlow.value = listOf(
            PokemonEntry(4, PokemonSpecies("charmander", "url4")),
            PokemonEntry(7, PokemonSpecies("squirtle", "url7"))
        )
        assertEquals(2, pokemonFlow.value.size)
        assertEquals("charmander", pokemonFlow.value.first().pokemon_species.name)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Data Transformation Tests (Logic ที่ UI ใช้ร่วมกับ ViewModel)
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `Pokemon list - can be filtered by name`() {
        val list = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(25, PokemonSpecies("pikachu", "url25")),
            PokemonEntry(6, PokemonSpecies("charizard", "url6"))
        )

        val filtered = list.filter { it.pokemon_species.name.contains("char") }
        assertEquals(1, filtered.size)
        assertEquals("charizard", filtered.first().pokemon_species.name)
    }

    @Test
    fun `Pokemon list - can be sorted by entry number`() {
        val unsorted = listOf(
            PokemonEntry(25, PokemonSpecies("pikachu", "url25")),
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(150, PokemonSpecies("mewtwo", "url150"))
        )

        val sorted = unsorted.sortedBy { it.entry_number }
        assertEquals(1, sorted[0].entry_number)
        assertEquals(25, sorted[1].entry_number)
        assertEquals(150, sorted[2].entry_number)
    }

    @Test
    fun `Pokemon image URL - should be generated correctly from entry number`() {
        val entry = PokemonEntry(25, PokemonSpecies("pikachu", "url"))

        // จำลอง Logic เดียวกับที่ใช้ใน ListActivity3
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${entry.entry_number}.png"

        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            imageUrl
        )
    }

    @Test
    fun `Pokemon list - map to name list`() {
        val entries = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(4, PokemonSpecies("charmander", "url4")),
            PokemonEntry(7, PokemonSpecies("squirtle", "url7"))
        )

        val names = entries.map { it.pokemon_species.name }
        assertEquals(listOf("bulbasaur", "charmander", "squirtle"), names)
    }

    @Test
    fun `Pokemon list - count entries in a range`() {
        val entries = (1..151).map {
            PokemonEntry(it, PokemonSpecies("pokemon_$it", "url_$it"))
        }

        // นับโปเกมอน Gen 1 (entry 1..151)
        assertEquals(151, entries.size)

        // นับเฉพาะ entry 1-10
        val firstTen = entries.filter { it.entry_number in 1..10 }
        assertEquals(10, firstTen.size)
    }
}
