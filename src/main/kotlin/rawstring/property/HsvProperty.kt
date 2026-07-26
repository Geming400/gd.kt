package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.objects.data.Hsv
import fr.geming400.gddotkt.rawstring.Id

class HsvProperty(id: Id, defaultValue: Hsv? = Hsv.create(), currentValue: Hsv? = defaultValue) : AbstractProperty<Hsv>(id, defaultValue, currentValue) {
    private var usesColorPropID: Id? = null

    // TODO: test this + KDoc
    fun setUsesColorProp(propID: Id): HsvProperty {
        this.usesColorPropID = propID
        return this
    }

    override fun toRawString(): String {
        val suffix =
            if (this.usesColorPropID == null)
                ""
            else
                KEY_VAL_SEPARATOR + this.usesColorPropID!!.getID() + KEY_VAL_SEPARATOR + "1"

        return this.toRawStringHelper(suffix = suffix) {
            it.asRawString()
        }
    }
}