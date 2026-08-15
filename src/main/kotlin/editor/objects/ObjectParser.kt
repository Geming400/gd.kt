package editor.objects

import editor.rawstring.Id
import editor.rawstring.RawStringFactory
import editor.rawstring.property.AbstractProperty
import editor.rawstring.property.MutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

object ObjectParser {
    /**
     * Parses any raw string into [T]. Every [mutable properties][MutableProperty] of this object
     * are filled accordingly to the raw string
     * @param rawString the raw string to parse
     * @param toFill the instance to fill
     * @param separator the separator used to separate the raw string (ex, in `1,10,2,20,3,30` the separator is `,`)
     * @return returns the final instance, aka the same as [toFill]
     * @throws exceptions.InvalidRawStringException if the raw string is invalid *(see [GenericGdObject.isValidObjectString])*
     * @throws IllegalArgumentException if any of the parsed [ids][Id] are below `0` (exclusive, so `< 0`)
     */
    inline fun <reified T : Any> parse(rawString: String, toFill: T = T::class.createInstance(), separator: Char = AbstractProperty.KEY_VAL_SEPARATOR): T {
        val rawStringMap = RawStringFactory.rawStringToMap(rawString, separator)
        T::class.memberProperties.forEach {
            if (it.visibility == KVisibility.PUBLIC && it.returnType.isSubtypeOf(MutableProperty::class.starProjectedType)) {
                @Suppress("UNCHECKED_CAST")
                val prop = it.get(toFill) as MutableProperty<Any?>

                if (rawStringMap.containsKey(prop.id)) {
                    val value = prop.serializer.parse(rawStringMap[prop.id]!!)
                    if (value != null)
                        prop.value = value
                }
            }
        }

        return toFill
    }
}