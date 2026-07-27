package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.objects.data.Pos
import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.objects.data.Scale
import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.RawStringFactory
import fr.geming400.gddotkt.rawstring.id
import fr.geming400.gddotkt.rawstring.property.AbstractProperty
import fr.geming400.gddotkt.rawstring.property.BoolProperty
import fr.geming400.gddotkt.rawstring.property.IntProperty
import fr.geming400.gddotkt.rawstring.property.FloatProperty
import fr.geming400.gddotkt.rawstring.property.HsvProperty
import fr.geming400.gddotkt.rawstring.property.SetProperty
import fr.geming400.gddotkt.rawstring.property.UIntProperty

/**
 * A simple object is a generic geometry dash object in the editor.
 * This is the class that represents every other objects
 */
open class SimpleObject : GenericGdObject {
    protected val rawStringFactory: RawStringFactory

    val objID = UIntProperty(1.id)
    val x = FloatProperty(2.id)
    val y = FloatProperty(3.id)
    val rotation = FloatProperty(6.id, defaultValue = 0f)
    // This is now deprecated because in 2.2
    // the editor uses scaleX and scaleY
    // val scale = FloatProperty(32u, defaultValue = 1f)
    val scaleX = FloatProperty(128.id, defaultValue = 1f)
    val scaleY = FloatProperty(129.id, defaultValue = 1f)
    val flipHorizontal = BoolProperty(5.id, defaultValue = false)
    val flipVertical = BoolProperty(4.id, defaultValue = false)
    val warpXangle = FloatProperty(132.id)
    val warpYangle = FloatProperty(131.id)
    val baseColor = IntProperty(21.id)
    val detailColor = IntProperty(22.id)
    val baseColorHSV = HsvProperty(43.id).setUsesColorProp(41.id)
    val detailColorHSV = HsvProperty(44.id).setUsesColorProp(42.id)

    val groups = SetProperty<UInt>(57.id)
    val groupsParent = SetProperty<UInt>(274.id)
    val editorLayer = IntProperty(20.id, defaultValue = 0)
    val editorLayer2 = IntProperty(61.id, defaultValue = 0)
    val zLayer = IntProperty(24.id, defaultValue = 0)
    val zOrder = IntProperty(25.id, defaultValue = 0)
    val order = IntProperty(115.id, defaultValue = 0)
    val channel = IntProperty(170.id, defaultValue = 0)
    // TODO: Make a object class where the 'preview' property is a thing
    val linkedGroupID = IntProperty(108.id, defaultValue = 0)

    // val customProperties = PropertiesSet<ImplementableProperty<Any>>()

    inline var pos: Pos
        get() = Pos(this.x.value!!, this.y.value!!)
        set(value) {
            this.x.value = value.x
            this.y.value = value.y
        }

    inline var scale: Scale
        get() = Scale(this.scaleX.value!!, this.scaleY.value!!)
        set(value) {
            this.scaleX.value = value.width
            this.scaleY.value = value.height
        }

    constructor(objID: UInt, pos: Position) {
        this.objID.value = objID.coerceAtLeast(1u)
        this.setPos(pos)

        this.rawStringFactory = RawStringFactory(this)
    }

    constructor(objID: UInt, x: Float, y: Float) : this(objID, Pos(x, y))

    fun setScale(width: Float, height: Float) {
        this.scaleX.value = width
        this.scaleY.value = height
    }

    fun setPos(x: Float, y: Float) {
        this.x.value = x
        this.y.value = y
    }

    fun setPos(pos: Position) {
        this.x.value = pos.actualX
        this.y.value = pos.actualY
    }


    override fun asRawString(): String =
        this.rawStringFactory.asRawString()

    /**
     * For more information see [RawStringFactory.asRawStringIntMap]
     * @see RawStringFactory.asRawStringIntMap
     */
    fun asRawStringMap(): Map<UInt, String> =
        this.rawStringFactory.asRawStringIntMap()

    /**
     * For more information see [RawStringFactory.asIntMap]
     * @see RawStringFactory.asIntMap
     */
    fun asMap(): Map<UInt, PropertyDefinition<*>> =
        this.rawStringFactory.asIntMap()


    override fun get(propID: Id): PropertyDefinition<*> =
        this.rawStringFactory.properties.first { it.id == propID }

// See why this was removed in RawStringable
//    override fun <T> set(propID: UInt, value: T) {
//        @Suppress("UNCHECKED_CAST")
//        val prop = this[propID] as AbstractProperty<T>
//        prop.value = value
//    }
}