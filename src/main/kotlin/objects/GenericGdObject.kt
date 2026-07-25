package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.rawstring.RawStringable
import fr.geming400.gddotkt.rawstring.property.BaseProperty

interface GenericGdObject : RawStringable {
    /**
     * Get the property of this geometry dash object by its property id
     * @throws NullPointerException if there is no property at the given id
     */
    operator fun get(propID: UInt): BaseProperty<*>

    /**
     * Sets the property of this geometry dash object by its property id
     * @throws ClassCastException if [T] is the invalid type for the corresponding property
     * @throws NullPointerException if there is no property at the given id
     */
    operator fun <T> set(propID: UInt, value: T)
}