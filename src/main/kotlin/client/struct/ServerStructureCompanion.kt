package client.struct

import client.GDClient
import client.GDClientApi

/**
 * Represents a structure that can be returned by Robtop's server.
 * This always must be the children of a `companion object`
 */
@GDClientApi
interface ServerStructureCompanion<out T> {
    val separator: Char

    fun parse(rawString: String, client: GDClient): T
}