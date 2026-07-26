package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.objects.GenericGdObject
import fr.geming400.gddotkt.rawstring.property.AbstractProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

class RawStringFactory {
    companion object {
        const val OBJECTS_SEPARATOR: Char = ';'

        /**
         * Joins multiple objects into a larger raw string understandable by geometry dash.
         * Each entry is separated by a semicolon.
         */
        fun joinObjectsIntoRawString(objects: Collection<RawStringable>): String =
            objects.joinToString(OBJECTS_SEPARATOR.toString()) { it.asRawString() }
    }

    private val parent: GenericGdObject
    lateinit var properties: Collection<AbstractProperty<*>>
        private set

    constructor(parent: GenericGdObject) {
        this.parent = parent
        this.computeProperties { props ->
            this.properties = props
        }
    }

    private fun computeProperties(consumer: (List<AbstractProperty<*>>) -> Unit) {
        val props = mutableListOf<AbstractProperty<*>>()

        this.parent::class.memberProperties.forEach {
            if (it.visibility == KVisibility.PUBLIC && it.returnType.isSubtypeOf(AbstractProperty::class.starProjectedType)) {
                val prop = it.getter.call(this.parent)
                if (prop != null)
                    props.add(prop as AbstractProperty<*>)
            }
        }

        consumer(props)
    }

    fun getSerializableProperties(): List<AbstractProperty<*>> =
        this.properties
            .stream()
            .filter { it.isSerializable() }
            .toList()

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     */
    fun asMap(): Map<Id, AbstractProperty<*>> {
        val res = mutableMapOf<Id, AbstractProperty<*>>()
        this.properties.forEach {
            res[it.id] = it
        }

        return res
    }

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [numerical ids][Id.numericalID] is `null`
     */
    fun asIntMap(): Map<UInt, AbstractProperty<*>> =
        this.asMap().mapKeys { it.key.getNumericalIdStrict() }

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [string ids][Id.stringID] is `null`
     */
    fun asStringMap(): Map<String, AbstractProperty<*>> =
        this.asMap().mapKeys { it.key.getStringIdStrict() }

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

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     */
    fun asRawStringMap(): Map<Id, String> =
        this.asMap().mapValues { it.value.toRawString() }

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [numerical ids][Id.numericalID] is `null`
     */
    fun asRawStringIntMap(): Map<UInt, String> =
        this.asIntMap().mapValues { it.value.toRawString() }

    /**
     * Gets all the properties of this factory's parent in a map
     * in the format `propID: propRawString`
     * @return the properties in a [Map]
     * @throws NullPointerException if **ANY** of the [string ids][Id.stringID] is `null`
     */
    fun asRawStringStringMap(): Map<String, String> =
        this.asStringMap().mapValues { it.value.toRawString() }
}