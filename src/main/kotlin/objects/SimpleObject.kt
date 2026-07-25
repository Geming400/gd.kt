package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.rawstring.RawStringFactory
import fr.geming400.gddotkt.rawstring.property.BaseProperty

open class SimpleObject : GenericGdObject {
    protected val rawStringFactory: RawStringFactory = RawStringFactory(this)
    

    override fun asRawString(): String =
        this.rawStringFactory.asRawString()

    fun asRawStringMap(): Map<UInt, String> =
        this.rawStringFactory.asRawStringMap()

    fun asMap(): Map<UInt, BaseProperty<*>> =
        this.rawStringFactory.asMap()


    override fun get(propID: UInt): BaseProperty<*> =
        this.rawStringFactory.properties.first { it.id == propID }

    override fun <T> set(propID: UInt, value: T) {
        @Suppress("UNCHECKED_CAST")
        val prop = this[propID] as BaseProperty<T>
        prop.value = value
    }
}