package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.exceptions.InvalidRawStringException
import fr.geming400.gddotkt.rawstring.RawStringable
import fr.geming400.gddotkt.rawstring.property.AbstractProperty

/**
 * Represents an object that can have a conversion to a "geometry dash raw string"
 * and contains [properties][AbstractProperty]
 */
interface GenericGdObject : RawStringable {
    companion object {
        /**
         * Loosely check if a raw string is valid in its form
         * @sample samples.rawstring.isValidObjectStringSample
         */
        fun isValidObjectString(rawStr: String): Boolean =
            rawStr.count { it == ',' } % 2 == 1
    }

    /**
     * Get the property of this geometry dash object by its property id
     * @throws NullPointerException if there is no property at the given id
     */
    operator fun get(propID: UInt): AbstractProperty<*>

    /**
     * Get the geometry dash raw string representing this object "strictly".
     * If the raw string happens to be invalid it throws an exception
     * @return the raw string representing this object
     * @throws InvalidRawStringException if the raw string is invalid
     * @see isValidObjectString
     */
    fun asRawStringStrict(): String {
        val rawString = this.asRawString()
        if (rawString == "") {
            return rawString
        } else {
            if (isValidObjectString(rawString))
                return rawString

            throw InvalidRawStringException(rawString)
        }
    }

// Old function, removed because of type unsafety
// (you can more easily assign the wrong type to the value var, yes, you can assign the WRONG TYPE)
//    /**
//     * Sets the property of this geometry dash object by its property id
//     * @throws ClassCastException if [T] is the invalid type for the corresponding property
//     * @throws NullPointerException if there is no property at the given id
//     */
//    operator fun <T> set(propID: UInt, value: T)
}