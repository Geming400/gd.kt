package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.rawstring.RawStringable
import fr.geming400.gddotkt.rawstring.property.AbstractProperty

/**
 * Represents an object that can have a conversion to a "geometry dash raw string"
 * and contains [properties][AbstractProperty]
 */
interface GenericGdObject : RawStringable {
    /**
     * Get the property of this geometry dash object by its property id
     * @throws NullPointerException if there is no property at the given id
     */
    operator fun get(propID: UInt): AbstractProperty<*>

    /**
     * Sets the property of this geometry dash object by its property id
     * @throws ClassCastException if [T] is the invalid type for the corresponding property
     * @throws NullPointerException if there is no property at the given id
     */
    operator fun <T> set(propID: UInt, value: T)
}