package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.objects.SimpleObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

private class RawStringFactoryTests {
    // Every other .asMap related functions all depend on
    // RawStringFactoryTests.asMap

    @Test
    @DisplayName("RawStringBuilder.asIntMap test")
    fun intIdMapTest() {
        val obj = SimpleObject(0u, 0f, 0f)

        Assertions.assertEquals(obj.asMap(), mapOf(Pair(1u, obj.objID), Pair(2u, obj.x), Pair(3u, obj.y)))

        obj.channel.value = 7
        Assertions.assertEquals(obj.asMap(), mapOf(Pair(1u, obj.objID), Pair(2u, obj.x), Pair(3u, obj.y), Pair(170u, obj.channel)))
    }

    @Test
    @DisplayName("RawStringBuilder.asMap test")
    fun mapTest() {
        val obj = SimpleObject(0u, 0f, 0f)
        val rawStringFactory = obj.rawStringFactory

        Assertions.assertEquals(rawStringFactory.asMap(), mapOf(Pair(1.id, obj.objID), Pair(2.id, obj.x), Pair(3.id, obj.y)))

        obj.channel.value = 7
        Assertions.assertEquals(rawStringFactory.asMap(), mapOf(Pair(1.id, obj.objID), Pair(2.id, obj.x), Pair(3.id, obj.y), Pair(170.id, obj.channel)))
    }
}