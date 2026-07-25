package samples.rawstring.property

import fr.geming400.gddotkt.rawstring.property.IntProperty

private fun isSerializableSample() {
    val prop = IntProperty(1u, defaultValue = 5, currentValue = 5)

    // This returns false
    // The value is equal to the default value, so there's no need
    // to serialize it
    prop.isSerializable()

    prop.value = 7

    // This returns true
    // The value is NOT equal to the default value, so we need
    // to serialize it
    prop.isSerializable()
}