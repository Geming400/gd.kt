package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.annotations.GDName
import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.BoolProperty
import fr.geming400.gddotkt.rawstring.property.IntProperty

/**
 * A complex object, as the name suggests is "more complex".
 * Complex objects store properties which are present inside the `extra` and `extra2` tabs in the
 * "Edit Object" tab.
 *
 * **If you are wondering what any of these properties mean, check the [GD Editor Guide](https://www.robtopgames.com/files/GDEditor.pdf) !**
 */
open class ComplexObject : SimpleObject {
    val dontFade = BoolProperty(64.id, defaultValue = false)
    val dontEnter = BoolProperty(64.id, defaultValue = false)
    val noEffects = BoolProperty(116.id, defaultValue = false)
    val groupParent = BoolProperty(34.id, defaultValue = false)
    val areaParent = BoolProperty(279.id, defaultValue = false)
    val dontBoostX = BoolProperty(496.id, defaultValue = false)
    val dontBoostY = BoolProperty(509.id, defaultValue = false)
    @GDName("singlePTouch")
    val singlePlayerTouch = BoolProperty(280.id, defaultValue = false)
    val highDetail = BoolProperty(103.id, defaultValue = false)
    val noTouch = BoolProperty(121.id, defaultValue = false)
    val passable = BoolProperty(134.id, defaultValue = false)
    val hide = BoolProperty(135.id, defaultValue = false)
    val nonStickX = BoolProperty(136.id, defaultValue = false)
    val nonStickY = BoolProperty(289.id, defaultValue = false)
    val extraSticky = BoolProperty(495.id, defaultValue = false)
    val extendedCollisions = BoolProperty(511.id, defaultValue = false)
    val centerEffect = BoolProperty(369.id, defaultValue = false)
    val iceBlock = BoolProperty(137.id, defaultValue = false)
    val gripSlop = BoolProperty(193.id, defaultValue = false)
    val noGlow = BoolProperty(96.id, defaultValue = false)
    val noParticle = BoolProperty(507.id, defaultValue = false)
    val scaleStick = BoolProperty(356.id, defaultValue = false)
    val noAudioScale = BoolProperty(372.id, defaultValue = false)
    val reverse = BoolProperty(117.id, defaultValue = false)

    val enterChannel = IntProperty(343.id)
    val material = IntProperty(446.id)
    val controlID = IntProperty(534.id)

    constructor(objID: UInt, pos: Position) : super(objID, pos)
    constructor(objID: UInt, x: Float, y: Float) : super(objID, x, y)
}