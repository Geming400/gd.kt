package fr.geming400.gddotkt.editor.rawstring.property

import fr.geming400.gddotkt.editor.rawstring.Id
import fr.geming400.gddotkt.editor.rawstring.serializing.Serializer

class EnumProperty<T>(id: Id, override val serializer: Serializer<T>, defaultValue: T? = null, currentValue: T? = null) : AbstractProperty<T>(id, defaultValue, currentValue) where T : Enum<T>, T : GdEnum {
    override fun toRawString(): String =
        toRawStringHelper(this.serializer)
}

/**
 * Represents a geometry dash enum. This is used for serializing
 * @see Serializer.Companion.enum
 */
interface GdEnum {
    val value: Int
}
