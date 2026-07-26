package fr.geming400.gddotkt.rawstring

/**
 * Represents something that can have a conversion to a "geometry dash raw string"
 */
interface RawStringable {
    /**
     * Get the geometry dash raw string representing this object
     * @return the raw string representing this object
     */
    fun asRawString(): String
}