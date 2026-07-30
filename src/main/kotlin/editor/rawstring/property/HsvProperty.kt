package fr.geming400.gddotkt.editor.rawstring.property

import fr.geming400.gddotkt.editor.objects.data.Hsv
import fr.geming400.gddotkt.editor.rawstring.Id
import fr.geming400.gddotkt.editor.rawstring.serializing.Serializers

class HsvProperty(id: Id, defaultValue: Hsv? = Hsv.create(), currentValue: Hsv? = null) : AbstractProperty<Hsv>(id, defaultValue, currentValue) {
    override val serializer = Serializers.HSV

    private var usesColorPropID: Id? = null

    /**
     * Sets the id of the property `uses[Base/Detail]ColorHSV` to be included in the raw string output
     * @param propID the property id of the `uses[Base/Detail]ColorHSV` property
     * @return the hsv property object, aka `this`
     */
    @Deprecated(message = "This is kept intact but conditional properties should be used in favor")
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

        return this.toRawStringHelper(Serializers.HSV, suffix = suffix)
    }
}