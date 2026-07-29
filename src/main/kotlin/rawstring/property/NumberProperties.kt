package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializers

sealed class NumberProperty<T>(id: Id, defaultValue: T? = null, currentValue: T? = null) : AbstractProperty<T>(id, defaultValue, currentValue)

sealed interface RangedProperty<T : Comparable<T>, R : ClosedRange<T>> {
    val range: R
}


open class IntProperty(id: Id, defaultValue: Int? = 0, currentValue: Int? = null) : NumberProperty<Int>(id, defaultValue, currentValue) {
    override val serializer = Serializers.INT

    companion object {
        fun ranged(id: Id, range: IntRange, defaultValue: Int? = 0, currentValue: Int? = null): RangedIntProperty =
            RangedIntProperty(id, range, defaultValue, currentValue)
    }

    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.INT)
}

open class UIntProperty(id: Id, defaultValue: UInt? = 0u, currentValue: UInt? = null) : NumberProperty<UInt>(id, defaultValue, currentValue) {
    override val serializer = Serializers.UINT

    companion object {
        fun ranged(id: Id, range: UIntRange, defaultValue: UInt? = 0u, currentValue: UInt? = null): RangedUIntProperty =
            RangedUIntProperty(id, range, defaultValue, currentValue)
    }

    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.UINT)
}

open class UByteProperty(id: Id, defaultValue: UByte? = 0u, currentValue: UByte? = null) : NumberProperty<UByte>(id, defaultValue, currentValue) {
    override val serializer = Serializers.UBYTE

    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.UBYTE)
}

open class FloatProperty(id: Id, defaultValue: Float? = 0f, currentValue: Float? = null) : NumberProperty<Float>(id, defaultValue, currentValue) {
    override val serializer = Serializers.FLOAT

    companion object {
        fun ranged(id: Id, range: ClosedFloatingPointRange<Float>, defaultValue: Float? = 0f, currentValue: Float? = null): RangedFloatProperty =
            RangedFloatProperty(id, range, defaultValue, currentValue)
    }

    override fun toRawString(): String =
        this.toRawStringHelper(Serializers.FLOAT)
}


class RangedIntProperty internal constructor(
    id: Id,
    override val range: IntRange,
    defaultValue: Int? = 0,
    currentValue: Int? = null
) : IntProperty(id, defaultValue, currentValue), RangedProperty<Int, IntRange> {
    override var value: Int?
        get() = super.value
        set(value) { super.value = value?.coerceIn(this.range) }
}

class RangedUIntProperty internal constructor(
    id: Id,
    override val range: UIntRange,
    defaultValue: UInt? = 0u,
    currentValue: UInt? = null
) : UIntProperty(id, defaultValue, currentValue), RangedProperty<UInt, UIntRange> {
    override var value: UInt?
        get() = super.value
        set(value) { super.value = value?.coerceIn(this.range) }
}

class RangedFloatProperty internal constructor(
    id: Id,
    override val range: ClosedFloatingPointRange<Float>,
    defaultValue: Float? = 0f,
    currentValue: Float? = null
) : FloatProperty(id, defaultValue, currentValue), RangedProperty<Float, ClosedFloatingPointRange<Float>> {
    override var value: Float?
        get() = super.value
        set(value) { super.value = value?.coerceIn(this.range) }
}
