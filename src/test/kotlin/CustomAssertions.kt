import editor.rawstring.RawStringFactory
import org.opentest4j.AssertionFailedError

object CustomAssertions {
    fun assertRawStringEquals(expected: String, actual: String) {
        val res = RawStringFactory.areRawStringEquals(expected, actual)
        if (!res)
            throw AssertionFailedError("Raw strings are different", expected, actual)
    }

    fun assertRawStringNotEquals(unexpected: String, actual: String) {
        val res = RawStringFactory.areRawStringEquals(unexpected, actual)
        if (res)
            throw AssertionFailedError("Raw strings are equal", unexpected, actual)
    }
}