package fr.geming400.gddotkt.rawstring.property

typealias IntProperty = NumberProperty<Int>
typealias FloatProperty = NumberProperty<Float>

open class NumberProperty<T : Number>(id: UInt, defaultValue: T? = null, currentValue: T? = defaultValue) : BaseProperty<T>(id, defaultValue, currentValue) {
    override fun toRawString(): String =
        this.toRawStringHelper()
}