package fr.geming400.gddotkt.rawstring.property

import org.intellij.lang.annotations.MagicConstant

class BoolProperty(id: UInt, defaultValue: Boolean? = null, currentValue: Boolean? = defaultValue) : BaseProperty<Boolean>(id, defaultValue, currentValue) {
    override fun toRawString(): String {
        val boolValue = when (this.value) {
            null -> null
            true -> "1"
            false -> "0"
        }

        return this.toRawStringHelper(boolValue)
    }

    /**
     * Turns this property's [value] into geometry dash's bool representation
     * @throws NullPointerException if the property's [value] is `null`
     */
    @MagicConstant(intValues = [0, 1])
    fun asGdBool(): Int {
        return if (this.getOrThrow())
            1
        else
            0
    }
}