package fr.geming400.gddotkt.rawstring.property

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PropertyTests {
    fun <T> propTest(
        prop: BaseProperty<T>,
        testValue: T,
        doEqualityCheck: Boolean = true
    ) {
        if (doEqualityCheck) {
            Assertions.assertNotEquals(prop.value, prop.defaultValue)
            Assertions.assertNotEquals(prop.value, testValue)
            Assertions.assertNotEquals(testValue, prop.defaultValue)
        }

        Assertions.assertEquals(prop.getOrElse(testValue), prop.value)
        Assertions.assertEquals(prop.getOrNullableElse(null), prop.value)
        Assertions.assertDoesNotThrow { prop.getOrThrow() }
        Assertions.assertNotEquals(prop.value, prop.defaultValue)
        Assertions.assertFalse(prop.isDefaultValue())

        val defaultValTest = {
            Assertions.assertEquals(prop.value, prop.defaultValue)
            Assertions.assertEquals(prop.toRawString(), "")
            Assertions.assertTrue(prop.isDefaultValue())

            Assertions.assertEquals(prop.value, prop.defaultValue)
            Assertions.assertEquals(prop.getOrElse(testValue), prop.defaultValue)
            Assertions.assertEquals(prop.getOrNullableElse(null), prop.defaultValue)
            Assertions.assertDoesNotThrow { prop.getOrThrow() }
            Assertions.assertEquals(prop.toRawString(), "")
        }

        prop.resetValue()
        defaultValTest()

        // This is the same thing as 'prop.resetValue'
        prop.value = null
        defaultValTest()

        prop.value = testValue
        Assertions.assertEquals(prop.value, testValue)

        Assertions.assertEquals(prop.toRawString(), "${prop.id},$testValue")

    }

    @Test
    fun valueTest() {
        propTest(
            IntProperty(0u, 5, 0),
            7
        )
    }
}