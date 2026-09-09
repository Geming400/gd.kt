package client

import client.struct.ServerStructure
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import utils.toFormRequestBody
import java.util.*

open class GDClient(
    val credentials: Credentials? = null,
    val url: HttpUrl = DEFAULT_URL,

    val gameVersion: UInt = GAME_VERSION,
    val binaryVersion: UInt = BINARY_VERSION
) {
    companion object {
        val DEFAULT_URL = "https://www.boomlings.com/".toHttpUrl()
        const val GAME_VERSION = 22u
        const val BINARY_VERSION = 47u
    }

    protected val client = OkHttpClient()

    protected fun resolveURL(endpoint: Endpoint): HttpUrl =
        endpoint.resolve(this.url)

    protected fun createRequest(endpoint: Endpoint): Request.Builder =
        Request.Builder()
            .url(this.resolveURL(endpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("User-Agent", "")

    fun isLoggedIn(): Boolean =
        this.credentials != null

    fun fetchLevel() {
        val req = this.createRequest(Endpoint.GET_USER_INFO)
            .post(mapOf(
                // TODO
                Pair("a", "a")
            ).toFormRequestBodyWithClientInfo(this, Secret.COMMON))
    }

    fun getUserInfo(accountID: Int): Result<ServerStructure> {
        val req = this.createRequest(Endpoint.GET_USER_INFO)
            .post(mapOf(
                Pair("targetAccountID", accountID)
            ).toFormRequestBodyWithClientInfo(this, Secret.COMMON))
            .build()

        try {
            this.client.newCall(req).execute().use { response ->
                println("Received: ${response.body.string()}")
                return Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}

// The 'Any' upper bound is to prevent null types
fun <K : Any, V : Any> Map<K, V>.toFormRequestBodyWithClientInfo(client: GDClient, secret: Secret): FormBody {
    val bodyBuilder = this.toFormRequestBody()
    bodyBuilder.add("secret", secret.secret)
    bodyBuilder.add("gameVersion", client.gameVersion.toString())
    bodyBuilder.add("gameVersion", client.binaryVersion.toString())
    if (client.credentials != null)
        bodyBuilder.add("gjp2", client.credentials.gjp2.encryptedPassword)
    bodyBuilder.add("uuid", UUID.randomUUID().toString())

    return bodyBuilder.build()
}
