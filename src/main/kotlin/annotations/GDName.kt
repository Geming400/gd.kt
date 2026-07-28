package fr.geming400.gddotkt.annotations

/**
 * Represents the actual name of the property in geometry dash. Some properties don't have names (eg: obj id)
 * but some have, like in the `extra` tab.
 *
 * This is mostly useful when a property's name was renamed
 * in the code to make more sense, while in geometry dash
 * it might be more ambiguous.
 *
 * Spaces are allowed in the [name], this is intended behavior.
 * @property name the name in geometry dash
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@MustBeDocumented
annotation class GDName(
    val name: String
)
