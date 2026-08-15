import editor.objects.SimpleObject
import editor.objects.data.GridPos
import editor.objects.data.Hsv

// TODO: Once this is actually more closer to being finished delete this temporary main function
// TODO: Store sentitive infos in system properties
private fun main() {
    val obj = SimpleObject(1u, GridPos.gridCentered(0f, 0f))
    obj.baseColorHSV.value = Hsv.checkedBrightness()
    println("Raw string is: ${obj.asRawString()}")
}