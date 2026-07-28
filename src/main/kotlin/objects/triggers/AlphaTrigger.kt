package fr.geming400.gddotkt.objects.triggers

import fr.geming400.gddotkt.annotations.GDName
import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.objects.propertycontainers.TriggerProperties

class AlphaTrigger : TriggerObject {
    companion object {
        const val OBJ_ID = 1007u
    }

    val fadeTime = TriggerProperties.DURATION
    @GDName("Spawn Trigger")
    val targetGroup = TriggerProperties.TARGET_GROUP
    val opacity = TriggerProperties.OPACITY

    constructor(pos: Position) : super(OBJ_ID, pos)
    constructor(x: Float, y: Float) : super(OBJ_ID, x, y)
}