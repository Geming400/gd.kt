package samples.editor.rawstring

import editor.objects.GenericGdObject

private fun isValidObjectStringSample() {
    // This will return true
    // In its form it's a valid raw string
    GenericGdObject.isValidObjectString("1,1" + "2,2" + "3,3")

    // However this will return false
    // In its form it's NOT a valid raw string because it has an even
    // number of commas
    GenericGdObject.isValidObjectString("1,1" + "2,2" + "3,3" + "4")
}