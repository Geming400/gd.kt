package exceptions

/**
 * An exception that is thrown only and only if a client tries to do
 * an action that requires to be logged in
 * @see client.GDClient.throwIfLoggedOut
 */
class LoggedOutException(message: String = "This client must be logged in, but it isn't") : GdDotKtException(message)