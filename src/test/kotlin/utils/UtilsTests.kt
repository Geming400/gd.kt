package utils

import TestTags
import exceptions.IllegalTypeException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.random.Random

private class UtilsTest {
    @Test
    fun isPrimitiveTest() {
        Assertions.assertTrue(isPrimitive("0"))
        Assertions.assertTrue(isPrimitive(0))
        Assertions.assertTrue(isPrimitive(0.0f))
        Assertions.assertTrue(isPrimitive(0.0))
        Assertions.assertTrue(isPrimitive(0u))
        Assertions.assertTrue(isPrimitive(0L))
        Assertions.assertTrue(isPrimitive(0.toByte()))
        Assertions.assertTrue(isPrimitive(0.toShort()))
        Assertions.assertTrue(isPrimitive(false))

        // Random classes
        Assertions.assertFalse(isPrimitive(arrayOf<Any>()))
        Assertions.assertFalse(isPrimitive(UtilsTest()))
        Assertions.assertFalse(isPrimitive(Random))
    }

    @Test
    @Tag(TestTags.CLIENT)
    @DisplayName("Map.toFormRequestBody Test")
    fun toFormRequestBodyTest() {
        val mapWithPrimitives = mapOf(
            Pair("hi", 0),
            Pair("hi 2", 1)
        )

        Assertions.assertDoesNotThrow { mapWithPrimitives.toFormRequestBody() }

        val mapWithSomePrimitives = mapOf(
            Pair(listOf<String>(), 0),
            Pair("hi 2", 1)
        )

        Assertions.assertThrows(IllegalTypeException::class.java) { mapWithSomePrimitives.toFormRequestBody() }

        val mapWithNoPrimitives = mapOf(
            Pair(listOf<String>(), byteArrayOf()),
            Pair(listOf(), byteArrayOf())
        )

        Assertions.assertThrows(IllegalTypeException::class.java) { mapWithNoPrimitives.toFormRequestBody() }
    }

    @Test
    @DisplayName("String.staticXor test")
    fun staticXorTest() {
        val string = "Hi hello !!! This is a cool string which is very long......".repeat(3)
        val key = Random.nextInt()

        val encryptedString = string.staticXor(key)

        Assertions.assertNotEquals(string, encryptedString)
        Assertions.assertEquals(string, encryptedString.staticXor(key))
        Assertions.assertNotEquals(string, encryptedString.cyclicXor(key))
    }

    @Test
    @DisplayName("String.cyclicXor(Int) test")
    fun cyclicXorIntKeyTest() {
        val string = "Hi hello !!! This is a cool string which is very long...... (v2)".repeat(3)
        val key = Random.nextInt()

        val encryptedString = string.cyclicXor(key)

        Assertions.assertNotEquals(string, encryptedString)
        Assertions.assertEquals(string, encryptedString.cyclicXor(key))
        Assertions.assertNotEquals(string, encryptedString.staticXor(key))
    }

    @Test
    @DisplayName("String.cyclicXor(String) test")
    fun cyclicXorStringKeyTest() {
        val string = "Hi hello !!! This is a cool string which is very long...... (v2)".repeat(3)

        val baseKey = "Wow look at this amazing key I just made. I hope no one will steal it (please don't, I have a family to feed !)"
        val key = baseKey.drop(Random.nextInt(baseKey.length - 5))

        val encryptedString = string.cyclicXor(key)

        Assertions.assertNotEquals(string, encryptedString)
        Assertions.assertEquals(string, encryptedString.cyclicXor(key))
    }

    @Test
    @DisplayName("Cacher test")
    fun cacherTest() {
        val cacher = remember { 12 }
        var cachedValue by cacher

        Assertions.assertNull(cacher.cachedValue)
        Assertions.assertEquals(12, cachedValue /* Calls Cacher.getValue */)
        cachedValue = 20
        Assertions.assertEquals(20, cachedValue /* Calls Cacher.getValue */)

        cacher.invalidate()
        Assertions.assertNull(cacher.cachedValue)
    }
}