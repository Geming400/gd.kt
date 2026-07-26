package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id

typealias IntProperty = NumberProperty<Int>
typealias FloatProperty = NumberProperty<Float>

open class NumberProperty<T : Number>(id: Id, defaultValue: T? = null, currentValue: T? = defaultValue) : AbstractProperty<T>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper()
}

class UIntProperty(id: Id, defaultValue: UInt? = null, currentValue: UInt? = defaultValue) : AbstractProperty<UInt>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper()
}