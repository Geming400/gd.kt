package editor.rawstring.property

import editor.rawstring.Id
import editor.rawstring.serializing.Serializers
import java.nio.charset.Charset
import kotlin.io.encoding.Base64

/**
 * A string property is used to store text in base64 format. Geometry dash only understands text in this format
 * @see Base64.UrlSafe
 */
class StringProperty(id: Id, defaultValue: String? = "", currentValue: String? = null) : AbstractProperty<String>(id, defaultValue, currentValue) {
    override val serializer = Serializers.B64STRING

    companion object {
        fun ofCharSequence(id: Id, defaultValue: CharSequence? = "", currentValue: CharSequence? = null): StringProperty =
            StringProperty(id, defaultValue.toString(), currentValue.toString())
    }

    fun toRawString(separator: Char = KEY_VAL_SEPARATOR, charset: Charset): String =
        this.toRawStringHelper(separator) {
            Base64.UrlSafe.encode(it.toByteArray(charset))
        }

    override fun asRawString(separator: Char): String =
        this.toRawString(separator, Charset.defaultCharset())

    override fun asRawString(): String =
        this.toRawString(KEY_VAL_SEPARATOR, Charset.defaultCharset())
}