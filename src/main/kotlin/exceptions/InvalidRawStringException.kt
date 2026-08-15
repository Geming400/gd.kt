package exceptions

class InvalidRawStringException : GdDotKtException {
    val rawString: String?

    constructor(rawString: String?) : super(getErrorMessage(rawString)) {
        this.rawString = rawString
    }

    constructor(rawString: String?, cause: Throwable) : super(getErrorMessage(rawString), cause) {
        this.rawString = rawString
    }

    companion object {
        private fun getErrorMessage(rawString: String?): String {
            return "Raw string '$rawString' is malformed or invalid."
        }
    }
}