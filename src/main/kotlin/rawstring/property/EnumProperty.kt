package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializable

class EnumProperty<T>(id: Id, val serializer: Serializable<T>, defaultValue: T? = null, currentValue: T? = null) : AbstractProperty<T>(id, defaultValue, currentValue) where T : Enum<T>, T : GdEnum {
    override fun toRawString(): String =
        toRawStringHelper(this.serializer)
}

/**
 * Represents a geometry dash enum. This is used for serializing
 * @see fr.geming400.gddotkt.rawstring.serializing.Serializer.enum
 */
interface GdEnum {
    /**
     * Gets the value of this enum entry
     */
    fun getValue(): Int
}
