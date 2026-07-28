package fr.geming400.gddotkt.objects.propertycontainers

import fr.geming400.gddotkt.objects.data.enums.Easing
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.EnumProperty
import fr.geming400.gddotkt.rawstring.property.FloatProperty
import fr.geming400.gddotkt.rawstring.property.GDDurationProperty
import fr.geming400.gddotkt.rawstring.property.HsvProperty
import fr.geming400.gddotkt.rawstring.property.MutableConditionalProperty
import fr.geming400.gddotkt.rawstring.property.RangedFloatProperty
import fr.geming400.gddotkt.rawstring.property.UIntProperty
import fr.geming400.gddotkt.rawstring.serializing.Serializer

object TriggerProperties {
    val EASING_RATE_RANGE = 0.1f..20f
    const val MAX_GROUPS = 9999u


    inline val DURATION: GDDurationProperty
        get() = GDDurationProperty(10.id)

    inline val EASING: EnumProperty<Easing>
        get() = EnumProperty(30.id, Serializer.enum(Easing.entries))

    inline val TARGET_GROUP: UIntProperty
        get() = UIntProperty.ranged(51.id, 1u..MAX_GROUPS)

    inline val OPACITY: RangedFloatProperty
        get() = FloatProperty.ranged(35.id, 0f..1f)

    inline val HSV: HsvProperty
        get() = HsvProperty(49.id)

    fun getEasingRateProp(easingProp: EnumProperty<Easing>) =
        MutableConditionalProperty(85.id, 0f, dependantOn = easingProp, valueChanger = { it.coerceIn(EASING_RATE_RANGE) }, serializer = Serializer.clampedFloat(EASING_RATE_RANGE)) {
            it.value?.type?.hasEasingRate ?: false
        }
}