package fr.geming400.gddotkt.rawstring.property

import fr.geming400.gddotkt.rawstring.Id
import fr.geming400.gddotkt.rawstring.serializing.Serializers
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

    fun toRawString(charset: Charset): String =
        this.toRawStringHelper {
            Base64.UrlSafe.encode(it.toByteArray(charset))
        }

    override fun toRawString(): String =
        this.toRawString(Charset.defaultCharset())
}