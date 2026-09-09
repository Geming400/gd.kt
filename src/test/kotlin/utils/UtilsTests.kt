package utils

import TestTags
import exceptions.IllegalTypeException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

private class UtilsTest {
    @Test
    fun isPrimitiveTest() {
        Assertions.assertTrue(isPrimitive("0"))
        Assertions.assertTrue(isPrimitive(0))
        Assertions.assertTrue(isPrimitive(0.0f))
        Assertions.assertTrue(isPrimitive(0.0))
        Assertions.assertTrue(isPrimitive(0L))
        Assertions.assertTrue(isPrimitive(0.toByte()))
        Assertions.assertTrue(isPrimitive(0.toShort()))
        Assertions.assertTrue(isPrimitive(false))

        Assertions.assertFalse(isPrimitive(arrayOf<Any>()))
    }

    @Test
    @DisplayName("Map.toFormRequestBody Test")
    @Tag(TestTags.CLIENT)
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
}