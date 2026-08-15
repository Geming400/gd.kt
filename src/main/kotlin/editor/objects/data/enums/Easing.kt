package editor.objects.data.enums

import editor.rawstring.property.GdEnum

/**
 * Enum class containing all of geometry dash's easings.
 * This does not contain however their formulas, to learn more about that go to [easings.net](https://easings.net/)
 */
enum class Easing(override val value: Int, val type: Type) : GdEnum {
    EASE_IN_OUT(1, Type.EASE),
    EASE_IN(2, Type.EASE),
    EASE_OUT(3, Type.EASE),

    ELASTIC_IN_OUT(4, Type.ELASTIC),
    ELASTIC_IN(5, Type.ELASTIC),
    ELASTIC_OUT(6, Type.ELASTIC),

    BOUNCE_IN_OUT(7, Type.BOUNCE),
    BOUNCE_IN(8, Type.BOUNCE),
    BOUNCE_OUT(9, Type.BOUNCE),

    EXPONENTIAL_IN_OUT(10, Type.EXPONENTIAL),
    EXPONENTIAL_IN(11, Type.EXPONENTIAL),
    EXPONENTIAL_OUT(12, Type.EXPONENTIAL),

    SINE_IN_OUT(13, Type.SINE),
    SINE_IN(14, Type.SINE),
    SINE_OUT(15, Type.SINE),

    BACK_IN_OUT(16, Type.BACK),
    BACK_IN(17, Type.BACK),
    BACK_OUT(18, Type.BACK);

    enum class Type(val hasEasingRate: Boolean = false) {
        EASE(true),
        ELASTIC(true),
        BOUNCE,
        EXPONENTIAL,
        SINE,
        BACK
    }
}
