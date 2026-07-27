package fr.geming400.gddotkt.rawstring.serializing

import fr.geming400.gddotkt.rawstring.RawStringable

interface Serializable<T> {
    fun serialize(value: T): String
}

interface Parsable<T> {
    fun parse(rawValue: String): T
}

interface Serializer<T> : Serializable<T>, Parsable<T> {
    companion object {
        fun <T> create(serializer: (T) -> String, parser: (String) -> T): Serializer<T> {
            return object : Serializer<T> {
                override fun serialize(value: T): String = serializer(value)

                override fun parse(rawValue: String): T = parser(rawValue)
            }
        }

        fun <T : RawStringable> fromRawstringable(parser: (String) -> T): Serializer<T> {
            return object : Serializer<T> {
                override fun serialize(value: T): String = value.asRawString()

                override fun parse(rawValue: String): T = parser(rawValue)
            }
        }
    }
}