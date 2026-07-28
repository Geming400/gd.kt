package fr.geming400.gddotkt.objects.triggers

import fr.geming400.gddotkt.objects.ComplexObject
import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.BoolProperty
import fr.geming400.gddotkt.rawstring.property.IndependentMutableConditionalProperty
import fr.geming400.gddotkt.rawstring.property.MutableConditionalProperty
import fr.geming400.gddotkt.rawstring.serializing.Serializers

/*
TODO: Triggers that needs to be made:
- [x] alpha trigger
- [x] toggle trigger
- move trigger
- [x] color trigger
 */

/**
 * A trigger object is the base class for every trigger.
 *
 * **If you are wondering what any of these properties mean, check the [GD Editor Guide](https://www.robtopgames.com/files/GDEditor.pdf) !**
 */
abstract class TriggerObject : ComplexObject {
    val spawnTriggered = BoolProperty(62.id, false)
    val touchTriggered = BoolProperty(11.id, false)
    val multiTriggered: IndependentMutableConditionalProperty<Boolean> =
        MutableConditionalProperty.createIndependent(87.id, defaultValue = false, serializer = Serializers.BOOLEAN) {
            this@TriggerObject.spawnTriggered.isSerializable() || this@TriggerObject.touchTriggered.isSerializable()
        }

    constructor(objID: UInt, pos: Position) : super(objID, pos)
    constructor(objID: UInt, x: Float, y: Float) : super(objID, x, y)
}