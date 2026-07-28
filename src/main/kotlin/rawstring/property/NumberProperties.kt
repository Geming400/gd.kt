package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializers

sealed class NumberProperty<T>(id: Id, defaultValue: T? = null, currentValue: T? = null) : AbstractProperty<T>(id, defaultValue, currentValue)


class IntProperty(id: Id, defaultValue: Int? = 0, currentValue: Int? = null) : NumberProperty<Int>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.INT)
}

class FloatProperty(id: Id, defaultValue: Float? = 0f, currentValue: Float? = null) : NumberProperty<Float>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.FLOAT)
}

class UIntProperty(id: Id, defaultValue: UInt? = 0u, currentValue: UInt? = null) : NumberProperty<UInt>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.UINT)
}