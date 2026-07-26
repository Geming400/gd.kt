package fr.geming400.gddotkt.utils

/**
 * Returns this boolean as an int.
 * It is `0` when `false` and `1` when `true`
 */
fun Boolean.toInt(): Int =
    if (this)
        1
    else
        0

/**
 * Turns this string into a boolean by converting it to an int.
 * This can be simplified to: `str -> str.toInt() -> num == 1`.
 *
 * This also checks if the string is equal to `true` or `false` beforehand
 * @return the string's boolean representation
 */
fun String.toBooleanFromInt(): Boolean {
    if (this.lowercase().toBooleanStrictOrNull() == null)
        return this.trim() == "1"

    return this.toBooleanStrict()
}

/**
 * Turns this string into a boolean by converting it to an int.
 * This can be simplified to: `str -> str.toInt() -> num == 1`.
 *
 * This also checks if the string is equal to `true` or `false` beforehand
 * @return the string's boolean representation
 * @throws IllegalArgumentException if the string is not equal to `0` or `1`
 */
fun String.toBooleanFromIntStrict(): Boolean {
    if (this.toBooleanStrictOrNull() == null)
        return when (this.lowercase().trim()) {
            "0" -> false
            "1" -> true
            else -> throw IllegalArgumentException("The string doesn't represent a boolean value: $this")
        }

    return this.toBooleanStrict()
}

/**
 * Turns this string into a boolean by converting it to an int.
 * This can be simplified to: `str -> str.toInt() -> num == 1`.
 *
 * This also checks if the string is equal to `true` or `false` beforehand
 * @return the string's boolean representation, or `null` if the string is not equal to `0` or `1`
 */
fun String.toBooleanFromIntStrictOrNull(): Boolean? {
    if (this.toBooleanStrictOrNull() == null)
        return when (this.lowercase().trim()) {
            "0" -> false
            "1" -> true
            else -> null
        }

    return this.toBooleanStrictOrNull()
}