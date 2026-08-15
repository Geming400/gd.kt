package editor.rawstring.property

import editor.rawstring.Id
import editor.rawstring.serializing.Serializer

class EnumProperty<T>(id: Id, override val serializer: Serializer<T>, defaultValue: T? = null, currentValue: T? = null) : AbstractProperty<T>(id, defaultValue, currentValue) where T : Enum<T>, T : GdEnum {
    override fun toRawString(): String =
        this.toRawStringHelper(this.serializer)
}

/**
 * Represents a geometry dash enum. This is used for serializing
 * @see Serializer.enum
 */
interface GdEnum {
    val value: Int
}
