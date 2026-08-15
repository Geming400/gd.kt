package fr.geming400.gddotkt.editor.objects

import fr.geming400.gddotkt.TestTags
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.EDITOR)
private class ObjectParserTests {
    @Test
    fun objectParserTest() {
        val obj = SimpleObject(2u, 0f, 0f)
        var parsedObj = ObjectParser.parse(obj.asRawString(), SimpleObject(0u, 0f, 0f))
        Assertions.assertEquals(obj, parsedObj)

        obj.linkedGroupID.value = 5
        parsedObj = ObjectParser.parse(SimpleObject(2u, 0f, 0f).asRawString(), SimpleObject(0u, 0f, 0f))
        Assertions.assertNotEquals(obj, parsedObj)
    }
}