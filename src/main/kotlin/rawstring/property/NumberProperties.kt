package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializers

sealed class NumberProperty<T>(id: Id, defaultValue: T? = null, currentValue: T? = defaultValue) : AbstractProperty<T>(id, defaultValue, currentValue)


class IntProperty(id: Id, defaultValue: Int? = 0, currentValue: Int? = defaultValue) : NumberProperty<Int>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.INT)
}

class FloatProperty(id: Id, defaultValue: Float? = 0f, currentValue: Float? = defaultValue) : NumberProperty<Float>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.FLOAT)
}

class UIntProperty(id: Id, defaultValue: UInt? = 0u, currentValue: UInt? = defaultValue) : NumberProperty<UInt>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.UINT)
}