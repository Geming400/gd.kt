package fr.geming400.gddotkt.rawstring

import fr.geming400.gddotkt.objects.GenericGdObject
import fr.geming400.gddotkt.rawstring.property.BaseProperty
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
    lateinit var properties: Collection<BaseProperty<*>>
        private set

    constructor(parent: GenericGdObject) {
        this.parent = parent
        this.computeProperties { props ->
            this.properties = props
        }
    }

    private fun computeProperties(consumer: (List<BaseProperty<*>>) -> Unit) {
        val props = mutableListOf<BaseProperty<*>>()

        this.parent::class.memberProperties.forEach {
            if (it.visibility == KVisibility.PUBLIC && it.returnType.isSubtypeOf(BaseProperty::class.starProjectedType)) {
                val prop = it.getter.call(this.parent)
                if (prop != null)
                    props.add(prop as BaseProperty<*>)
            }
        }

        consumer(props)
    }

    fun getSerializableProperties(): List<BaseProperty<*>> =
        this.properties
            .stream()
            .filter { it.isSerializable() }
            .toList()

    fun asMap(): Map<UInt, BaseProperty<*>> {
        val res = mutableMapOf<UInt, BaseProperty<*>>()
        this.properties.forEach {
            res[it.id] = it
        }

        return res
    }

    /**
     * Get the raw string of this factory's [parent] by concatenating all
     * properties' raw strings
     * @return the raw string of this factory's [parent]
     * @see BaseProperty.toRawString
     */
    fun asRawString(): String =
        this.getSerializableProperties().joinToString(BaseProperty.KEY_VAL_SEPARATOR.toString()) {
            it.toRawString()
        }

    fun asRawStringMap(): Map<UInt, String> {
        val res = mutableMapOf<UInt, String>()
        this.properties.forEach {
            res[it.id] = it.toRawString()
        }

        return res
    }
}