package client

import client.struct.ServerStructure
import client.struct.ServerStructureCompanion
import client.struct.UserInfo
import exceptions.InvalidRawStringException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import utils.toFormRequestBody
import java.io.IOException
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.random.Random

@GDClientApi
typealias ResponseHandler = (body: String) -> Unit

@GDClientApi
abstract class AbstractGDClient(
    val credentials: Credentials? = null,
    val url: HttpUrl = DEFAULT_URL,

    val gameVersion: UInt = GAME_VERSION,
    val binaryVersion: UInt = BINARY_VERSION,
    val platform: Platform = Platform.get()
) {
    companion object {
        val DEFAULT_URL = "https://www.boomlings.com/".toHttpUrl()
        const val GAME_VERSION = 22u
        const val BINARY_VERSION = 47u
    }

    protected val client = OkHttpClient()

    protected fun resolveURL(endpoint: Endpoint): HttpUrl =
        endpoint.resolve(this.url)

    protected open fun createRequest(endpoint: Endpoint): Request.Builder =
        Request.Builder()
            .url(this.resolveURL(endpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("User-Agent", "")

    protected fun <T : ServerStructure> executeRequest(
        serverStructureCompanion: ServerStructureCompanion<T>,
        endpoint: Endpoint,
        data: Map<Any, Any>,
        asyncCallback: CallbackWithData<T>? = null,
        secret: Secret = Secret.COMMON,
        postProcessingReqBuilder: (Request.Builder) -> Request.Builder = { it }
    ): Result<T> {
        val reqBuilder = postProcessingReqBuilder(
            this.createRequest(endpoint)
                .post(data.toFormRequestBodyWithClientInfo(this, secret))
        )

        val call = this.client.newCall(reqBuilder.build())
        if (asyncCallback == null) {
            // if asyncCallback == null then we do synchronous requests
            try {
                call.execute().use { response ->
                    val res = Result.success(serverStructureCompanion.parse(response.body.string(), this))
                    return res
                }
            } catch (e: IOException) {
                return Result.failure(e)
            } catch (e: Exception) {
                return Result.failure(e)
            }
        } else {
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) =
                    asyncCallback.onNetworkFailure(call, e)

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val parsedData = serverStructureCompanion.parse(response.body.string(), this@AbstractGDClient)
                        asyncCallback.onResponse(call, response, parsedData)
                    } catch (e: InvalidRawStringException) {
                        asyncCallback.onParsingFailure(call, e)
                    }
                }
            })

            return Result.failure(NullPointerException("Cannot get a return value on async requests"))
        }
    }

    fun isLoggedIn(): Boolean =
        this.credentials != null
}

@GDClientApi
class GDClient(
    credentials: Credentials? = null,
    url: HttpUrl = DEFAULT_URL,

    gameVersion: UInt = GAME_VERSION,
    binaryVersion: UInt = BINARY_VERSION,
    platform: Platform = Platform.get()
) : AbstractGDClient(credentials, url, gameVersion, binaryVersion, platform) {
    fun getUserInfo(accountID: Int): Result<UserInfo> =
        this.executeRequest(
            UserInfo,
            Endpoint.GET_USER_INFO,
            mapOf(
                Pair("targetAccountID", accountID)
            ),
            null
        )
}

@GDClientApi
class AsyncGDClient(
    credentials: Credentials? = null,
    url: HttpUrl = DEFAULT_URL,

    gameVersion: UInt = GAME_VERSION,
    binaryVersion: UInt = BINARY_VERSION,
    platform: Platform = Platform.get()
) : AbstractGDClient(credentials, url, gameVersion, binaryVersion, platform) {
    fun getUserInfo(accountID: Int, asyncCallback: CallbackWithData<UserInfo>) {
        this.executeRequest(
            UserInfo,
            Endpoint.GET_USER_INFO,
            mapOf(
                Pair("targetAccountID", accountID)
            ),
            asyncCallback
        )
    }
}

// The 'Any' upper bound is to prevent null types
@GDClientApi
fun <K : Any, V : Any> Map<K, V>.toFormRequestBodyWithClientInfo(client: AbstractGDClient, secret: Secret): FormBody {
    val bodyBuilder = this.toFormRequestBody()
    bodyBuilder.add("secret", secret.secret)
    bodyBuilder.add("gameVersion", client.gameVersion.toString())
    bodyBuilder.add("gameVersion", client.binaryVersion.toString())
    bodyBuilder.add("dvs", client.platform.value.toString())
    if (client.credentials != null)
        bodyBuilder.add("gjp2", client.credentials.gjp2.encryptedPassword)
    bodyBuilder.add("uuid", UUID.randomUUID().toString())

    return bodyBuilder.build()
}

@GDClientApi
interface CallbackWithData<T> {
    @Throws(IOException::class)
    fun onNetworkFailure(
        call: Call,
        e: IOException,
    )

    fun onParsingFailure(
        call: Call,
        e: InvalidRawStringException,
    ) {}

    @Throws(IOException::class)
    fun onResponse(
        call: Call,
        response: Response,
        data: T
    )
}
