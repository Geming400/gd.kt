@file:JvmName("GdDotKTUtils")
package utils

import exceptions.IllegalTypeException
import okhttp3.FormBody
import okhttp3.RequestBody

object Utils {
    fun isPrimitive(value: Any) =
        value is String || value::class.javaPrimitiveType != null
}

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

// The 'Any' upper bound is to prevent null types
fun <K : Any, V : Any> Map<K, V>.toFormRequestBody(): RequestBody {
    val bodyBuilder = FormBody.Builder()
    this.forEach { (k, v) ->
        if (!Utils.isPrimitive(k))
            throw IllegalTypeException("Key '$k' type is not a primitive and so cannot get turned into a form key")

        if (!Utils.isPrimitive(v))
            throw IllegalTypeException("Value '$v' type is not a primitive and so cannot get turned into a form key")

        bodyBuilder.add(k.toString(), v.toString())
    }

    return bodyBuilder.build()
}

/**
 * This is not like kotlin's T0DO() function and is instead used for tests to prevent
 * show useless TODOs from showing up
 */
@Suppress("FunctionName")
internal fun LACKS_IMPL(): Nothing = throw NotImplementedError("This doesn't have any implementation. This is maybe because we are in a test environment and this has no reason to be implemented.")
