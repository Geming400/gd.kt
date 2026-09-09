package client.struct

import client.GDClient
import editor.objects.GenericGdObject

/**
 * Represents a structure that can be returned by Robtop's server.
 */
interface ServerStructure : GenericGdObject {
    val client: GDClient

    /**
     * Get the geometry dash raw string representing this object.
     * Generally on server structures **there's no need to use this**
     * @return the raw string representing this object
     */
    override fun asRawString(): String =
        super.asRawString()
}