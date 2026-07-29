package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.objects.data.Pos
import fr.geming400.gddotkt.objects.data.Scale
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.UIntProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import kotlin.test.Test

@Tag("editor")
private class ObjectTests {
    @Test
    @DisplayName("GenericGdObject.get operator test")
    fun getOperatorTest() {
        val obj = SimpleObject(0u, 0f, 0f)

        Assertions.assertThrows(NoSuchElementException::class.java) { obj[9999.id] }
        Assertions.assertDoesNotThrow { obj[1.id] }
        Assertions.assertEquals(UIntProperty::class, obj[1.id]::class)
    }

    @Test
    @DisplayName("SimpleObject.pos test")
    fun posTest() {
        val obj = SimpleObject(0u, 0f, 0f)
        Assertions.assertEquals(0f, obj.x.value)
        Assertions.assertEquals(0f, obj.y.value)
        Assertions.assertEquals(Pos(0f, 0f), obj.pos)

        obj.pos = Pos(5f, 2f)
        Assertions.assertEquals(5f, obj.x.value)
        Assertions.assertEquals(2f, obj.y.value)
        Assertions.assertEquals(Pos(5f, 2f), obj.pos)

        obj.setPos(6f, 3f)
        Assertions.assertEquals(6f, obj.x.value)
        Assertions.assertEquals(3f, obj.y.value)
        Assertions.assertEquals(Pos(6f, 3f), obj.pos)
    }

    @Test
    @DisplayName("SimpleObject.scale test")
    fun scaleTest() {
        val obj = SimpleObject(0u, 0f, 0f)
        Assertions.assertEquals(1f, obj.scaleX.value)
        Assertions.assertEquals(1f, obj.scaleY.value)
        Assertions.assertEquals(Scale(1f, 1f), obj.scale)

        obj.scale = Scale(5f, 2f)
        Assertions.assertEquals(5f, obj.scaleX.value)
        Assertions.assertEquals(2f, obj.scaleY.value)
        Assertions.assertEquals(Scale(5f, 2f), obj.scale)

        obj.setScale(6f, 3f)
        Assertions.assertEquals(6f, obj.scaleX.value)
        Assertions.assertEquals(3f, obj.scaleY.value)
        Assertions.assertEquals(Scale(6f, 3f), obj.scale)
    }
}