package com.example.a173_lableanandriod

import com.example.a173_lableanandriod.utils.PokemonEntry
import com.example.a173_lableanandriod.utils.PokemonSpecies
import com.example.a173_lableanandriod.utils.PokedexResponse
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit Tests สำหรับ Data Classes ของโปรเจกต์
 *
 * ทดสอบ: AccelerometerData, LocationData, PokemonEntry, PokemonSpecies, PokedexResponse
 * สิ่งที่เทสต์: การสร้าง instance, ค่า default, equality, copy, toString
 */
class DataClassesTest {

    // ═══════════════════════════════════════════════════════════════════════
    // AccelerometerData Tests
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `AccelerometerData - default values should be zero`() {
        val data = AccelerometerData()
        assertEquals(0f, data.x, 0.001f)
        assertEquals(0f, data.y, 0.001f)
        assertEquals(0f, data.z, 0.001f)
    }

    @Test
    fun `AccelerometerData - should store custom values correctly`() {
        val data = AccelerometerData(x = 1.5f, y = -2.3f, z = 9.8f)
        assertEquals(1.5f, data.x, 0.001f)
        assertEquals(-2.3f, data.y, 0.001f)
        assertEquals(9.8f, data.z, 0.001f)
    }

    @Test
    fun `AccelerometerData - same values should be equal`() {
        val data1 = AccelerometerData(x = 1.0f, y = 2.0f, z = 3.0f)
        val data2 = AccelerometerData(x = 1.0f, y = 2.0f, z = 3.0f)
        assertEquals(data1, data2)
    }

    @Test
    fun `AccelerometerData - different values should not be equal`() {
        val data1 = AccelerometerData(x = 1.0f, y = 2.0f, z = 3.0f)
        val data2 = AccelerometerData(x = 4.0f, y = 5.0f, z = 6.0f)
        assertNotEquals(data1, data2)
    }

    @Test
    fun `AccelerometerData - copy should create modified copy`() {
        val original = AccelerometerData(x = 1.0f, y = 2.0f, z = 3.0f)
        val copied = original.copy(x = 99.0f)
        assertEquals(99.0f, copied.x, 0.001f)
        assertEquals(2.0f, copied.y, 0.001f) // ค่าเดิมไม่เปลี่ยน
        assertEquals(3.0f, copied.z, 0.001f) // ค่าเดิมไม่เปลี่ยน
    }

    @Test
    fun `AccelerometerData - can handle negative values`() {
        val data = AccelerometerData(x = -9.8f, y = -0.5f, z = -3.14f)
        assertTrue(data.x < 0)
        assertTrue(data.y < 0)
        assertTrue(data.z < 0)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // LocationData Tests
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `LocationData - default values should be zero`() {
        val data = LocationData()
        assertEquals(0.0, data.latitude, 0.001)
        assertEquals(0.0, data.longitude, 0.001)
        assertEquals(0.0, data.altitude, 0.001)
    }

    @Test
    fun `LocationData - should store Bangkok coordinates correctly`() {
        // พิกัดกรุงเทพมหานคร
        val bangkok = LocationData(
            latitude = 13.7563,
            longitude = 100.5018,
            altitude = 1.5
        )
        assertEquals(13.7563, bangkok.latitude, 0.0001)
        assertEquals(100.5018, bangkok.longitude, 0.0001)
        assertEquals(1.5, bangkok.altitude, 0.001)
    }

    @Test
    fun `LocationData - should handle negative coordinates (Southern hemisphere)`() {
        // ตัวอย่าง: พิกัดซีดนีย์ (ซีกโลกใต้)
        val sydney = LocationData(latitude = -33.8688, longitude = 151.2093, altitude = 58.0)
        assertTrue(sydney.latitude < 0)
        assertTrue(sydney.longitude > 0)
    }

    @Test
    fun `LocationData - same coordinates should be equal`() {
        val loc1 = LocationData(latitude = 13.75, longitude = 100.50, altitude = 5.0)
        val loc2 = LocationData(latitude = 13.75, longitude = 100.50, altitude = 5.0)
        assertEquals(loc1, loc2)
        assertEquals(loc1.hashCode(), loc2.hashCode())
    }

    @Test
    fun `LocationData - copy should create modified copy`() {
        val original = LocationData(latitude = 13.75, longitude = 100.50, altitude = 5.0)
        val newAltitude = original.copy(altitude = 100.0)
        assertEquals(13.75, newAltitude.latitude, 0.001)
        assertEquals(100.0, newAltitude.altitude, 0.001)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PokemonSpecies Tests
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `PokemonSpecies - should store name and URL correctly`() {
        val species = PokemonSpecies(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon-species/1/"
        )
        assertEquals("bulbasaur", species.name)
        assertEquals("https://pokeapi.co/api/v2/pokemon-species/1/", species.url)
    }

    @Test
    fun `PokemonSpecies - same species should be equal`() {
        val species1 = PokemonSpecies(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon-species/25/")
        val species2 = PokemonSpecies(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon-species/25/")
        assertEquals(species1, species2)
    }

    @Test
    fun `PokemonSpecies - different species should not be equal`() {
        val pikachu = PokemonSpecies(name = "pikachu", url = "url1")
        val charmander = PokemonSpecies(name = "charmander", url = "url2")
        assertNotEquals(pikachu, charmander)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PokemonEntry Tests
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `PokemonEntry - should store entry number and species`() {
        val species = PokemonSpecies(name = "bulbasaur", url = "url")
        val entry = PokemonEntry(entry_number = 1, pokemon_species = species)
        assertEquals(1, entry.entry_number)
        assertEquals("bulbasaur", entry.pokemon_species.name)
    }

    @Test
    fun `PokemonEntry - same entries should be equal`() {
        val species = PokemonSpecies(name = "charmander", url = "url")
        val entry1 = PokemonEntry(entry_number = 4, pokemon_species = species)
        val entry2 = PokemonEntry(entry_number = 4, pokemon_species = species)
        assertEquals(entry1, entry2)
    }

    @Test
    fun `PokemonEntry - entry number starts from 1`() {
        val species = PokemonSpecies(name = "bulbasaur", url = "url")
        val entry = PokemonEntry(entry_number = 1, pokemon_species = species)
        assertTrue(entry.entry_number > 0)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PokedexResponse Tests
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    fun `PokedexResponse - should contain list of pokemon entries`() {
        val entries = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(4, PokemonSpecies("charmander", "url2")),
            PokemonEntry(7, PokemonSpecies("squirtle", "url3"))
        )
        val response = PokedexResponse(pokemon_entries = entries)
        assertEquals(3, response.pokemon_entries.size)
    }

    @Test
    fun `PokedexResponse - empty list should have size zero`() {
        val response = PokedexResponse(pokemon_entries = emptyList())
        assertTrue(response.pokemon_entries.isEmpty())
        assertEquals(0, response.pokemon_entries.size)
    }

    @Test
    fun `PokedexResponse - should access first and last pokemon`() {
        val entries = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(151, PokemonSpecies("mew", "url151"))
        )
        val response = PokedexResponse(pokemon_entries = entries)
        assertEquals("bulbasaur", response.pokemon_entries.first().pokemon_species.name)
        assertEquals("mew", response.pokemon_entries.last().pokemon_species.name)
    }

    @Test
    fun `PokedexResponse - can filter pokemon by entry number`() {
        val entries = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1")),
            PokemonEntry(25, PokemonSpecies("pikachu", "url25")),
            PokemonEntry(150, PokemonSpecies("mewtwo", "url150"))
        )
        val response = PokedexResponse(pokemon_entries = entries)

        // ค้นหา Pikachu (entry_number = 25)
        val pikachu = response.pokemon_entries.find { it.entry_number == 25 }
        assertNotNull(pikachu)
        assertEquals("pikachu", pikachu!!.pokemon_species.name)
    }

    @Test
    fun `PokedexResponse - searching non-existent entry returns null`() {
        val entries = listOf(
            PokemonEntry(1, PokemonSpecies("bulbasaur", "url1"))
        )
        val response = PokedexResponse(pokemon_entries = entries)

        val notFound = response.pokemon_entries.find { it.entry_number == 999 }
        assertNull(notFound)
    }
}
