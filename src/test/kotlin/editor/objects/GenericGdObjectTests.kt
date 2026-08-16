package editor.objects

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.EDITOR)
private class GenericGdObjectTests {
    @Test
    fun isValidObjectStringTest() {
        Assertions.assertTrue(GenericGdObject.isValidObjectString(SimpleObject(2u, 5f, 5f).asRawString()))
        Assertions.assertTrue(GenericGdObject.isValidObjectString("1~1~2~2~3~3", separator = '~'))

        Assertions.assertFalse(GenericGdObject.isValidObjectString(SimpleObject(2u, 5f, 5f).asRawString() + ",1"))
        Assertions.assertFalse(GenericGdObject.isValidObjectString("1~1~2~2~3~3~4", separator = '~'))
    }
}