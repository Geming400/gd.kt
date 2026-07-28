package fr.geming400.gddotkt.objects.triggers

import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.objects.propertycontainers.TriggerProperties
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.BoolProperty

class ToggleTrigger : TriggerObject {
    companion object {
        const val OBJ_ID = 1049u
    }

    val targetGroup = TriggerProperties.TARGET_GROUP
    val activateGroup = BoolProperty(56.id, false)

    constructor(pos: Position) : super(OBJ_ID, pos)
    constructor(x: Float, y: Float) : super(OBJ_ID, x, y)
}