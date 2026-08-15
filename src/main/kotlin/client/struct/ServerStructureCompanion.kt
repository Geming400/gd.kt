package client.struct

/**
 * Represents a structure that can be returned by Robtop's server.
 * This always must be the children of a `companion object`
 */
interface ServerStructureCompanion<out T> {
    val separator: Char

    fun parse(rawString: String): T
}