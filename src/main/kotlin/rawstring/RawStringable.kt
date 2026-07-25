package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.exceptions.InvalidRawStringException

interface RawStringable {
    companion object {
        /**
         * Loosely check if a raw string is valid in its form
         * @sample samples.rawstring.isValidRawStringSample
         */
        fun isValidRawString(rawStr: String): Boolean =
            rawStr.count { it == ',' } % 2 == 1
    }

    /**
     * Get the geometry dash raw string representing this object
     * @return the raw string representing this object
     */
    fun asRawString(): String

    /**
     * Get the geometry dash raw string representing this object "strictly".
     * If the raw string happens to be invalid it throws an exception
     * @return the raw string representing this object
     * @throws InvalidRawStringException if the raw string is invalid
     * @see isValidRawString
     */
    fun asRawStringStrict(): String {
        val rawString = this.asRawString()
        if (isValidRawString(rawString))
            return rawString

        throw InvalidRawStringException(rawString)
    }
}