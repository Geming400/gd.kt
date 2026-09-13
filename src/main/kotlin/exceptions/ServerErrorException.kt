package exceptions

class ServerErrorException(message: String) : GdDotKtException(message) {
    companion object {
        fun genericError(): ServerErrorException =
            ServerErrorException("Server returned error code -1: Generic Error")
    }

    constructor(errorCode: Any, errorMessage: String) : this("Server returned error code $errorCode: $errorMessage")
}