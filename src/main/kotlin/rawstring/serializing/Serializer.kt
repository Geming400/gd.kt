package fr.geming400.gddotkt.rawstring.serializing

import fr.geming400.gddotkt.rawstring.RawStringable
import fr.geming400.gddotkt.rawstring.property.GdEnum
import kotlin.enums.EnumEntries

@FunctionalInterface
interface Serializable<T> {
    companion object {
        /**
         * @see Any.toString
         */
        fun <T> usingToString(): Serializable<T> =
            object : Serializable<T> {
                override fun serialize(value: T): String = value.toString()
            }
    }

    fun serialize(value: T): String
}

@FunctionalInterface
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

        fun clampedInt(range: IntRange): Serializer<Int> =
            create(
                { it.coerceIn(range).toString() },
                { it.toInt().coerceIn(range) }
            )

        fun clampedFloat(range: ClosedFloatingPointRange<Float>): Serializer<Float> =
            create(
                { it.coerceIn(range).toString() },
                { it.toFloat().coerceIn(range) }
            )

        @Suppress("UNCHECKED_CAST", "MoveLambdaOutsideParentheses")
        fun <T> createEnumSerializer(enumEntries: EnumEntries<T>): Serializer<T> where T : Enum<T>, T : GdEnum =
            create(
                Any::toString,
                { str -> enumEntries.first { it.getValue() == str.toInt() } }
            )
    }
}
