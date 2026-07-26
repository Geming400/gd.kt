package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.objects.data.Hsv
import fr.geming400.gddotkt.rawstring.id
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64

class PropertyTests {
    fun <T> propTest(
        prop: AbstractProperty<T>,
        testValue: T,
        testRawStringValue: Any? = testValue,
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

        Assertions.assertEquals(prop.toRawString(), "${prop.id.getID()},$testRawStringValue")
    }

    @Test
    fun testIntProp() {
        propTest(
            IntProperty(1.id, 5, 0),
            testValue = 7
        )
    }

    @Test
    fun testBoolProp() {
        val prop = BoolProperty(1.id, null)
        Assertions.assertEquals(prop.toRawString(), "")

        prop.value = true
        Assertions.assertEquals(prop.asGdBool(), 1)
        Assertions.assertEquals(prop.toRawString(), "${prop.id.getID()},1")

        prop.value = false
        Assertions.assertEquals(prop.asGdBool(), 0)
        Assertions.assertEquals(prop.toRawString(), "${prop.id.getID()},0")
    }

    @Test
    fun testStringProp() {
        propTest(
            StringProperty(0.id, defaultValue = "", currentValue = "Hii !!"),
            testValue = "Bye :(",
            testRawStringValue = Base64.UrlSafe.encode("Bye :(".toByteArray())
        )
    }

    @Test
    fun testListProp() {
        val prop = ListProperty<Int>(0.id)
        Assertions.assertEquals(prop.size(), 0)

        prop.add(5)
        Assertions.assertEquals(prop.size(), 1)
        Assertions.assertEquals(prop[0], 5)
        Assertions.assertEquals(prop[0], prop.getOrThrow()[0])

        prop[0] = 4
        Assertions.assertEquals(prop.size(), 1)
        Assertions.assertEquals(prop[0], 4)
        Assertions.assertEquals(prop[0], prop.getOrThrow()[0])

        prop.clear()
        Assertions.assertEquals(prop.size(), 0)
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { prop[0] }

        val secondProp = ListProperty<Int>(0.id, defaultValue = null)
        Assertions.assertThrows(NullPointerException::class.java) { secondProp[0] }
        Assertions.assertThrows(NullPointerException::class.java) { secondProp[0] = 5 }
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.add(5) }
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.size() }
        Assertions.assertDoesNotThrow { secondProp.clear() }
        Assertions.assertDoesNotThrow { secondProp.isEmpty() }
    }

    @Test
    fun testSetProp() {
        // Most of the tests are already done above

        val prop = SetProperty<Int>(0.id)
        Assertions.assertEquals(prop.size(), 0)

        prop.add(5)
        Assertions.assertEquals(prop.size(), 1)

        prop.clear()
        Assertions.assertEquals(prop.size(), 0)

        val secondProp = SetProperty<Int>(0.id, defaultValue = null)
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.add(5) }
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.size() }
        Assertions.assertDoesNotThrow { secondProp.clear() }
        Assertions.assertDoesNotThrow { secondProp.isEmpty() }
    }

    @Test
    fun hsvTest() {
        val prop = HsvProperty(1.id, defaultValue = null, currentValue = Hsv.create())
        Assertions.assertEquals(
            prop.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + prop.getOrThrow().asRawString(),
            prop.toRawString()
        )

        prop.setUsesColorProp(5.id)
        Assertions.assertEquals(
            prop.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + prop.getOrThrow().asRawString()
                    + AbstractProperty.KEY_VAL_SEPARATOR + "5,1",
            prop.toRawString()
        )
    }
}