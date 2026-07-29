package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializer

/**
 * A constant property is **always** serializable and serializes to a **[constant value][constantValue]**
 */
class ConstantProperty<T>(id: Id, constantValue: T, override val serializer: Serializer<T>) : AbstractProperty<T>(id, constantValue, constantValue) {
    override fun isSerializable(): Boolean = true

    override fun toRawString(): String =
        this.toRawStringHelper(this.serializer)
}