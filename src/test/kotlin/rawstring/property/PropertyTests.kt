package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.CustomAssertions
import fr.geming400.gddotkt.objects.GenericGdObject
import fr.geming400.gddotkt.objects.data.Hsv
import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.RawStringFactory
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.serializing.Serializer
import fr.geming400.gddotkt.rawstring.serializing.Serializers
import fr.geming400.gddotkt.utils.LACKS_IMPL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64

var shouldMutableConditionalPropBeSerializable = false

enum class MyCoolEnum : GdEnum {
    FIRST {
        override fun getValue(): Int = 0
    },

    SECOND {
        override fun getValue(): Int = 1
    },

    THIRD {
        override fun getValue(): Int = 2
    }
}

class MyObj : GenericGdObject {
    var rawStringFactory: RawStringFactory = RawStringFactory(this)

    val normalProp = IntProperty(1.id, defaultValue = 0)

    val conditionalProp = ConditionalProperty(
        id = 2.id,
        dependantOn = this.normalProp,
        serializer = Serializers.BOOLEAN,
        predicate = { it.isSerializable() }
    ) {
        true
    }

    val mutableConditionalProp = MutableConditionalProperty(
        id = 3.id,
        defaultValue = true,
        dependantOn = this.normalProp,
        serializer = Serializers.BOOLEAN,
        predicate = { shouldMutableConditionalPropBeSerializable }
    )

    override fun get(propID: Id): PropertyDefinition<*> = LACKS_IMPL()

    override fun asRawString(): String =
        this.rawStringFactory.asRawString()
}

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

        Assertions.assertEquals(prop.value, prop.getOrElse(testValue))
        Assertions.assertEquals(prop.value, prop.getOrNullableElse(null))
        Assertions.assertDoesNotThrow { prop.getOrThrow() }
        Assertions.assertNotEquals(prop.value, prop.defaultValue)
        Assertions.assertFalse(prop.isDefaultValue())

        val defaultValTest = {
            Assertions.assertEquals(prop.value, prop.defaultValue)
            Assertions.assertEquals("", prop.toRawString())
            Assertions.assertTrue(prop.isDefaultValue())

            Assertions.assertEquals(prop.value, prop.defaultValue)
            Assertions.assertEquals(prop.defaultValue, prop.getOrElse(testValue))
            Assertions.assertEquals(prop.defaultValue, prop.getOrNullableElse(null))
            Assertions.assertDoesNotThrow { prop.getOrThrow() }
            Assertions.assertEquals("", prop.toRawString())
        }

        prop.resetValue()
        defaultValTest()

        // This is the same thing as 'prop.resetValue'
        prop.value = null
        defaultValTest()

        prop.value = testValue
        Assertions.assertEquals(testValue, prop.value)

        Assertions.assertEquals("${prop.id.getID()},$testRawStringValue", prop.toRawString())
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
        Assertions.assertEquals("", prop.toRawString())

        prop.value = true
        Assertions.assertEquals(1, prop.asGdBool())
        Assertions.assertEquals("${prop.id.getID()},1", prop.toRawString())

        prop.value = false
        Assertions.assertEquals(0, prop.asGdBool())
        Assertions.assertEquals("${prop.id.getID()},0", prop.toRawString())
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
        Assertions.assertEquals(0, prop.size())

        prop.add(5)
        Assertions.assertEquals(1, prop.size())
        Assertions.assertEquals(5, prop[0])
        Assertions.assertEquals(prop[0], prop.getOrThrow()[0])

        prop[0] = 4
        Assertions.assertEquals(1, prop.size())
        Assertions.assertEquals(4, prop[0])
        Assertions.assertEquals(prop[0], prop.getOrThrow()[0])

        prop.clear()
        Assertions.assertEquals(0, prop.size())
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
        Assertions.assertEquals(0, prop.size())

        prop.add(5)
        Assertions.assertEquals(1, prop.size())

        prop.clear()
        Assertions.assertEquals(0, prop.size())

        val secondProp = SetProperty<Int>(0.id, defaultValue = null)
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.add(5) }
        Assertions.assertThrows(NullPointerException::class.java) { secondProp.size() }
        Assertions.assertDoesNotThrow { secondProp.clear() }
        Assertions.assertDoesNotThrow { secondProp.isEmpty() }
    }

    @Test
    fun testHsvProp() {
        val prop = HsvProperty(1.id, defaultValue = null, currentValue = Hsv.create())
        Assertions.assertEquals(
            prop.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + prop.getOrThrow().asRawString(),
            prop.toRawString()
        )

        @Suppress("DEPRECATION")
        prop.setUsesColorProp(5.id)
        Assertions.assertEquals(
            prop.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + prop.getOrThrow().asRawString()
                    + AbstractProperty.KEY_VAL_SEPARATOR + "5,1",
            prop.toRawString()
        )
    }

    @Test
    fun testEnumProp() {
        propTest(
            EnumProperty(0.id, Serializer.createEnumSerializer(MyCoolEnum.entries), defaultValue = MyCoolEnum.FIRST, currentValue = MyCoolEnum.SECOND),
            testValue = MyCoolEnum.THIRD
        )
    }

    @Test
    @DisplayName("ConditionalProp test")
    fun testImmutableConditionalProp() {
        val obj = MyObj()
        Assertions.assertEquals("", obj.asRawString())
        Assertions.assertFalse(obj.normalProp.isSerializable())
        Assertions.assertTrue(obj.normalProp.isDefaultValue())
        Assertions.assertFalse(obj.conditionalProp.isSerializable())

        obj.normalProp.value = 5
        // Now, since the normalProp is serializable
        // the conditional property's predicate will return 'true'
        CustomAssertions.assertRawStringEquals("2,1,1,5", obj.asRawString())
        Assertions.assertTrue(obj.normalProp.isSerializable())
        Assertions.assertFalse(obj.normalProp.isDefaultValue())
        Assertions.assertTrue(obj.conditionalProp.isSerializable())
    }

    @Test
    @DisplayName("MutableConditionalProp test")
    fun testMutableConditionalPropTest() {
        val obj = MyObj()
        Assertions.assertEquals("", obj.asRawString())
        Assertions.assertFalse(obj.normalProp.isSerializable())
        Assertions.assertTrue(obj.normalProp.isDefaultValue())
        Assertions.assertFalse(obj.mutableConditionalProp.isSerializable())
        Assertions.assertTrue(obj.mutableConditionalProp.isDefaultValue())

        // We set mutableConditionalProp in it's "non default state"
        // but it is still non-serializable because of the 'shouldMutableConditionalPropBeSerializable' flag
        obj.mutableConditionalProp.value = !obj.mutableConditionalProp.defaultValue!!
        Assertions.assertEquals("", obj.asRawString())
        Assertions.assertFalse(obj.normalProp.isSerializable())
        Assertions.assertTrue(obj.normalProp.isDefaultValue())
        Assertions.assertFalse(obj.mutableConditionalProp.isSerializable())
        Assertions.assertFalse(obj.mutableConditionalProp.isDefaultValue())

        obj.normalProp.value = 5
        // 2,1 is from 'MyObj.conditionalProp'
        CustomAssertions.assertRawStringEquals("2,1,1,5", obj.asRawString())
        Assertions.assertTrue(obj.normalProp.isSerializable())
        Assertions.assertFalse(obj.normalProp.isDefaultValue())
        Assertions.assertFalse(obj.mutableConditionalProp.isSerializable())
        Assertions.assertFalse(obj.mutableConditionalProp.isDefaultValue())

        shouldMutableConditionalPropBeSerializable = true
        CustomAssertions.assertRawStringEquals("2,1,3,0,1,5", obj.asRawString())
        Assertions.assertTrue(obj.mutableConditionalProp.isSerializable())
        Assertions.assertFalse(obj.mutableConditionalProp.isDefaultValue())

        shouldMutableConditionalPropBeSerializable = false
        CustomAssertions.assertRawStringEquals("2,1,1,5", obj.asRawString())
        Assertions.assertFalse(obj.mutableConditionalProp.isSerializable())
        Assertions.assertFalse(obj.mutableConditionalProp.isDefaultValue())

        shouldMutableConditionalPropBeSerializable = true
        obj.normalProp.resetValue()
        Assertions.assertEquals("3,0", obj.asRawString())
        Assertions.assertTrue(obj.mutableConditionalProp.isSerializable())
        Assertions.assertFalse(obj.mutableConditionalProp.isDefaultValue())
    }
}