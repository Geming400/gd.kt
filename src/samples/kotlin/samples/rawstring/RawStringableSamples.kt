package samples.rawstring

import fr.geming400.gddotkt.rawstring.RawStringable

private fun isValidRawStringSample() {
    // This will return true
    // In its form it's a valid raw string
    RawStringable.isValidRawString("1,1" + "2,2" + "3,3")

    // However this will return false
    // In its form it's NOT a valid raw string because it has an even
    // number of commas
    RawStringable.isValidRawString("1,1" + "2,2" + "3,3" + "4")
}