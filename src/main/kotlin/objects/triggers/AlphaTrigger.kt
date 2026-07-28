package fr.geming400.gddotkt.objects.triggers

import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.objects.propertycontainers.TriggerProperties
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.FloatProperty

class AlphaTrigger : TriggerObject {
    companion object {
        const val OBJ_ID = 1007u
    }

    val fadeTime = TriggerProperties.DURATION
    val targetGroup = TriggerProperties.TARGET_GROUP
    val opacity = FloatProperty.ranged(35.id, 0f..1f)

    constructor(pos: Position) : super(OBJ_ID, pos)
    constructor(x: Float, y: Float) : super(OBJ_ID, x, y)
}