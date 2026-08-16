package client.struct

import editor.objects.ObjectParser
import editor.rawstring.RawStringFactory
import editor.rawstring.id
import editor.rawstring.property.IntProperty

class TestStructure : ServerStructure {
    companion object : ServerStructureCompanion<TestStructure> {
        override val separator: Char = '~'

        override fun parse(rawString: String): TestStructure =
            ObjectParser.parse<TestStructure>(rawString, separator = this.separator)
    }

    override val rawStringFactory: RawStringFactory = RawStringFactory.create(this)

    val levelID = IntProperty(1.id)
}