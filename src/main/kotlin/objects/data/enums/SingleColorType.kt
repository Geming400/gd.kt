package fr.geming400.gddotkt.objects.data.enums

import fr.geming400.gddotkt.rawstring.property.GdEnum

/**
 * The "single color type" value changes if single color objects are treated as base or detail.
 * It is located in the gear icon at the top of the `Edit Object` tab
 */
enum class SingleColorType : GdEnum {
    BASE {
        override fun getValue(): Int = 1
    },
    DETAIL {
        override fun getValue(): Int = 2
    }
}