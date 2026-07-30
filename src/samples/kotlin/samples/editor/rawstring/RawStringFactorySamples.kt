package samples.editor.rawstring

import fr.geming400.gddotkt.editor.rawstring.RawStringFactory

private fun areRawStringEqualsSample() {
    val rawString1 = "1,10,2,20,3,30"
    val rawString2 = "3,30,1,10,2,20"
    val rawString3 = "1,10,2,20,3,30,4,40"

    // 'rawString1' and 'rawString2' are equal.
    // They are not the same string, but their object representation is the same
    // (same keys / pairs)
    RawStringFactory.areRawStringEquals(rawString1, rawString2) // Returns 'true'

    // However 'rawString1' and 'rawString3' are not equal !
    RawStringFactory.areRawStringEquals(rawString1, rawString3) // Returns 'false'
}