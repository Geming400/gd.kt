package editor.rawstring.property

import editor.rawstring.Id
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

private typealias JavaDuration = java.time.Duration

/**
 * A duration property is like a [FloatProperty] except its allowed values are:
 * - In the range `[0, Float.MAX_VALUE]`
 * - `-1`
 */
class GDDurationProperty(id: Id, defaultValue: Float? = 0f, currentValue: Float? = null) : FloatProperty(id, defaultValue, clampDuration(currentValue)) {
    companion object {
        const val INFINITE_DURATION = -1f

        fun clampDuration(input: Float?): Float? {
            return if (input == null) {
                null
            } else if (input < 0 || input.isInfinite()) {
                INFINITE_DURATION
            } else {
                input.coerceAtLeast(0f)
            }
        }
    }

    /**
     * The current value of this property.
     *
     * The set value is automatically clamped in the range `[0, Float.MAX_VALUE]`, but if it
     * happens to be below `0` (exclusive), the value will get set to `-1` (aka infinite).
     *
     * If the value is [infinite][Float.isInfinite], then the value will also get set to `-1`
     */
    override var value: Float?
        get() = super.value
        set(value) {
            super.value = clampDuration(value)
        }

    /**
     * The [duration][Duration] of this property truncated to the **millisecond**
     */
    var duration: Duration?
        get() {
            return if (this.value == null)
                null
            else if (this.value?.isInfinite() == true)
                Duration.INFINITE
            else
                this.value!!.toDouble().seconds
        }
        set(value) {
            if (value == null)
                this.value = null
            else if (value.isInfinite())
                this.value = INFINITE_DURATION
            else
                this.value = value.inWholeMilliseconds / 1e3f
        }

    /**
     * The [**java** duration][java.time.Duration] of this property truncated to the **millisecond**
     */
    var javaDuration: JavaDuration?
        get() = this.duration?.toJavaDuration()
        set(value) {
            this.duration = value?.toKotlinDuration()
        }
}
