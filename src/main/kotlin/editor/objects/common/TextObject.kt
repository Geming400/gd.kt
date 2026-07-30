package fr.geming400.gddotkt.editor.objects.common

import fr.geming400.gddotkt.editor.objects.ComplexObject
import fr.geming400.gddotkt.editor.objects.data.Pos
import fr.geming400.gddotkt.editor.objects.data.Position
import fr.geming400.gddotkt.editor.rawstring.id
import fr.geming400.gddotkt.editor.rawstring.property.IntProperty
import fr.geming400.gddotkt.editor.rawstring.property.StringProperty

class TextObject : ComplexObject {
    companion object {
        const val OBJ_ID = 914u
    }

    val text = StringProperty(31.id)
    val kerning = IntProperty(488.id)

    constructor(pos: Position) : super(OBJ_ID, pos)
    constructor(x: Float, y: Float) : super(OBJ_ID, x, y)

    constructor(pos: Position, text: String, kerning: Int = 0) : this(pos) {
        this.text.value = text
        this.text.value = text
        this.kerning.value = kerning
    }
    constructor(x: Float, y: Float, text: String, kerning: Int = 0) : this(Pos(x, y), text, kerning)
}