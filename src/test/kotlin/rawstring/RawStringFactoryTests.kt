package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.CustomAssertions
import fr.geming400.gddotkt.exceptions.InvalidRawStringException
import fr.geming400.gddotkt.objects.SimpleObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import kotlin.test.Test

@Tag("editor")
private class RawStringFactoryTests {
    // Every other .asMap related functions all depend on
    // RawStringFactory.asMap so there's no need to make tests
    // for them too

    @Test
    @DisplayName("RawStringFactory.asIntMap test")
    fun intIdMapTest() {
        val obj = SimpleObject(0u, 0f, 0f)

        Assertions.assertEquals(obj.asMap(), mapOf(Pair(1u, obj.objID), Pair(2u, obj.x), Pair(3u, obj.y)))

        obj.channel.value = 7
        Assertions.assertEquals(obj.asMap(), mapOf(Pair(1u, obj.objID), Pair(2u, obj.x), Pair(3u, obj.y), Pair(170u, obj.channel)))
    }

    @Test
    @DisplayName("RawStringFactory.asMap test")
    fun mapTest() {
        val obj = SimpleObject(0u, 0f, 0f)
        val rawStringFactory = obj.rawStringFactory

        Assertions.assertEquals(rawStringFactory.asMap(), mapOf(Pair(1.id, obj.objID), Pair(2.id, obj.x), Pair(3.id, obj.y)))

        obj.channel.value = 7
        Assertions.assertEquals(rawStringFactory.asMap(), mapOf(Pair(1.id, obj.objID), Pair(2.id, obj.x), Pair(3.id, obj.y), Pair(170.id, obj.channel)))
    }

    @Test
    @DisplayName("RawStringFactory.areRawStringEquals test")
    fun areRawStringEqualsTest() {
        val rawString1 = "1,10,2,20,3,30"
        val rawString2 = "3,30,1,10,2,20"
        val rawString3 = "1,10,2,20,3,30,4,40"

        CustomAssertions.assertRawStringEquals(rawString1, rawString2)
        CustomAssertions.assertRawStringNotEquals(rawString1, rawString3)
        CustomAssertions.assertRawStringNotEquals(rawString2, rawString3)
    }

    @Test
    @DisplayName("RawStringFactory.rawStringToMap test")
    fun rawStringToMapTest() {
        val rawString1 = "1,10,2,20,3,30"

        @Suppress("KotlinMisorderedAssertEqualsArguments")
        Assertions.assertEquals(
            mapOf(Pair(1.id, "10"), Pair(2.id, "20"), Pair(3.id, "30")),
            RawStringFactory.rawStringToMap(rawString1)
        )

        val rawString2 = "1,10,2,20,-3,30"
        Assertions.assertThrows(IllegalArgumentException::class.java) { RawStringFactory.rawStringToMap(rawString2) }

        val rawString3 = "1,10," // Even number of commas = invalid raw string
        Assertions.assertThrows(InvalidRawStringException::class.java) { RawStringFactory.rawStringToMap(rawString3) }
    }
}