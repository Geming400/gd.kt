package fr.geming400.gddotkt.rawstring.property

import java.nio.charset.Charset
import kotlin.io.encoding.Base64

/**
 * A string property is used to store text
 */
class StringProperty(id: UInt, defaultValue: String? = "", currentValue: String? = defaultValue) : BaseProperty<String>(id, defaultValue, currentValue) {
    companion object {
        fun ofCharSequence(id: UInt, defaultValue: CharSequence? = "", currentValue: CharSequence? = defaultValue): StringProperty =
            StringProperty(id, defaultValue.toString(), currentValue.toString())
    }

    fun toRawString(charset: Charset): String =
        this.toRawStringHelper {
            Base64.UrlSafe.encode(it.toByteArray())
        }

    override fun toRawString(): String = this.toRawString(Charsets.UTF_8)
}