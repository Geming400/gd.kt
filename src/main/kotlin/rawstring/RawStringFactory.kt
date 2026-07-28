package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.exceptions.InvalidRawStringException
import fr.geming400.gddotkt.objects.GenericGdObject
import fr.geming400.gddotkt.rawstring.property.AbstractProperty
import fr.geming400.gddotkt.rawstring.property.PropertyDefinition
import java.util.Collections
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * A raw string factory allows you to abstract the generation of raw string for [GenericGdObjects][GenericGdObject].
 * Internally, using reflection it looks for [PropertyDefinitions][PropertyDefinition] and creates the raw string from there.
 */
class RawStringFactory {
    companion object {
        const val OBJECTS_SEPARATOR: Char = ';'

        /**
         * Joins multiple objects into a larger raw string understandable by geometry dash.
         * Each entry is separated by a semicolon.
         */
        fun joinRawStrings(objects: Collection<RawStringable>): String =
            objects.joinToString(OBJECTS_SEPARATOR.toString()) { it.asRawString() }

        /**
         * Joins multiple objects into a larger raw string understandable by geometry dash.
         * Each entry is separated by a semicolon.
         */
        fun joinRawStrings(vararg objects: RawStringable): String =
            joinRawStrings(objects = listOf(*objects))

        /**
         * Joins multiple objects' raw strings into a larger raw string understandable by geometry dash.
         * Each entry is separated by a semicolon.
         */
        @JvmName("joinRawStringsFromCharSequence")
        fun joinRawStrings(objects: Collection<CharSequence>): String =
            objects.joinToString(OBJECTS_SEPARATOR.toString())

        /**
         * Joins multiple objects' raw strings into a larger raw string understandable by geometry dash.
         * Each entry is separated by a semicolon.
         */
        @JvmName("joinRawStringsFromCharSequence")
        fun joinRawStrings(vararg objects: CharSequence): String =
            joinRawStrings(objects = listOf(*objects))

        /**
         * Turns a raw string into a [Map] in the format `propID: value`
         * @param rawString the raw string to turn into a map
         * @param separator the separator used to separate the raw string (ex, in `1,10,2,20,3,30` the separator is `,`)
         * @return the map output where keys are [ids][Id] and the values the prop's value
         * @throws InvalidRawStringException if the raw string is invalid *(see [GenericGdObject.isValidObjectString])*
         * @throws IllegalArgumentException if any of the parsed [ids][Id] are below `0` (exclusive, so `< 0`)
         */
        fun rawStringToMap(rawString: String, separator: Char = AbstractProperty.KEY_VAL_SEPARATOR): Map<Id, String> {
            return if (GenericGdObject.isValidObjectString(rawString, separator)) {
                val rawStrAsPairs = rawString.split(separator).chunked(2) {
                    AbstractProperty.rawStringToPairID(it.joinToString(separator.toString()))
                }.toTypedArray()

                mapOf(*rawStrAsPairs)
            } else {
                throw InvalidRawStringException(rawString)
            }
        }

        /**
         * Checks the equality of 2 possible raw strings. If any of the raw strings are malformed `false` is returned
         * @sample samples.rawstring.areRawStringEqualsSample
         */
        fun areRawStringEquals(a: String, b: String, separator: Char = AbstractProperty.KEY_VAL_SEPARATOR): Boolean {
            return try {
                rawStringToMap(a, separator) == rawStringToMap(b, separator)
            } catch (_: InvalidRawStringException) {
                false
            } catch (_: IllegalArgumentException) {
                false
            }
        }
    }

    private val parent: GenericGdObject
    private var cachedProperties: Collection<PropertyDefinition<*>>? = null

    constructor(parent: GenericGdObject) {
        this.parent = parent
    }

    /**
     * The properties of this factory's [parent].
     * They are cached per-instance and are only cached when this var's getter is called
     */
    val properties: Collection<PropertyDefinition<*>>
        get() {
            if (this.cachedProperties == null)
                this.computeProperties { props ->
                    this.cachedProperties = Collections.unmodifiableCollection(props)
                }

            return this.cachedProperties!!
        }

    private fun computeProperties(consumer: (List<PropertyDefinition<*>>) -> Unit) {
        val props = mutableListOf<PropertyDefinition<*>>()

        this.parent::class.memberProperties.forEach {
            if (it.visibility == KVisibility.PUBLIC && it.returnType.isSubtypeOf(PropertyDefinition::class.starProjectedType)) {
                val prop = it.getter.call(this.parent)
                if (prop != null)
                    props.add(prop as PropertyDefinition<*>)
            }
        }

        consumer(props.sortedBy { it.id })
    }

    /**
     * Get the raw string of this factory's [parent] by concatenating all
     * properties' raw strings
     * @return the raw string of this factory's [parent]
     * @see AbstractProperty.toRawString
     */
    fun asRawString(): String =
        this.getSerializableProperties().joinToString(AbstractProperty.KEY_VAL_SEPARATOR.toString()) {
            it.toRawString()
        }

    fun getSerializableProperties(): List<PropertyDefinition<*>> =
        this.properties
            .stream()
            .filter { it.isSerializable() }
            .toList()

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     */
    fun asMap(): Map<Id, PropertyDefinition<*>> {
        val res = mutableMapOf<Id, PropertyDefinition<*>>()
        this.getSerializableProperties().forEach {
            res[it.id] = it
        }

        return res
    }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [numerical ids][Id.numericalID] is `null`
     */
    fun asIntMap(): Map<UInt, PropertyDefinition<*>> =
        this.asMap().mapKeys { it.key.getNumericalIdStrict() }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [string ids][Id.stringID] is `null`
     */
    fun asStringMap(): Map<String, PropertyDefinition<*>> =
        this.asMap().mapKeys { it.key.getStringIdStrict() }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     */
    fun asRawStringMap(): Map<Id, String> =
        this.asMap().mapValues { it.value.toRawString() }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [numerical ids][Id.numericalID] is `null`
     */
    fun asRawStringIntMap(): Map<UInt, String> =
        this.asIntMap().mapValues { it.value.toRawString() }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [string ids][Id.stringID] is `null`
     */
    fun asRawStringStringMap(): Map<String, String> =
        this.asStringMap().mapValues { it.value.toRawString() }
}