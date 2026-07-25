package fr.geming400.gddotkt.rawstring.property

/**
 * The base class for all properties.
 * @property id the id of the property. It's unsigned because no properties have negative IDs.
 *              Also in geometry dash, no id goes below 1
 * @property defaultValue the default value if the property, this affects if it will be serialized or not
 * @property currentValue the value to set by default. It defaults to the property value
 */
abstract class BaseProperty<T>(val id: UInt, val defaultValue: T? = null, private var currentValue: T? = defaultValue) {
    companion object {
        const val KEY_VAL_SEPARATOR: Char = ','
    }

    /**
     * The current value of this property.
     *
     */
    open var value: T?
        get() {
            return if (this.currentValue == null)
                this.defaultValue
            else
                this.currentValue
        }
        set(value) { this.currentValue = value }

    fun getOrThrow(): T {
        if (this.value == null)
            throw NullPointerException("${this::class.simpleName}'s value is null when it was expected to be non null")

        return this.value!!
    }

    fun getOrElse(other: T): T {
        return if (this.value == null)
            other
        else
            this.value!!
    }

    fun getOrElse(other: T?): T? {
        return if (this.value == null)
            other
        else
            this.value!!
    }

    /**
     * Checks if this property's [value] is equal to its [default value][defaultValue]
     */
    fun isDefaultValue(): Boolean = this.defaultValue == this.value

    /**
     * Reset this property's [value] to its [default value][defaultValue]
     */
    fun resetValue() {
        this.currentValue = this.defaultValue
    }

    /**
     * Checks if this property needs to be serialized
     * @sample samples.rawstring.property.isSerializableSample
     */
    open fun isSerializable(): Boolean {
        // if there's no value, no need to serialize it into a raw string
        if (this.currentValue == null)
            return false

        // if the value is equal to the default value, no need to serialize it into a raw string
        return this.defaultValue != this.currentValue
    }

    /**
     * Helper to make raw strings
     * @param value the value of the property, possibly put in a string format if needed.
     *              This will always get converted into a [String]
     * @param suffix the suffix to append
     * @param suffixMode the mode deciding how to append the suffix
     * @return the raw string of this property in the format `id,value`.
     *         However, an empty string can be returned if [value] is `null` or if
     *         this property is [not serializable][isSerializable]
     */
    protected fun toRawStringHelper(value: Any? = this.value, suffix: String = "", suffixMode: SuffixMode = SuffixMode.WHEN_SERIALIZABLE): String {
        return if (this.isSerializable() && value != null) {
            this.id.toString() + KEY_VAL_SEPARATOR + value.toString() + suffixMode.getString(this, suffix)
        } else {
            ""
        }
    }

    /**
     * Turns this property into the raw string understandable by geometry dash.
     *
     * If you are implementing [BaseProperty], use [toRawStringHelper] to make a raw string
     * @return the raw string in the format `id,value`
     */
    abstract fun toRawString(): String

    override fun toString(): String {
        return "${this::class.qualifiedName!!}(id = ${this.id}, defaultValue = ${this.defaultValue}, value = ${this.value})"
    }
}

/**
 * The mode on how to apply the suffix in [BaseProperty.toRawStringHelper]
 */
enum class SuffixMode {
    /**
     * Always add the suffix
     */
    ALWAYS,

    /**
     * Only add the suffix when the property is [serializable][BaseProperty.isSerializable]
     * @see BaseProperty.isSerializable
     */
    WHEN_SERIALIZABLE;

    fun getString(prop: BaseProperty<*>, suffix: String): String {
        return if (this == ALWAYS)
            suffix
        else if (prop.isSerializable())
            suffix
        else
            ""
    }
}