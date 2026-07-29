package fr.geming400.gddotkt.objects.data.enums

import fr.geming400.gddotkt.rawstring.property.GdEnum

/**
 * The "single color type" value changes if single color objects are treated as base or detail.
 * It is located in the gear icon at the top of the `Edit Object` tab
 */
enum class SingleColorType(override val value: Int) : GdEnum {
    BASE(1),
    DETAIL(2)
}