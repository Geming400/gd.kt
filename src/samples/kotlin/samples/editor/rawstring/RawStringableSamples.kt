package samples.editor.rawstring

import editor.objects.GenericGdObject
import editor.rawstring.property.AbstractProperty

private fun isValidObjectStringSample() {
    // This will return true
    // In its form it's a valid raw string
    val firstRawStr = listOf("1,1", "2,2", "3,3")
        .joinToString(AbstractProperty.KEY_VAL_SEPARATOR.toString())
    GenericGdObject.isValidObjectString(firstRawStr)

    // However this will return false
    // In its form it's NOT a valid raw string because it has an even
    // number of commas
    val secondRawStr = listOf("1,1", "2,2", "3,3", "4")
        .joinToString(AbstractProperty.KEY_VAL_SEPARATOR.toString())
    GenericGdObject.isValidObjectString(secondRawStr)
}