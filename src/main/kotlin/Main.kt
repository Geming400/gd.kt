package fr.geming400.gddotkt

import fr.geming400.gddotkt.editor.objects.SimpleObject
import fr.geming400.gddotkt.editor.objects.data.GridPos
import fr.geming400.gddotkt.editor.objects.data.Hsv

// TODO: Once this is actually more closer to being finished delete this temporary main function
// TODO: Store sentitive infos in system properties
private fun main() {
    val obj = SimpleObject(1u, GridPos.gridCentered(0f, 0f))
    obj.baseColorHSV.value = Hsv.checkedBrightness()
    println("Raw string is: ${obj.asRawString()}")
}