package editor.rawstring.property

import editor.rawstring.Id

/**
 * A duration property is like a [FloatProperty] except its allowed values are:
 * - In the range `[0, Float.MAX_VALUE]`
 * - `-1`
 */
class GDDurationProperty(id: Id, defaultValue: Float? = 0f, currentValue: Float? = null) : FloatProperty(id, defaultValue, currentValue) {
    override var value: Float?
        get() = super.value
        set(value) {
            when (value) {
                null -> { super.value = null }
                -1f -> { super.value = value }
                else -> { super.value = value.coerceAtLeast(0f) }
            }
        }

    // TODO: ava.time.Duration getters
}
