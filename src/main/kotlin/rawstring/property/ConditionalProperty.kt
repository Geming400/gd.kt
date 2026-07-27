package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializer

/**
 * Dummy / sugar interface which is used to
 * tell conditional properties apart from regular properties
 * @see ConditionalProperty
 * @see MutableConditionalProperty
 */
interface IsConditional

/**
 * This conditional property is **read-only**.
 * Its value changes based on its [value from the value getter][valueGetter].
 *
 * The difference with a [**mutable** conditional property][MutableConditionalProperty] is that the [value]
 * of this property is immutable, meaning only the [valueGetter] is what decides to change the value.
 *
 * If you are looking for a "mutable conditional property" or its meaning, there is [MutableConditionalProperty]
 * @property id the id of the property. See the [Id] class on how to create an instance
 * @property dependantOn the [property][AbstractProperty] on which this conditional property is dependent on.
 *                       It cannot be another conditional property
 * @property serializer the [Serializer] used to serialize the obtained [value][valueGetter]
 * @property predicate this predicate decides if this conditional property should be enabled.
 *                     If it returns `false`, the [raw string output][toRawString] will be empty *(= `""`)*
 * @property valueGetter the lambda used to get the value of this conditional property.
 *                       It can only be obtained if the conditional property is "enabled" *(refer to [predicate])*
 * @param T the type of the conditional property
 * @param PT the type of the dependant property (ex: [Int])
 * @param P the class of the dependant property (ex: [IntProperty])
 * @see MutableConditionalProperty
 * @sample samples.rawstring.property.conditionalPropertySample
 */
open class ConditionalProperty<T, PT, P : AbstractProperty<PT>?>(
    override val id: Id,
    val dependantOn: P,
    val serializer: Serializer<T>,
    val predicate: (P) -> Boolean,
    val valueGetter: (P) -> T?
) : PropertyDefinition<T>, IsConditional {
    companion object {
        /**
         * Creates a [ConditionalProperty] dependant of no properties. It is only dependant of its [predicate]
         * @param id the id of the property. See the [Id] class on how to create an instance
         * @param serializer the [Serializer] used to serialize the obtained [value][valueGetter]
         * @param predicate this predicate decides if this conditional property should be enabled.
         *                  If it returns `false`, the [raw string output][toRawString] will be empty *(= `""`)*
         * @param valueGetter the lambda used to get the value of this conditional property.
         * @param T the type of the conditional property
         */
        fun <T> createIndependent(
            id: Id,
            serializer: Serializer<T>,
            predicate: () -> Boolean,
            valueGetter: () -> T?
        ): ConditionalProperty<T, Nothing, AbstractProperty<Nothing>?> =
            ConditionalProperty(id = id, dependantOn = null, serializer = serializer, predicate = { predicate() }, valueGetter = { valueGetter() })
    }

    final override val value: T?
        get() {
            return if (this.predicate(this.dependantOn))
                this.valueGetter(this.dependantOn)
            else
                null
        }

    /**
     * Checks if this property needs to be serialized.
     * This is dependent on the property's [predicate]
     * @sample samples.rawstring.property.isSerializableSample
     */
    override fun isSerializable(): Boolean = this.predicate(this.dependantOn)

    override fun toRawString(): String {
        val propValue = this.value
        return if (this.isSerializable() && propValue != null)
            this.id.getID() + AbstractProperty.KEY_VAL_SEPARATOR + this.serializer.serialize(propValue)
        else
            ""
    }
}

/**
 * This conditional property is **mutable**.
 * Its value changes based on its [dependant property][dependantOn].
 *
 * The difference with a [**immutable** conditional property][ConditionalProperty] is that the [value]
 * of this property is mutable, meaning anything can decide to change its value.
 *
 * If you are looking for a "read-only conditional property" or its meaning, there is [ConditionalProperty]
 * @property id the id of the property. See the [Id] class on how to create an instance
 * @property defaultValue the default value if the property, this affects if it will be serialized or not
 * @property currentValue the value to set by default. It defaults to the property value
 * @property dependantOn the [property][AbstractProperty] on which this conditional property is dependent on.
 *                       It cannot be another conditional property
 * @property serializer the [Serializer] used to serialize the obtained [value]
 * @property predicate this predicate decides if this conditional property should be enabled.
 *                     If it returns `false`, the [raw string output][toRawString] will be empty *(= `""`)*
 * @param T the type of the conditional property
 * @param PT the type of the dependant property (ex: [Int])
 * @param P the class of the dependant property (ex: [IntProperty])
 * @see ConditionalProperty
 * @sample samples.rawstring.property.mutableConditionalPropertySample
 */
open class MutableConditionalProperty<T, PT, P : AbstractProperty<PT>?>(
    id: Id,
    defaultValue: T? = null,
    currentValue: T? = defaultValue,
    val dependantOn: P,
    val serializer: Serializer<T>,
    val predicate: (P) -> Boolean
) : AbstractProperty<T>(id, defaultValue, currentValue), IsConditional {
    companion object {
        /**
         * Creates a [MutableConditionalProperty] dependant of no properties. It is only dependant of its [predicate]
         * @param id the id of the property. See the [Id] class on how to create an instance
         * @param defaultValue the default value if the property, this affects if it will be serialized or not
         * @param currentValue the value to set by default. It defaults to the property value
         * @param serializer the [Serializer] used to serialize the obtained [value]
         * @param predicate this predicate decides if this conditional property should be enabled.
         *                  If it returns `false`, the [raw string output][toRawString] will be empty *(= `""`)*
         * @param T the type of the conditional property
         */
        fun <T> createIndependent(
            id: Id,
            defaultValue: T? = null,
            currentValue: T? = defaultValue,
            serializer: Serializer<T>,
            predicate: () -> Boolean
        ): MutableConditionalProperty<T, Nothing, AbstractProperty<Nothing>?> =
            MutableConditionalProperty(id = id, defaultValue = defaultValue, currentValue = currentValue, dependantOn = null, serializer = serializer, predicate = { predicate() })
    }

    /**
     * The current value of this property.
     * The actual value is stored internally and is not meant to be accessed.
     *
     * If this property's [predicate] returns `false` then this
     * property's [defaultValue] will be returned instead.
     *
     * If set to `null`, the [defaultValue] will be used instead *(see [resetValue])*
     */
    final override var value: T?
        get() = super.value
        set(value) { super.value = value }

    /**
     * Checks if this property needs to be serialized.
     * This is dependent on the property's [predicate]
     * @sample samples.rawstring.property.isSerializableSample
     */
    override fun isSerializable(): Boolean =
        this.predicate(this.dependantOn) && super.isSerializable()

    override fun toRawString(): String {
        val propValue = this.value
        return if (this.isSerializable() && propValue != null)
            this.id.getID() + KEY_VAL_SEPARATOR + this.serializer.serialize(propValue)
        else
            ""
    }
}
