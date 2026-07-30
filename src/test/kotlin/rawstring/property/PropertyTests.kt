package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.CustomAssertions
import fr.geming400.gddotkt.TestTags
import fr.geming400.gddotkt.editor.objects.GenericGdObject
import fr.geming400.gddotkt.editor.objects.data.Hsv
import fr.geming400.gddotkt.editor.rawstring.property.EnumProperty
import fr.geming400.gddotkt.editor.rawstring.property.GdEnum
import fr.geming400.gddotkt.editor.rawstring.Id
import fr.geming400.gddotkt.editor.rawstring.RawStringFactory
import fr.geming400.gddotkt.editor.rawstring.id
import fr.geming400.gddotkt.editor.rawstring.property.AbstractProperty
import fr.geming400.gddotkt.editor.rawstring.property.BoolProperty
import fr.geming400.gddotkt.editor.rawstring.property.ConditionalProperty
import fr.geming400.gddotkt.editor.rawstring.property.GDDurationProperty
import fr.geming400.gddotkt.editor.rawstring.property.HsvProperty
import fr.geming400.gddotkt.editor.rawstring.property.IntProperty
import fr.geming400.gddotkt.editor.rawstring.property.ListProperty
import fr.geming400.gddotkt.editor.rawstring.property.MutableConditionalProperty
import fr.geming400.gddotkt.editor.rawstring.property.PropertyDefinition
import fr.geming400.gddotkt.editor.rawstring.property.SetProperty
import fr.geming400.gddotkt.editor.rawstring.property.StringProperty
import fr.geming400.gddotkt.editor.rawstring.serializing.Serializer
import fr.geming400.gddotkt.editor.rawstring.serializing.Serializers
import fr.geming400.gddotkt.utils.LACKS_IMPL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64

var shouldMutableConditionalPropBeSerializable = false

enum class MyCoolEnum(override val value: Int) : GdEnum {
    FIRST(0),
    SECOND(1),
    THIRD(2)
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

@Tag(TestTags.EDITOR)
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
        val prop = ListProperty(0.id, elemSerializer = Serializers.INT)
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

        val secondProp = ListProperty(0.id, defaultValue = null, elemSerializer = Serializers.INT)
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { secondProp[0] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { secondProp[0] = 5 }

        secondProp.add(2)
        Assertions.assertDoesNotThrow { secondProp[0] }
        Assertions.assertDoesNotThrow { secondProp[0] = 5 }
        Assertions.assertDoesNotThrow { secondProp.add(5) }
        Assertions.assertEquals(2, secondProp.size())
        Assertions.assertDoesNotThrow { secondProp.clear() }
        Assertions.assertTrue(secondProp.isEmpty())

        prop.clear()
        prop.add(0)
        prop.add(1)
        prop.add(2)
        prop.add(3)
        Assertions.assertEquals("${prop.id},0.1.2.3", prop.toRawString())
    }

    @Test
    fun testSetProp() {
        // Most of the tests are already done above

        val prop = SetProperty(0.id, elemSerializer = Serializers.INT)
        Assertions.assertEquals(0, prop.size())

        prop.add(5)
        Assertions.assertEquals(1, prop.size())

        prop.clear()
        Assertions.assertEquals(0, prop.size())

        val secondProp = SetProperty(0.id, defaultValue = null, elemSerializer = Serializers.INT)
        Assertions.assertDoesNotThrow { secondProp.add(5) }
        Assertions.assertDoesNotThrow { secondProp.size() }
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
            EnumProperty(
                0.id,
                Serializer.enum(MyCoolEnum.entries),
                defaultValue = MyCoolEnum.FIRST,
                currentValue = MyCoolEnum.SECOND
            ),
            testValue = MyCoolEnum.THIRD,
            testRawStringValue = MyCoolEnum.THIRD.value
        )
    }

    @Test
    fun testGDDurationProp() {
        val durationProp = GDDurationProperty(1.id)
        durationProp.value = -1f
        Assertions.assertEquals(-1f, durationProp.value)

        durationProp.value = 5f
        Assertions.assertEquals(5f, durationProp.value)

        durationProp.value = -2f // Should get clamped to 0f
        Assertions.assertEquals(0f, durationProp.value)
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