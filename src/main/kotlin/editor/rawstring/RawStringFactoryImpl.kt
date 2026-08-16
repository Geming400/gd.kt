package editor.rawstring

import editor.objects.GenericGdObject
import editor.rawstring.property.AbstractProperty
import editor.rawstring.property.PropertyDefinition
import java.util.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

internal class RawStringFactoryImpl : RawStringFactory {
    private val parent: GenericGdObject
    private var cachedProperties: Collection<PropertyDefinition<*>>? = null

    constructor(parent: GenericGdObject) {
        this.parent = parent
    }

    override val properties: Collection<PropertyDefinition<*>>
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
     * @see PropertyDefinition.toRawString
     */
    override fun asRawString(): String =
        this.getSerializableProperties().joinToString(AbstractProperty.KEY_VAL_SEPARATOR.toString()) {
            it.toRawString()
        }

    /**
     * Gets all the **serializable** properties of this factory's parent in a map
     * in the format `propID: prop`
     * @return the properties in a [Map]
     */
    override fun asMap(): Map<Id, PropertyDefinition<*>> {
        val res = mutableMapOf<Id, PropertyDefinition<*>>()
        this.getSerializableProperties().forEach {
            res[it.id] = it
        }

        return res
    }
}