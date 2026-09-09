package client

/**
 * A list of secrets used for requests
 * @see GDClient
 */
@GDClientApi
enum class Secret(val secret: String) {
    COMMON("Wmfd2893gb7"),
    ACCOUNT("Wmfv3899gc9"),
    LEVEL("Wmfv2898gc9"),
    MOD("Wmfp3879gc3")
}