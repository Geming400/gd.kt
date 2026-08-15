package editor.rawstring.property

import editor.rawstring.Id
import editor.rawstring.serializing.Serializers
import utils.toInt
import org.intellij.lang.annotations.MagicConstant

class BoolProperty(id: Id, defaultValue: Boolean? = false, currentValue: Boolean? = null) : AbstractProperty<Boolean>(id, defaultValue, currentValue) {
    override val serializer = Serializers.BOOLEAN

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
        return this.getOrThrow().toInt()
    }
}