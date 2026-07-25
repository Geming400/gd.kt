package fr.geming400.gddotkt

import fr.geming400.gddotkt.objects.SimpleObject
import fr.geming400.gddotkt.objects.data.GridPos

fun main() {
    val obj = SimpleObject(1u, GridPos.gridCentered(0f, 0f))
    println("Raw string is: ${obj.asRawString()}")
}