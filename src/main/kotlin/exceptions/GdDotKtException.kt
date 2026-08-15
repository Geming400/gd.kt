package exceptions

/**
 * Descendant of every exception for the `gd.kt` lib
 */
open class GdDotKtException : RuntimeException {
    internal constructor() : super()
    internal constructor(message: String) : super(message)
    internal constructor(message: String, cause: Throwable) : super(message, cause)
    internal constructor(cause: Throwable) : super(cause)
}