package editor.rawstring.property

import editor.rawstring.Id
import editor.rawstring.serializing.Serializer

/**
 * A constant property is **always** serializable and serializes to a **[constant value][constantValue]**
 */
class ConstantProperty<T>(override val id: Id, val constantValue: T, override val serializer: Serializer<T>) :
    PropertyDefinition<T> {
    override val value: T?
        get() = this.constantValue

    override fun isSerializable(): Boolean = true

    override fun toRawString(): String =
        this.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + this.serializer.serialize(this.constantValue)
}