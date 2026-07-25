package fr.geming400.gddotkt.rawstring.property

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PropertyTest {
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

        Assertions.assertNotEquals(prop.value, prop.defaultValue)
        prop.resetValue()
        Assertions.assertEquals(prop.value, prop.defaultValue)

        prop.value = null
        Assertions.assertEquals(prop.value, prop.defaultValue)

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