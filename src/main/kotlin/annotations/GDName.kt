package fr.geming400.gddotkt.annotations

/**
 * Represents the actual name of the property in geometry dash. Some properties don't have names (eg: obj id)
 * but some have, like in the `extra` tab.
 *
 * Spaces are allowed in the [name]
 * @property name the name in geometry dash
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@MustBeDocumented
annotation class GDName(
    val name: String
)
