package fr.geming400.gddotkt.objects

import fr.geming400.gddotkt.objects.data.Pos
import fr.geming400.gddotkt.objects.data.Position
import fr.geming400.gddotkt.objects.data.Scale
import fr.geming400.gddotkt.rawstring.RawStringFactory
import fr.geming400.gddotkt.rawstring.property.BaseProperty
import fr.geming400.gddotkt.rawstring.property.BoolProperty
import fr.geming400.gddotkt.rawstring.property.IntProperty
import fr.geming400.gddotkt.rawstring.property.FloatProperty
import fr.geming400.gddotkt.rawstring.property.SetProperty
import fr.geming400.gddotkt.rawstring.property.UIntProperty

open class SimpleObject : GenericGdObject {
    protected val rawStringFactory: RawStringFactory

    val objID = UIntProperty(1u)
    val x = FloatProperty(2u)
    val y = FloatProperty(3u)
    val rotation = FloatProperty(6u, defaultValue = 0f)
    // This is now deprecated because in 2.2
    // the editor uses scaleX and scaleY
    // val scale = FloatProperty(32u, defaultValue = 1f)
    val scaleX = FloatProperty(128u, defaultValue = 1f)
    val scaleY = FloatProperty(129u, defaultValue = 1f)
    val flipHorizontal = BoolProperty(5u, defaultValue = false)
    val flipVertical = BoolProperty(4u, defaultValue = false)
    val warpXangle = FloatProperty(132u)
    val warpYangle = FloatProperty(131u)
    val baseColor = IntProperty(21u)
    val detailColor = IntProperty(22u)
//    val baseColorHSV = HsvProperty(43u)
//    val detailColorHSV = HsvProperty(44u)

    // val usesBaseColorHSV = ConditionalProperty(41u, baseColorHSV, ...)
    // val usesDetailColorHSV = ConditionalProperty(42u, detailColorHSV, ...)

    val groups = SetProperty<UInt>(57u)
    val groupsParent = SetProperty<UInt>(274u)
    val singleGroup = IntProperty(33u, defaultValue = 0)
    val editorLayer = IntProperty(20u, defaultValue = 0)
    val editorLayer2 = IntProperty(61u, defaultValue = 0)
    val zLayer = IntProperty(24u, defaultValue = 0)
    val zOrder = IntProperty(25u, defaultValue = 0)
    val order = IntProperty(115u, defaultValue = 0)
    val channel = IntProperty(170u, defaultValue = 0)
    // TODO: Make a object class where the 'preview' property is a thing
    val linkedGroupID = IntProperty(108u, defaultValue = 0)

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
     * For more information see [RawStringFactory.asRawStringMap]
     * @see RawStringFactory.asRawStringMap
     */
    fun asRawStringMap(): Map<UInt, String> =
        this.rawStringFactory.asRawStringMap()

    /**
     * For more information see [RawStringFactory.asMap]
     * @see RawStringFactory.asMap
     */
    fun asMap(): Map<UInt, BaseProperty<*>> =
        this.rawStringFactory.asMap()


    override fun get(propID: UInt): BaseProperty<*> =
        this.rawStringFactory.properties.first { it.id == propID }

    override fun <T> set(propID: UInt, value: T) {
        @Suppress("UNCHECKED_CAST")
        val prop = this[propID] as BaseProperty<T>
        prop.value = value
    }
}